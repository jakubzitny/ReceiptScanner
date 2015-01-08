package logic.classification;

import com.stromberglabs.jopensurf.Surf;
import model.ReceiptClass;
import model.ReceiptClassSurf;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 */
public class SurfSimpleClassificator implements IClassificator {

    public static SurfSimpleClassificator create () {
        return new SurfSimpleClassificator();
    }

    public ReceiptClass classify(BufferedImage image) throws IOException {
        Surf imageSurf = new Surf(image);
        double bestMatchScore = Double.MIN_VALUE;
        ReceiptClass bestMatch = null;
        for (ReceiptClassSurf refClass: ReceiptClassSurf.getTrainingClasses()) {
            double matchingPointNo = imageSurf.getMatchingPoints(refClass.getSurf(), false).size();
            //System.out.println("Found " + matchingPointNo + " matching pts with " + refClass.getIdentifier());
            if (matchingPointNo > bestMatchScore) {
                bestMatch = refClass;
                bestMatchScore = matchingPointNo;
            }
            // fallback
            if (bestMatchScore < 20.0) bestMatch = new ReceiptClass("generic");
        }
        //System.out.println("Matched: " + bestMatch.getIdentifier());
        return bestMatch;
    }

}
