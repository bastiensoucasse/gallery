package pdl.backend;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.Map;

import io.scif.img.SCIFIOImgPlus;
import net.imagej.ImgPlus;

import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import pdl.processing.ImageConverter;
import pdl.processing.Processing;

/**
 * This class is Implemented with the design Pattern Singleton This class is
 * Managing the execution of algorithms processing images.
 */
public class AlgorithmManager {

    private static AlgorithmManager _instance = null;
    private Map<String, Class<?>> algorithms;
    private Map<String, ArrayList<Class<?>[]>> parameters;
    private Class<?>[] Classes = { Processing.class };

    /**
     * To keep this class as a singleton the class as only one private constructor
     * to assure that no one can instantiate this class and only one instance of
     * this class then can exists.
     */
    private AlgorithmManager() {
        algorithms = new HashMap<>();
        parameters = new HashMap<>();
        for (Class<?> c : Classes) {
            for (Method m : c.getDeclaredMethods()) {
                String name = m.getName(); // get the name of the method
                System.out.println("----------- " + name + " -----------");
                algorithms.put(name, c); // add name of method and associated class
                if (parameters.containsKey(name)) { // if the method has already types associated
                    parameters.get(name).add(m.getParameterTypes()); // add the list of parameters to the list
                } else { // the method doesn't have any associated list of parameters at all
                    ArrayList<Class<?>[]> listOfParameters = new ArrayList<>(); // create a list of parameters
                    listOfParameters.add(m.getParameterTypes()); // add the list of parameters to the list
                    parameters.put(name, listOfParameters); // add to the map the method name and the corresponding list
                                                            // of types
                }
            }
        }
    }

    /**
     * Return the only instance of AlgorithmManager
     * 
     * @return AlgorithmManager
     */
    public static AlgorithmManager Instance() {
        if (_instance == null) {
            _instance = new AlgorithmManager();
        }
        return _instance;
    }

    /**
     * Parse a string into an integer
     * 
     * @param s String to parse
     * @return int integer parsed from the string
     * @throws NumberFormatException If error while trying to parse
     */
    private int parseStringAsInt(String s) throws NumberFormatException {
        return (int) Integer.parseInt(s);
    }

    private long parseStringAsLong(String s) throws NumberFormatException {
        return (int) Long.parseLong(s);
    }

    /**
     * Parse a string into a float
     * 
     * @param s String to parse
     * @return float float parsed from the string
     * @throws NumberFormatException If Error while trying to parse
     */
    private float parseStringAsFloat(String s) throws NumberFormatException {
        return (float) Float.parseFloat(s);
    }

    /**
     * Parse a string into an double
     * 
     * @param s String to parse
     * @return double parsed from the string
     * @throws NumberFormatException If Error while trying to parse
     */
    private double parseStringAsDouble(String s) throws NumberFormatException {
        return (double) Double.parseDouble(s);
    }

    private Object parseStringPrimitiveType(String s, Type t) throws NumberFormatException {
        if (t == int.class)
            return parseStringAsInt(s);
        else if (t == long.class)
            return parseStringAsLong(s);
        else if (t == float.class)
            return parseStringAsFloat(s);
        else if (t == double.class)
            return parseStringAsDouble(s);

        throw new NumberFormatException();
    }

    /**
     * Apply an algorithm to the image passed in parameters The name of the
     * algorithm can be passed as a string, and the arguments needed for the
     * algorithm in a Collection<String> This method does the parsing of arguments
     * 
     * @param name  String name of the algorithm
     * @param args  Collection<String> arguments needed for the algorithm
     * @param image Image to apply the algorithm on
     * @return Image processed Image after algorithm, else an exception will be
     *         thrown
     * @throws NumberFormatException    The parameters couldn't be parsed
     * @throws NoSuchMethodException    The Algorithm asked doesn't exists
     * @throws IllegalArgumentException Arguments passed not accepted, e.g too many
     *                                  Arguments or not enough
     */
    public Image applyAlgorithm(String name, Collection<String> args, Image image)
            throws Exception, NumberFormatException, NoSuchMethodException, IllegalArgumentException {
        Class<?> c = algorithms.get(name);
        if (c != null) {
            System.out.println("You got the class " + c.getName()); // debug

            SCIFIOImgPlus<UnsignedByteType> input = ImageConverter.imageFromJPEGBytes(image.getData()); // convert Image
                                                                                                        // to
                                                                                                        // ImgPlus<UnsignedByteType>
            final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<UnsignedByteType>(
                    new UnsignedByteType()); // ArrayImgFactory
            ImgPlus<UnsignedByteType> output = SCIFIOImgPlus.wrap(factory.create(input), SCIFIOImgPlus.wrap(input)); // Create
                                                                                                                     // an
                                                                                                                     // output
                                                                                                                     // image

            Object[] arguments = new Object[args.size() + 2]; // list of arguments to apply to the method
            arguments[0] = input;
            arguments[1] = output;

            ArrayList<Class<?>[]> typeOfParameters = parameters.get(name);
            int type = parseParameters(args, typeOfParameters, arguments);

            System.out.println("After Parsing " + type);
            // debug
            for (Object object : arguments) {
                System.out.println(object);
            }
            Method m = c.getMethod(name, typeOfParameters.get(type));
            System.out.println("You got The method " + m.getName());
            System.out.println("Try to call the method");
            m.invoke(null, arguments);

            SCIFIOImgPlus<UnsignedByteType> processedOutput = new SCIFIOImgPlus<>(output);

            System.out.println(processedOutput);
            byte[] rawProcessedImage = ImageConverter.imageToJPEGBytes(processedOutput);

            System.out.println("No BYTES !!!!");
            return new Image(image.getName() + "_" + name, rawProcessedImage, image.getType(), image.getSize());
        }
        throw new NoSuchMethodException();
    }

    /**
     * Parse the parameters to apply them to the algorithm
     * 
     * @param args             Collection<String> Arguments that need to be parsed
     * @param typeOfParameters ArrayList<Class<?>[]> Types of each arguments for the
     *                         chosen method. It takes in consideration Overloads of
     *                         methods
     * @param arguments        Object[] Object representing each element after
     *                         parsing
     * @return The index corresponding to the class<?>[] choosen
     */
    private int parseParameters(Collection<String> args, ArrayList<Class<?>[]> typeOfParameters, Object[] arguments) {
        int index = 2;
        for (int i = 0; i < typeOfParameters.size(); i++) {
            if (args.size() == typeOfParameters.get(i).length - index) {
                Class<?>[] types = typeOfParameters.get(i);
                for (String s : args) {
                    arguments[index] = parseStringPrimitiveType(s, types[index]);
                    index++;
                }
                if (index == types.length)
                    return i;
            }
        }
        throw new IllegalArgumentException();
    }

}
