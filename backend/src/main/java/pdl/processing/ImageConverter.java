package pdl.processing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.scijava.Context;
import org.scijava.io.location.BytesLocation;
import org.springframework.http.MediaType;

import io.scif.BufferedImagePlane;
import io.scif.FormatException;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.Writer;
import io.scif.formats.JPEGFormat;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import pdl.backend.AcceptedMediaTypes;
import pdl.backend.Utils;
import pdl.backend.mysqldb.Image;

public class ImageConverter {


    public static SCIFIOImgPlus<UnsignedByteType> imageFromJPEGBytes(final byte[] data)
            throws FormatException, IOException {
        final ImgOpener imgOpener = new ImgOpener();
        final Context c = imgOpener.getContext();
        final SCIFIO scifio = new SCIFIO(c);
        final JPEGFormat format = scifio.format().getFormatFromClass(JPEGFormat.class);
        final Reader r = format.createReader();
        final BytesLocation location = new BytesLocation(data);
        r.setSource(location);
        final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<UnsignedByteType>(new UnsignedByteType());
        final SCIFIOImgPlus<UnsignedByteType> img = imgOpener.openImgs(r, factory, null).get(0);
        c.dispose();
        return img;
    }

    

    public static byte[] imageToJPEGBytes(final SCIFIOImgPlus<UnsignedByteType> img)
            throws FormatException, IOException {
        final ImgSaver imgSaver = new ImgSaver();
        final Context c = imgSaver.getContext();
        final SCIFIO scifio = new SCIFIO(c);
        final JPEGFormat format = scifio.format().getFormatFromClass(JPEGFormat.class);
        final Writer w = format.createWriter();
        final BytesLocation saveLocation = new BytesLocation(10);
        w.setMetadata(img.getMetadata());
        w.setDest(saveLocation);
        imgSaver.saveImg(w, img);
        final byte[] imageInByte = saveLocation.getByteBank().toByteArray();
        c.dispose();
        return imageInByte;
    }


    public static Image convertImage(final Image input, MediaType type) throws IOException {
        if(input.getType().equals(type))
            throw new IllegalArgumentException("input image has already type: " + type.toString());
        if(!AcceptedMediaTypes.contains(type.toString()))
            throw new IllegalArgumentException(type.toString() + " is not supported");

        final BufferedImage bufferedImageInput = ImageIO.read(new ByteArrayInputStream(input.getData()));
        File output = new File(input.getNameWithoutExtension() + type.getSubtype());
        
        if(!ImageIO.write(bufferedImageInput, type.getSubtype(), output))
            throw new IOException("Could not convert TIFF to jpg !");

        Image image = new Image(output.getName(), Files.readAllBytes(output.toPath()), Utils.typeOfFile(output), Utils.sizeOfImage(output));
        output.delete();
        return image;
    }

    
}
