package pdl.processing;

import io.scif.img.SCIFIOImgPlus;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Processing
{
    public static void toGrayscale(final SCIFIOImgPlus<UnsignedByteType> input, final SCIFIOImgPlus<UnsignedByteType> output)
    {
        System.out.printf("Converting to grayscale...\n");
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.getImg().randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.getImg().randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++)
            {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                final int[] rgb = new int[(int) input.dimension(2)];
                for (long channel = input.min(2); channel <= input.max(2); channel++)
                {
                    inputRandomAccess.setPosition(channel, 2);
                    rgb[(int) channel] = inputRandomAccess.get().get();
                }

                final int value = (int) (.3 * rgb[0] + .59 * rgb[1] + .11 * rgb[2]);
                for (long channel = input.min(2); channel <= input.max(2); channel++)
                {
                    outputRandomAccess.setPosition(channel, 2);
                    outputRandomAccess.get().set(value);
                }
            }
        }
    }

    public static void changeBrightness(final SCIFIOImgPlus<UnsignedByteType> input, final SCIFIOImgPlus<UnsignedByteType> output, final int gain)
    {
        if (gain < -255 || gain > 255)
        {
            System.err.printf("Could not apply a gain of %d to the brightness!\n", gain);
            return;
        }

        System.out.printf("Applying a gain of %d to the brightness...\n", gain);
        final Cursor<UnsignedByteType> inputCursor = input.cursor();
        final Cursor<UnsignedByteType> outputCursor = output.cursor();

        while (inputCursor.hasNext())
        {
            inputCursor.fwd();
            outputCursor.fwd();

            int value = inputCursor.get().get() + gain;

            if (value < 0)
                value = 0;
            if (value > 255)
                value = 255;

            outputCursor.get().set(value);
        }
    }

    public static void colorize(final SCIFIOImgPlus<UnsignedByteType> input, final SCIFIOImgPlus<UnsignedByteType> output, final float hue)
    {
        if (hue < 0f || hue > 360f)
        {
            System.err.printf("Could not colorize using hue %f!\n", hue);
            return;
        }

        System.out.printf("Colorizing using hue %f...\n", hue);
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++)
            {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                int[] rgb = new int[(int) input.dimension(2)];
                for (long channel = input.min(2); channel <= input.max(2); channel++)
                {
                    inputRandomAccess.setPosition(channel, 2);
                    rgb[(int) channel] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                hsv[0] = hue;

                rgb = Computing.hsvToRgb(hsv);
                for (long channel = input.min(2); channel <= input.max(2); channel++)
                {
                    outputRandomAccess.setPosition(channel, 2);
                    outputRandomAccess.get().set(rgb[(int) channel]);
                }
            }
        }
    }

    public static void extendDynamics(final SCIFIOImgPlus<UnsignedByteType> input, final SCIFIOImgPlus<UnsignedByteType> output)
    {
        final SCIFIOImgPlus<UnsignedByteType> inputGrayscale = input.copy();
        toGrayscale(input, inputGrayscale);

        System.out.printf("Extending dynamics...\n");

        final RandomAccess<UnsignedByteType> tempRandomAccess = inputGrayscale.randomAccess();

        final int LUT[] = new int[256];
        int min = 255, max = 0;

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            for (long y = input.min(1); y <= input.max(1); y++)
            {
                tempRandomAccess.setPosition(x, 0);
                tempRandomAccess.setPosition(y, 1);
                tempRandomAccess.setPosition(input.min(2), 2);

                final int value = tempRandomAccess.get().get();

                if (min > value)
                    min = value;
                if (max < value)
                    max = value;
            }
        }

        for (int i = 0; i < 256; i++)
            LUT[i] = (int) (((double) (i - min) / (double) (max - min)) * 255);

        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++)
            {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                for (long channel = input.min(2); channel <= input.max(2); channel++)
                {
                    inputRandomAccess.setPosition(channel, 2);
                    outputRandomAccess.setPosition(channel, 2);

                    int value = LUT[inputRandomAccess.get().get()];

                    if (value < 0)
                        value = 0;
                    if (value > 255)
                        value = 255;

                    outputRandomAccess.get().set(value);
                }
            }
        }
    }

    public static void equalizeHistogram(final SCIFIOImgPlus<UnsignedByteType> input, final SCIFIOImgPlus<UnsignedByteType> output, final int channel)
    {
        final int bits = 1000;

        if (channel != 1 && channel != 2)
        {
            System.err.printf("Could not equalize histogram using channel %d!", channel);
            return;
        }

        System.out.printf("Equalizing histogram on channel %d...\n", channel);
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess(), outputRandomAccess = output.randomAccess();
        final int[] histogram = new int[bits + 1];
        int total = 0;

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            inputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++)
            {
                inputRandomAccess.setPosition(y, 1);

                final int[] rgb = new int[3];
                for (int c = 0; c < 3; c++)
                {
                    inputRandomAccess.setPosition(c, 2);
                    rgb[c] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                histogram[(int) (hsv[channel] * bits)]++;
                total++;
            }
        }

        for (int i = 1; i <= bits; i++)
            histogram[i] += histogram[i - 1];

        for (long x = input.min(0); x <= input.max(0); x++)
        {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++)
            {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                int[] rgb = new int[3];
                for (int c = 0; c < 3; c++)
                {
                    inputRandomAccess.setPosition(c, 2);
                    rgb[c] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                hsv[channel] = (float) (histogram[(int) (hsv[channel] * bits)]) / (float) total;
                rgb = Computing.hsvToRgb(hsv);

                for (int c = 0; c < 3; c++)
                {
                    outputRandomAccess.setPosition(c, 2);
                    outputRandomAccess.get().set(rgb[c]);
                }
            }
        }
    }
}
