package pdl.backend;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.scif.FormatException;
import io.scif.Writer;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imagej.ImgPlus;
import net.imagej.ImgPlusMetadata;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import pdl.processing.ImageConverter;
import pdl.processing.Processing;

public class AlgorithmManager {
    
    private static AlgorithmManager _instance = null; 
    private Map<String, Class<?>> algorithms;

    private AlgorithmManager(){
        algorithms = new HashMap<>();
        for (Method method : Processing.class.getDeclaredMethods()) {
            System.out.println("----------- " + method.getName() +" -----------");
            algorithms.put(method.getName(), Processing.class);
        }
    }



    public static AlgorithmManager Instance(){
        if(_instance == null){
            _instance = new AlgorithmManager();
        }
        return _instance;
    }

    public Image applyAlgorithm(String name, Collection<String> args, Image image) throws Exception {
        SCIFIOImgPlus<UnsignedByteType> input = ImageConverter.imageFromJPEGBytes(image.getData());
        final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<UnsignedByteType>(new UnsignedByteType());
        ImgPlus<UnsignedByteType> output = SCIFIOImgPlus.wrap(factory.create(input), SCIFIOImgPlus.wrap(input));
        System.out.println("i = "+ input.getClass() +  " o = " + output.getClass());
        
        Class<?> c = algorithms.get(name);
        if(c == null){
            throw new NoSuchMethodException();
        }else{
            System.out.println("You go the class " + c.getName());
        }
        Method m = c.getMethod(name, input.getClass(), output.getClass());
        System.out.println("You got the method " + m.getName() + "  Now try to execute it");
        m.invoke(null, input, output);
        
        SCIFIOImgPlus<UnsignedByteType> processedOutput = new SCIFIOImgPlus<>(output);

        System.out.println(processedOutput);
        byte[] rawProcessedImage = ImageConverter.imageToJPEGBytes(processedOutput);
        
        System.out.println("No BYTES !!!!");
        return new Image(image.getName()+"_"+name, rawProcessedImage, image.getType(), image.getSize());
    }

    





}
