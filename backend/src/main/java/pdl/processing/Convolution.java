package pdl.processing;

import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class Convolution {
    private static double[][] generateMeanFilter(final int radius) {
        final int n = 2 * radius + 1;
        final double[][] kernel = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                kernel[i][j] = 1. / n * n;

        return kernel;
    }

    private static double[][] generateGaussianFilter(final int radius, final double sigma) {
        final int n = 2 * radius + 1;
        final double[][] kernel = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                kernel[i][j] = Math.exp(-.5 * (Math.pow((i - radius) / sigma, 2.) + Math.pow((j - radius) / sigma, 2.)))
                        / (2. * Math.PI * sigma * sigma);

        return kernel;
    }

    private static void convolution(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final double[][] kernel) {
        System.out.println("Image dimensions: " + input.dimension(0) + ", " + input.dimension(1));

        final int radius = kernel.length / 2;
        System.out.println("Kernel radius: " + radius);

        final Cursor<UnsignedByteType> outputRA = output.cursor();

        for (int c = 0; c < 3; c++) {
            final IntervalView<UnsignedByteType> slice = Views.hyperSlice(input, 2, c);
            final RandomAccessibleInterval<UnsignedByteType> inputRA = Views.expandMirrorDouble(slice, radius, radius);
            final RandomAccessibleInterval<UnsignedByteType> interval = Views.interval(inputRA, slice);

            for (final Neighborhood<UnsignedByteType> neighborhood : new RectangleShape(radius, false)
                    .neighborhoods(interval)) {
                outputRA.fwd();

                double value = 0;
                final Cursor<UnsignedByteType> neighborhoodCursor = neighborhood.cursor();
                for (int i = 0; i < kernel.length; i++)
                    for (int j = 0; j < kernel.length; j++) {
                        neighborhoodCursor.fwd();
                        value += neighborhoodCursor.get().get() * kernel[i][j];
                    }

                if (value < 0) value = 0;
                if (value > 255) value = 255;

                outputRA.get().set((int) value);
            }
        }
    }

    public static void meanFilter(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final int radius) {
        convolution(input, output, generateMeanFilter(radius));
    }

    public static void gaussianFilter(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
            final int radius) {
        convolution(input, output, generateGaussianFilter(radius, 4. / 3.));
    }

    public static void sobelOperator(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {
        final double[][] sobelX = { { -1., 0., 1. }, { -2., 0., 2. }, { -1., 0., 1. } };
        final double[][] sobelY = { { -1., -2., -1. }, { 0., 0., 0. }, { 1., 2., 1. } };

        final Img<UnsignedByteType> inputGrayscale = input.copy();
        Processing.toGrayscale(input, inputGrayscale);

        final Img<UnsignedByteType> inputX = inputGrayscale.copy();
        final Img<UnsignedByteType> inputY = inputGrayscale.copy();

        convolution(inputGrayscale, inputX, sobelX);
        convolution(inputGrayscale, inputY, sobelY);

        final Cursor<UnsignedByteType> inputXCursor = inputX.cursor();
        final Cursor<UnsignedByteType> inputYCursor = inputY.cursor();
        final Cursor<UnsignedByteType> outputCursor = output.cursor();

        while (outputCursor.hasNext()) {
            inputXCursor.fwd();
            inputYCursor.fwd();
            outputCursor.fwd();

            double value = Math.sqrt(inputXCursor.get().get() * inputXCursor.get().get() + inputYCursor.get().get() * inputYCursor.get().get());

            if (value < 0) value = 0;
            if (value > 255) value = 255;
            
            outputCursor.get().set((int) value);
        }
    }
}