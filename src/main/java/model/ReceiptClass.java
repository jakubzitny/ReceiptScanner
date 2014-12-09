package model;

import com.stromberglabs.jopensurf.Surf;
import logic.recognition.BillaCharacterRecognizer;
import logic.recognition.KauflandCharacterRecognizer;
import logic.recognition.MFCharacterRecognizer;
import logic.recognition.StarbucksCharacterRecognizer;

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
    public static final String TRAINDIR2 = "/Users/d_rc/Desktop/train2/";
    public static final String TRAINIMGFORMAT = ".jpg";

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
        classNames.add(KauflandCharacterRecognizer.CLASSIDENTIFIER);
        classNames.add(BillaCharacterRecognizer.CLASSIDENTIFIER);
        classNames.add(MFCharacterRecognizer.CLASSIDENTIFIER);
        classNames.add(StarbucksCharacterRecognizer.CLASSIDENTIFIER);
        return classNames;
    }

    public static List<ReceiptClass> getTrainingClasses() throws IOException {
        List<ReceiptClass> trainingClasses = new ArrayList<ReceiptClass>();
        for (String className: getTrainingClassNames()) {
            File file = new File(TRAINDIR + className + TRAINIMGFORMAT);
            BufferedImage image = ImageIO.read(file);
            trainingClasses.add(new ReceiptClass(className, new Surf(image)));
        }
        return trainingClasses;
    }

    public static List<ReceiptClass> getTrainingClassPrototypes() throws IOException {
        List<ReceiptClass> trainingClasses = new ArrayList<ReceiptClass>();
        for (String className: getTrainingClassNames()) {
            File dir = new File(TRAINDIR2 + className);
            List<Surf> classSurfs = new ArrayList<Surf>();
            for (File file: dir.listFiles()) {
                if (file.getName().equals(".DS_Store")) continue;
                BufferedImage image = ImageIO.read(file);
                classSurfs.add(new Surf(image));
            }
            // TODO: averagize classSurfs
            Surf refSurf = classSurfs.get(0);
            trainingClasses.add(new ReceiptClass(className, refSurf));
        }

        return trainingClasses;
    }
}
