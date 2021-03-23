package pdl.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.scif.img.SCIFIOImgPlus;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import pdl.processing.ImageConverter;
import pdl.processing.Processing;

/**
 * This class is Implemented with the design Pattern Singleton This class is
 * Managing the execution of algorithms processing images.
 */
public class AlgorithmManager {

    private static AlgorithmManager _instance = null; // single instance of the class
    final private Map<String, Map<Class<?>[], Method>> algorithms; // map of all algorithms

    private Class<?>[] Classes = { Processing.class, pdl.processing.Convolution.class }; // classes to exctract
                                                                                         // Algorithms from

    /**
     * To keep this class as a singleton the class as only one private constructor
     * to assure that no one can instantiate this class and only one instance of
     * this class then can exists.
     */
    private AlgorithmManager() {
        algorithms = new HashMap<>();

        for (Class<?> c : Classes) {
            for (Method m : c.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers())) {
                    Map<Class<?>[], Method> methods = algorithms.get(m.getName());
                    if (methods == null) {
                        methods = new HashMap<>();
                        methods.put(m.getParameterTypes(), m);
                        algorithms.put(m.getName(), methods);
                    } else {
                        methods.put(m.getParameterTypes(), m);
                    }
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
     * Return list of names of algorithms
     * 
     * @return Set<String>
     */
    public Set<String> listAlgorithms() {
        return algorithms.keySet();
    }

    /**
     * Return the list of each type for each parameters of an algorithm
     * 
     * @param name String name of the algorithm
     * @return ArrayList<Class<?>[]> if no algorithm correspond to the given name
     *         return null
     */
    public Set<Class<?>[]> listOfParameterType(String name) {
        return algorithms.get(name).keySet();
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

    /**
     * Parse a string into a long
     * 
     * @param s String to parse
     * @return long parsed form the string
     * @throws NumberFormatException if Error while parsin
     */
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

    /**
     * Parse a String into a one of the following primitive type long, int, double,
     * float
     * 
     * @param s String to parse
     * @param t Target class
     * @return Object
     * @throws NumberFormatException If error while parsing
     */
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
    public Image applyAlgorithm(String name, Collection<String> args, Image image) throws Exception,
            NumberFormatException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        Map<Class<?>[], Method> methods = algorithms.get(name);
        if (methods != null) {
            // Convert the Image as a SCIFIOImgPlus<>
            SCIFIOImgPlus<UnsignedByteType> input = ImageConverter.imageFromJPEGBytes(image.getData());
            SCIFIOImgPlus<UnsignedByteType> output = input.copy(); // create a copy of the input

            Object[] arguments = new Object[args.size() + 2]; // list of arguments to apply to the method
            arguments[0] = input; // load input as first argument
            arguments[1] = output; // load output as second argument

            Class<?>[] parametersType = parseParameters(name, args, methods.keySet(), arguments, 2);
            Method m = methods.get(parametersType);

            m.invoke(null, arguments); // call the method

            byte[] rawProcessedImage = ImageConverter.imageToJPEGBytes(output); // get the bytes of the processedImage
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
    private Class<?>[] parseParameters(String name, Collection<String> args, Set<Class<?>[]> parameterTypes,
            Object[] arguments, int start) throws IllegalArgumentException, NumberFormatException {
        Iterator<Class<?>[]> iterator = parameterTypes.iterator();
        Class<?>[] types;
        while (iterator.hasNext()) {
            types = iterator.next();
            if (types.length - start == args.size()) {
                int index = start;
                for (String string : args) {
                    try {
                        arguments[index] = parseStringPrimitiveType(string, types[index]);
                        index++;
                    } catch (NumberFormatException e) {
                        if (!iterator.hasNext()) {
                            throw e;
                        } else {
                            break;
                        }
                    }
                }
                if (index == types.length) {
                    return types;
                }
            }
        }
        throw new IllegalArgumentException();
    }

}
