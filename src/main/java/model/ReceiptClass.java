package model;

import com.stromberglabs.jopensurf.Surf;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d_rc on 08/12/14.
 */
public class ReceiptClass {

    private static final String TRAINDIR = "/Users/d_rc/Desktop/train/";
    private static final String TRAINIMGFORMAT = ".jpg";

    private String mIdentifier;
    private Surf mReferenceSurf;

    public ReceiptClass(String className, Surf surf) {
        mIdentifier = className;
        mReferenceSurf = surf;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public Surf getSurf() {
        return mReferenceSurf;
    }

    public Surf getReferenceSurf() {
        return mReferenceSurf;
    }

    public void setReferenceSurf(Surf referenceSurf) {
        mReferenceSurf = referenceSurf;
    }

    public static List<String> getTrainingClassNames() {
        List<String> classNames = new ArrayList<String>();
        classNames.add("kaufland");
        classNames.add("billa");
        classNames.add("starbucks");
        classNames.add("mf");
        return classNames;
    }

    public static List<ReceiptClass> getTrainingClasses() throws IOException {
        List<ReceiptClass> trainingClasses = new ArrayList<ReceiptClass>();
        for (String className: getTrainingClassNames()) {
            String fileName = TRAINDIR + className + TRAINIMGFORMAT;
            BufferedImage image = ImageIO.read(new File(fileName));
            trainingClasses.add(new ReceiptClass(className, new Surf(image)));
        }
        return trainingClasses;
    }
}
