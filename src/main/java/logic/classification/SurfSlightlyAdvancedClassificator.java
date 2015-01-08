package logic.classification;

import com.stromberglabs.jopensurf.Surf;
import model.ReceiptClass;
import model.ReceiptClassSurf;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 * slow, use db!
 */
public class SurfSlightlyAdvancedClassificator implements IClassificator {

    public static SurfSlightlyAdvancedClassificator create () {
        return new SurfSlightlyAdvancedClassificator();
    }

    public ReceiptClass classify(BufferedImage image) throws IOException {
        Surf imageSurf = new Surf(image);
        double bestMatchScore = Double.MIN_VALUE;
        ReceiptClass bestMatch = null;
        for (ReceiptClassSurf refClass: ReceiptClassSurf.getTrainingClassPrototypes()) {
            double matchingPointNo = imageSurf.getMatchingPoints(refClass.getSurf(), false).size();
            System.out.println("Found " + matchingPointNo + " matching pts with " + refClass.getIdentifier());
            if (matchingPointNo > bestMatchScore) {
                bestMatch = refClass;
                bestMatchScore = matchingPointNo;
            }
        }
        teachClass(bestMatch.getIdentifier(), image);
        System.out.println("Matched: " + bestMatch.getIdentifier());
        return bestMatch;
    }

    private void teachClass(String className, BufferedImage image) throws IOException {
        File file = File.createTempFile(className, ReceiptClass.TRAINIMGFORMAT, new File(ReceiptClass.TRAIN_DIR_PATH + className));
        String fileName = file.getAbsolutePath();
        file.delete();
        boolean res = ImageIO.write(image, ReceiptClass.TRAINIMGFORMAT, new File("/Users/d_rc/Desktop/asd.jpg"));
        System.out.println(res);

    }

}
