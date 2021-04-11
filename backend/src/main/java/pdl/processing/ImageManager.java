package pdl.processing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.scijava.Context;
import org.scijava.io.location.BytesLocation;
import org.springframework.http.MediaType;

import io.scif.FormatException;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.Writer;
import io.scif.formats.JPEGFormat;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import pdl.backend.AcceptedMediaTypes;
import pdl.backend.Utils;
import pdl.backend.mysqldb.Image;

public class ImageManager
{
    public static SCIFIOImgPlus<UnsignedByteType> createImage(final SCIFIOImgPlus<UnsignedByteType> img)
    {
        return img.copy();
    }

    public static SCIFIOImgPlus<UnsignedByteType> createImage(final long width, final Long height)
    {
        System.err.printf("ImageManager.createImage is still in progress!\n");

        final ImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
        final Img<UnsignedByteType> output = factory.create(new long[] { width, height, 3 });

        final Cursor<UnsignedByteType> cursor = output.cursor();

        while (cursor.hasNext())
        {
            cursor.fwd();
            cursor.get().set(0);
        }

        return new SCIFIOImgPlus<UnsignedByteType>(output);
    }

    public static SCIFIOImgPlus<UnsignedByteType> imageFromJPEGBytes(final byte[] data)
    throws FormatException, IOException
    {
        final ImgOpener imgOpener = new ImgOpener();
        final Context context = imgOpener.getContext();
        final SCIFIO scifio = new SCIFIO(context);
        final JPEGFormat format = scifio.format().getFormatFromClass(JPEGFormat.class);
        final Reader reader = format.createReader();
        final BytesLocation location = new BytesLocation(data);
        reader.setSource(location);
        final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<UnsignedByteType>(new UnsignedByteType());
        final SCIFIOImgPlus<UnsignedByteType> img = imgOpener.openImgs(reader, factory, null).get(0);
        context.dispose();
        return img;
    }

    public static byte[] imageToJPEGBytes(final SCIFIOImgPlus<UnsignedByteType> img)
    throws FormatException, IOException
    {
        final ImgSaver imgSaver = new ImgSaver();
        final Context context = imgSaver.getContext();
        final SCIFIO scifio = new SCIFIO(context);
        final JPEGFormat format = scifio.format().getFormatFromClass(JPEGFormat.class);
        final Writer writer = format.createWriter();
        final BytesLocation location = new BytesLocation(10);
        writer.setMetadata(img.getMetadata());
        writer.setDest(location);
        imgSaver.saveImg(writer, img);
        final byte[] imageInByte = location.getByteBank().toByteArray();
        context.dispose();
        return imageInByte;
    }


    public static Image convertImage(final Image input, final MediaType type)
    throws IOException
    {
        if (input.getType().equals(type))
            throw new IllegalArgumentException("Input image already has type " + type.toString());

        if (!AcceptedMediaTypes.contains(type.toString()))
            throw new IllegalArgumentException(type.toString() + " is not supported");

        final BufferedImage bufferedImageInput = ImageIO.read(new ByteArrayInputStream(input.getData()));
        final File output = new File(input.getNameWithoutExtension() + type.getSubtype());

        if (!ImageIO.write(bufferedImageInput, type.getSubtype(), output))
            throw new IOException("Could not convert TIFF to JPG");

        final Image image = new Image(output.getName(), Files.readAllBytes(output.toPath()), Utils.typeOfFile(output), Utils.sizeOfImage(output));
        output.delete();
        return image;
    }
}
