package pdl.processing;

import java.io.File;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Processing {
    public static void changeBrightness(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final int delta) {
        final Cursor<UnsignedByteType> inputCursor = input.cursor();
        final Cursor<UnsignedByteType> outputCursor = output.cursor();

        while (inputCursor.hasNext()) {
            inputCursor.fwd();
            outputCursor.fwd();

            int value = inputCursor.get().get() + delta;

            if (value < 0)
                value = 0;
            if (value > 255)
                value = 255;

            outputCursor.get().set(value);
        }
    }

    public static void toGrayscale(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++) {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                final int[] rgb = new int[(int) input.dimension(2)];
                for (long channel = input.min(2); channel <= input.max(2); channel++) {
                    inputRandomAccess.setPosition(channel, 2);
                    rgb[(int) channel] = inputRandomAccess.get().get();
                }

                final int value = (int) (.3 * rgb[0] + .59 * rgb[1] + .11 * rgb[2]);
                for (long channel = input.min(2); channel <= input.max(2); channel++) {
                    outputRandomAccess.setPosition(channel, 2);
                    outputRandomAccess.get().set(value);
                }
            }
        }
    }

    public static void colorize(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final float hue) {
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++) {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                int[] rgb = new int[(int) input.dimension(2)];
                for (long channel = input.min(2); channel <= input.max(2); channel++) {
                    inputRandomAccess.setPosition(channel, 2);
                    rgb[(int) channel] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                hsv[0] = hue;

                rgb = Computing.hsvToRgb(hsv);
                for (long channel = input.min(2); channel <= input.max(2); channel++) {
                    outputRandomAccess.setPosition(channel, 2);
                    outputRandomAccess.get().set(rgb[(int) channel]);
                }
            }
        }
    }

    public static void extendDynamics(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final Img<UnsignedByteType> temp, final int dmin, final int dmax) {
        toGrayscale(input, temp);

        final RandomAccess<UnsignedByteType> tempRandomAccess = temp.randomAccess();

        final int LUT[] = new int[256];

        int min = 255;
        int max = 0;

        for (long x = input.min(0); x <= input.max(0); x++) {
            for (long y = input.min(1); y <= input.max(1); y++) {
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
            LUT[i] = (int) ((double) ((i - min) / (double) (max - min)) * (double) (dmax - dmin) + (double) dmin);

        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++) {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                for (long channel = input.min(2); channel <= input.max(2); channel++) {
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

    public static void extendDynamics(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final Img<UnsignedByteType> temp) {
        extendDynamics(input, output, temp, 0, 255);
    }

    public static void equalizeHistogram(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final int channel) {
        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess(),
                outputRandomAccess = output.randomAccess();
        final int[] histogram = new int[101], cumulativeHistogram = new int[101];
        int total = 0;

        for (long x = input.min(0); x <= input.max(0); x++) {
            inputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++) {
                inputRandomAccess.setPosition(y, 1);

                final int[] rgb = new int[3];
                for (int c = 0; c < 3; c++) {
                    inputRandomAccess.setPosition(c, 2);
                    rgb[c] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                histogram[(int) (hsv[channel] * 100)]++;
                total++;
            }
        }

        for (int i = 0; i < 101; i++) {
            if (i == 0)
                cumulativeHistogram[i] = histogram[i];
            else
                cumulativeHistogram[i] = histogram[i] + cumulativeHistogram[i - 1];
        }

        for (long x = input.min(0); x <= input.max(0); x++) {
            inputRandomAccess.setPosition(x, 0);
            outputRandomAccess.setPosition(x, 0);

            for (long y = input.min(1); y <= input.max(1); y++) {
                inputRandomAccess.setPosition(y, 1);
                outputRandomAccess.setPosition(y, 1);

                int[] rgb = new int[3];
                for (int c = 0; c < 3; c++) {
                    inputRandomAccess.setPosition(c, 2);
                    rgb[c] = inputRandomAccess.get().get();
                }

                final float[] hsv = Computing.rgbToHsv(rgb);
                hsv[channel] = (float) (cumulativeHistogram[(int) (hsv[channel] * 100)]) / (float) total;
                rgb = Computing.hsvToRgb(hsv);

                for (int c = 0; c < 3; c++) {
                    outputRandomAccess.setPosition(c, 2);
                    outputRandomAccess.get().set(rgb[c]);
                }
            }
        }
    }

    public static void main(final String[] args) throws ImgIOException, IncompatibleTypeException {
        // Usage Check
        if (args.length < 2) {
            System.out.println("Missing input or output image filename.");
            System.exit(-1);
        }

        // Input Open
        final String inputFilename = args[0];
        final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
        final ImgOpener imgOpener = new ImgOpener();
        final Img<UnsignedByteType> input = (Img<UnsignedByteType>) imgOpener.openImgs(inputFilename, factory).get(0);
        imgOpener.context().dispose();
        final Img<UnsignedByteType> output = SCIFIOImgPlus.wrap(factory.create(input), SCIFIOImgPlus.wrap(input));
        System.out.println("Opened Image: " + inputFilename);

        // Process
        Convolution.contour(input, output);

        // Output Save
        final String outputFilename = args[1];
        final File path = new File(outputFilename);
        if (path.exists())
            path.delete();
        final ImgSaver imgSaver = new ImgSaver();
        imgSaver.saveImg(outputFilename, output);
        imgSaver.context().dispose();
        System.out.println("Saved Image: " + outputFilename);
    }
}
