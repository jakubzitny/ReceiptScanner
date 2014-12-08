package logic.classification;

import com.stromberglabs.jopensurf.Surf;
import model.ReceiptClass;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 */
public class SurfSimpleIClassificator implements IClassificator {

    public static SurfSimpleIClassificator create () {
        return new SurfSimpleIClassificator();
    }

    public ReceiptClass classify(BufferedImage image) throws IOException {
        Surf imageSurf = new Surf(image);
        double bestMatchScore = Double.MIN_VALUE;
        ReceiptClass bestMatch = null;
        for (ReceiptClass refClass: ReceiptClass.getTrainingClasses()) {
            double matchingPointNo = imageSurf.getMatchingPoints(refClass.getSurf(), false).size();
            System.out.println("Found " + matchingPointNo + " matching pts with " + refClass.getIdentifier());
            if (matchingPointNo > bestMatchScore) {
                bestMatch = refClass;
                bestMatchScore = matchingPointNo;
            }
        }
        System.out.println("Matched: " + bestMatch.getIdentifier());
        return bestMatch;
    }

}
