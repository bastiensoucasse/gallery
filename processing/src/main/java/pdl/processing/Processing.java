package pdl.processing;

public class Processing {
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
