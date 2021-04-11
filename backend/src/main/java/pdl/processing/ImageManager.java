package pdl.processing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

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

    public static SCIFIOImgPlus<UnsignedByteType> createImage(final long width, final long height)
    {
        System.err.printf("ImageManager.createImage is still in progress!\n");

        final ImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
        final Img<UnsignedByteType> output = factory.create(new long[] { width, height, 3 });

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


    /**
     * Change the format of an image, for example from png -> jpeg
     * Note this function doesn't take in consideration any compression, the conversion is done with java.io API and javax.imageio
     * 
     * @param input Image to convert
     * @param type MediaType type to convert to
     * @return Image a new image converted to the right format
     * @throws IOException If conversion fails
     * @throws IllegalArgumentException If type is not supported or the image input already have the same type
     */
    public static Image convertImage(final Image input, final MediaType type)
    throws IOException, IllegalArgumentException
    {
        if (input.getType().equals(type))
            throw new IllegalArgumentException("Input image already has type " + type.toString());

        if (!AcceptedMediaTypes.contains(type.toString()))
            throw new IllegalArgumentException(type.toString() + " is not supported");

        final BufferedImage bufferedImageInput = ImageIO.read(new ByteArrayInputStream(input.getData()));
       
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream outputStream = output;
        if (!ImageIO.write(bufferedImageInput, type.getSubtype(), outputStream))
            throw new IOException("Could not convert: " + input.getType() + " to: " + type.toString());

        return new Image(input.getNameWithoutExtension() + "." + type.getSubtype(), output.toByteArray(), type, input.getSize());
        
    }
    
    /**
     * Convert bytes to a desired format
     * !!! This function does not have any idea of the format of the bytes given, it is to the caller of the function to know in which format the given bytes are
     * @param bytes byte[] to convert
     * @param type MediaType format wanted
     * @return byte[]
     * @throws IOException If conversion fails
     */
    public static byte[] convertImageBytes(final byte[] bytes, MediaType type) throws IOException{
        final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes)); // convert byte to bufferedImage
        ByteArrayOutputStream output = new ByteArrayOutputStream(); // create a output stream byte[]
        OutputStream outputStream = output;
        if(!ImageIO.write(bufferedImage, type.getSubtype(), outputStream)) // write to the outputstream here byte array
            throw new IOException("Could not do conversion to: " + type.toString());
        
        return output.toByteArray();
    }
    
}
