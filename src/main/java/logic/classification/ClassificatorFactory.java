package logic.classification;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

/**
 * Created by d_rc on 09/12/14.
 */
public class ClassificatorFactory {

    public static IClassificator getClassificator() {
        return new RocchioClassificator();
    }

    public static IClassificator getSurfSimpleClassificator () {
        return SurfSimpleClassificator.create();
    }

    public static IClassificator getRocchioClassificator (Class<? extends LireFeature> featureExtractor) {
        return new RocchioClassificator(featureExtractor);
    }

}