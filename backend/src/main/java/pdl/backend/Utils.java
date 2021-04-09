package pdl.backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public final class Utils {

    /**
     * List all the .jpeg and .tif files for the given path
     * 
     * @param p Path to look at
     * @return Set<String> Set of String corresponding to each file .jpeg and .tif
     *         at the given path
     * @throws IOException
     */
    public static Set<String> listFiles(Path p) throws IOException {
        try (Stream<Path> stream = Files.walk(p)) {
            return stream.map(Path::getFileName).map(Path::toString)
                    .filter(file -> file.endsWith(".jpeg") || file.endsWith(".tif")).collect(Collectors.toSet());
        }
    }

    /**
     * Give the type of an Image
     * 
     * @param file File to check from
     * @return MediaType type of the file
     * @throws IOException               If impossible to determine the type of the
     *                                   file
     * @throws InvalidMediaTypeException If the type of media cannot be parsed
     */
    public static MediaType typeOfFile(File file) throws IOException, InvalidMediaTypeException {
        return MediaType.parseMediaType(Files.probeContentType(file.toPath()));
    }

    public static MediaType typeOfFile(Path p) throws IOException, InvalidMediaTypeException {
        return MediaType.parseMediaType(Files.probeContentType(p));
    }

    /**
     * Give the size of an Image in a string of the form :
     * "width*height*numberOfComponentsInColorModel" example : "680*480*3" width:
     * 680, height: 480, 3 components This function assume that the file is an
     * Image, it's up to the caller to be sure to pass an Image as parameter
     * 
     * @param file File to
     * @return String
     * @throws IOException If impossible to read the file as a BufferedImage
     */
    public static String sizeOfImage(File file) throws IOException {
        return "" + ImageIO.read(file).getWidth() + "*" + ImageIO.read(file).getHeight() + "*"
                + ImageIO.read(file).getColorModel().getNumComponents();
    }

    public static String sizeOfImage(Path p) throws IOException {
        File f = p.toFile();
        return "" + ImageIO.read(f).getWidth() + "*" + ImageIO.read(f).getHeight() + "*"
                + ImageIO.read(f).getColorModel().getNumComponents();
    }

    /**
     * Give the type of an Image
     * 
     * @param file MultipartFile
     * @return MediaType
     * @throws InvalidMediaTypeException If impossible to parse the type of the file
     */
    public static MediaType typeOfFile(MultipartFile file) throws InvalidMediaTypeException {
        return MediaType.parseMediaType(file.getContentType());
    }

    /**
     * Give the size of an Image in a string of the form :
     * "width*height*numberOfComponentsInColorModel" example : "680*480*3" width:
     * 680, height: 480, 3 components This function assume that the file is an
     * Image, it's up to the caller to be sure to pass an Image as parameter
     * 
     * @param file MultipartFile
     * @return String
     * @throws IOException If impossible to read the file as a BufferedImage
     */
    public static String sizeOfImage(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        return "" + bufferedImage.getWidth() + "*" + bufferedImage.getHeight() + "*"
                + bufferedImage.getColorModel().getNumComponents();
    }

    /**
     * Return a random Number depending on the type specified
     * int, float long, or double
     * @param c Class<?> to specify the number
     * @param min int minimum
     * @param max int maxmum inclusive
     * @return Obect 
     * @throws IllegalArgumentException If the min >= max or wrong Class passed in parameters
     */
    public static Object getRandomNumber(Class<?> c, int min, int max) throws IllegalArgumentException {
        if(min >= max){
            throw new IllegalArgumentException("max has to be greater than min !");
        }
        Random r = new Random();
        if(c == int.class){
            return Integer.valueOf(r.nextInt((max - min) + 1) + min);
        }else if(c == float.class){
            return Float.valueOf(r.nextFloat()*((float)max));
        }else if(c == double.class){
            return Double.valueOf(r.nextDouble()*((double)max));
        }else if(c == long.class){
            return Long.valueOf(r.nextInt((max - min) + 1) + min);
        }

        throw new IllegalArgumentException("Class must be either int, float, double or long, you gave :" + c.getName());
    
    }


}