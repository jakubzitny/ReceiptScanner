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
 * Created by d_rc on 07/01/15.
 */
public class ReceiptClassSurf extends ReceiptClass {

    private Surf mReferenceSurf;

    public ReceiptClassSurf(String className, Surf surf) {
        super(className);
        mReferenceSurf = surf;
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

    public static List<ReceiptClassSurf> getTrainingClasses() throws IOException {
        List<ReceiptClassSurf> trainingClasses = new ArrayList<ReceiptClassSurf>();
        for (String className: getTrainingClassNames()) {
            File file = new File(TRAINDIR + className + TRAINIMGFORMAT);
            BufferedImage image = ImageIO.read(file);
            trainingClasses.add(new ReceiptClassSurf(className, new Surf(image)));
        }
        return trainingClasses;
    }

    public static List<ReceiptClassSurf> getTrainingClassPrototypes() throws IOException {
        List<ReceiptClassSurf> trainingClasses = new ArrayList<ReceiptClassSurf>();
        for (String className: getTrainingClassNames()) {
            File dir = new File(TRAIN_DIR_PATH + className);
            List<Surf> classSurfs = new ArrayList<Surf>();
            for (File file: dir.listFiles()) {
                if (file.getName().equals(".DS_Store")) continue;
                BufferedImage image = ImageIO.read(file);
                classSurfs.add(new Surf(image));
            }
            // TODO: averigize classSurfs
            Surf refSurf = classSurfs.get(0);
            trainingClasses.add(new ReceiptClassSurf(className, refSurf));
        }

        return trainingClasses;
    }

}
