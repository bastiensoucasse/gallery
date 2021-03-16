package pdl.processing;

import java.io.File;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class Convolution {
    // private static final int[][] meanFilter = new int[][] { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 } };
    // private static final int[][] gaussianFilter = new int[][] { { 1, 2, 3, 2, 1 }, { 2, 6, 8, 6, 2 }, { 3, 8, 10, 8, 3 }, { 2, 6, 8, 6, 2 }, { 1, 2, 3, 2, 1 } };

    public static void meanFilterSimple(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, final int radius) {
        final double coef = 1. / Math.pow(2. * (double) radius + 1., 2.);

        final RandomAccess<UnsignedByteType> inputRandomAccess = input.randomAccess();
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            for (long y = input.min(1); y <= input.max(1); y++) {
                outputRandomAccess.setPosition(x, 0);
                outputRandomAccess.setPosition(y, 1);

                if (x < input.min(0) + radius || x > input.max(0) - radius || y < input.min(1) + radius || y > input.max(1) - radius) {
                    inputRandomAccess.setPosition(x, 0);
                    inputRandomAccess.setPosition(y, 1);

                    outputRandomAccess.get().set(inputRandomAccess.get().get());
                    continue;
                }

                int value = 0;

                for (long v = -radius; v <= radius; v++) {
                    for (long u = -radius; u <= radius; u++) {
                        inputRandomAccess.setPosition(x + u, 0);
                        inputRandomAccess.setPosition(y + v, 1);

                        value += (int) ((double) inputRandomAccess.get().get() * coef);
                    }
                }

                if (value < 0) value = 0;
                if (value > 255) value = 255;

                outputRandomAccess.get().set(value);
            }
        }
    }

    public static void meanFilterSimple(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {
        meanFilterSimple(input, output, 1);
    }

    public static void meanFilterWithBorders(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, final int radius) {
        final double coef = 1. / ((2 * radius + 1) * (2 * radius + 1));

        final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendMirrorDouble(input);
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            for (long y = input.min(1); y <= input.max(1); y++) {
                outputRandomAccess.setPosition(x, 0);
                outputRandomAccess.setPosition(y, 1);

                int value = 0;

                final IterableInterval<UnsignedByteType> neighborhood = Views.interval(extendedView, new long[] { x - radius, y - radius }, new long[] { x + radius, y + radius });
                final Cursor<UnsignedByteType> intervalCursor = neighborhood.cursor();

                while (intervalCursor.hasNext()) {
                    intervalCursor.fwd();
                    value += (int) ((double) intervalCursor.get().get() * coef);
                }

                if (value < 0) value = 0;
                if (value > 255) value = 255;

                outputRandomAccess.get().set(value);
            }
        }
    }

    public static void meanFilterWithNeighborhood(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, final int radius) {
        final double coef = 1. / ((2 * radius + 1) * (2 * radius + 1));

        final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendMirrorDouble(input);
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();
        final IntervalView<UnsignedByteType> intervalView = Views.interval(extendedView, input);

        for (final Neighborhood<UnsignedByteType> neighborhood : new RectangleShape(radius, false).neighborhoods(intervalView)) {
            outputRandomAccess.setPosition(neighborhood);

            int value = 0;

            for (final UnsignedByteType neighbor : neighborhood)
                value += (int) ((double) neighbor.get() * coef);

            if (value < 0) value = 0;
            if (value > 255) value = 255;

            outputRandomAccess.get().set(value);
        }
    }

    public static void convolution(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, final int[][] kernel) {
        final int radius = kernel.length / 2;
        int sum = 0;

        for (int x = 0; x < kernel.length; x++)
            for (int y = 0; y < kernel.length; y++)
                sum += kernel[x][y];

        final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendMirrorDouble(input);
        final RandomAccess<UnsignedByteType> outputRandomAccess = output.randomAccess();

        for (long x = input.min(0); x <= input.max(0); x++) {
            for (long y = input.min(1); y <= input.max(1); y++) {
                outputRandomAccess.setPosition(x, 0);
                outputRandomAccess.setPosition(y, 1);

                int value = 0;

                final IterableInterval<UnsignedByteType> neighborhood = Views.interval(extendedView, new long[] { x - radius, y - radius }, new long[] { x + radius, y + radius });
                final Cursor<UnsignedByteType> intervalCursor = neighborhood.cursor();

                int count = 0;
                while (intervalCursor.hasNext()) {
                    intervalCursor.fwd();

                    final int u = count % kernel.length;
                    final int v = count / kernel.length;
                    value += (int) ((double) intervalCursor.get().get() / (double) sum * (double) kernel[u][v]);

                    count++;
                }

                if (value < 0) value = 0;
                if (value > 255) value = 255;

                outputRandomAccess.get().set(value);
            }
        }
    }

    public static void gaussFilterImgLib(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output, final double sigma) {
        final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendMirrorDouble(input);
        Gauss3.gauss(sigma, extendedView, output);
    }

    public static void gaussFilterImgLib(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {
        gaussFilterImgLib(input, output, 4. / 3.);
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
        double minimalDuration = Double.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            final long startTime = System.nanoTime();
            // function to test
            final long elapsedNanos = System.nanoTime() - startTime;
            final double elapsedMillis = elapsedNanos / 1e+6;
            if (minimalDuration > elapsedMillis)
                minimalDuration = elapsedMillis;
        }
        System.out.println("Minimal duration: " + minimalDuration + " ms.");

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
