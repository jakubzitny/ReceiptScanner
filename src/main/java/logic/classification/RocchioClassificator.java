package logic.classification;

import model.ReceiptClass;
import model.ReceiptClassRocchio;
import net.semanticmetadata.lire.imageanalysis.*;
import net.semanticmetadata.lire.imageanalysis.joint.JointHistogram;
import net.semanticmetadata.lire.utils.FileUtils;
import util.DirectoryFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by d_rc on 07/01/15.
 */
public class RocchioClassificator implements IClassificator {

    public static final String TRAIN_DIR_PATH = "/Users/d_rc/Desktop/train2/";
    private static final Class<? extends LireFeature> DEFAULT_FEATURE_EXTRACTOR = EdgeHistogram.class; //-> good for cvut

    private Class<? extends LireFeature> mFeatureExtractor;
    private static List<ReceiptClassRocchio> mTrainSet;

    public RocchioClassificator () {
        mFeatureExtractor = DEFAULT_FEATURE_EXTRACTOR;
        mTrainSet = train();
    }

    public RocchioClassificator (Class<? extends LireFeature> featureExtractor) {
        mFeatureExtractor = featureExtractor;
        mTrainSet.clear();
        mTrainSet = train(true);
    }

    @Override
    public ReceiptClass classify(BufferedImage logo) throws IOException {
        LireFeature lireFeature = null;
        try {
            lireFeature = mFeatureExtractor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        lireFeature.extract(logo);
        double[] featureVector = lireFeature.getDoubleHistogram();

        ReceiptClassRocchio closestClass = null;
        double closestSimilarity = Double.MIN_VALUE;
        for (ReceiptClassRocchio receiptClassRocchio : mTrainSet) {
            double cs = cosineSimilarity(receiptClassRocchio.getCentroidVector(), featureVector);
            //System.out.println(cs + " (cs with " + receiptClassRocchio.getIdentifier() + ")");
            if (cs > closestSimilarity) {
                closestSimilarity = cs;
                closestClass = receiptClassRocchio;
            }
        }
        //System.out.println(closestClass.getIdentifier() + ": " + lireFeature.getStringRepresentation());
        return closestClass;
    }

    private double cosineSimilarity(double[] centroidVector, double[] featureVector) {
        int i = 0;
        double dotProduct = .0;
        double sqSumCentroid = .0;
        double sqSumFeature = .0;
        for (double item: centroidVector) {
            dotProduct += centroidVector[i] * featureVector[i];
            sqSumCentroid += centroidVector[i] * centroidVector[i];
            sqSumFeature += featureVector[i] * featureVector[i];
            i++;
        }
        double result = dotProduct / (Math.sqrt(sqSumCentroid) * Math.sqrt(sqSumFeature));
        return result;
    }

    public static List<Class<? extends LireFeature>> getAvailableFeatureExtractors() {
        List <Class<? extends LireFeature>> featureExtractors = new ArrayList<Class<? extends LireFeature>>();
        featureExtractors.add(ColorLayout.class);
        featureExtractors.add(EdgeHistogram.class);
        //featureExtractors.add(ScalableColor.class);
        featureExtractors.add(CEDD.class);
        featureExtractors.add(FCTH.class);
        //featureExtractors.add(JpegCoefficientHistogram.class);
        featureExtractors.add(SimpleColorHistogram.class);
        featureExtractors.add(Tamura.class);
        featureExtractors.add(Gabor.class);
        featureExtractors.add(JointHistogram.class);
        featureExtractors.add(OpponentHistogram.class);
        return featureExtractors;
    }

    public List<ReceiptClassRocchio> train() {
        return train(false);
    }

    public List<ReceiptClassRocchio> train(boolean override) {
        //System.out.println("training classificator");
        if (!override && mTrainSet != null) return mTrainSet;
        File trainDir = new File(TRAIN_DIR_PATH);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));
        List<ReceiptClassRocchio> receiptClassRocchios = new ArrayList<ReceiptClassRocchio>();
        try {
            for (String className : classes) {
                //System.out.println(className);
                receiptClassRocchios.add(trainClass(className));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTrainSet = receiptClassRocchios;
        return receiptClassRocchios;
    }

    public ReceiptClassRocchio trainClass(String className)
            throws IllegalAccessException, InstantiationException, IOException {
        LireFeature lireFeature = mFeatureExtractor.newInstance();
        File classDir = new File(TRAIN_DIR_PATH + className);
        ArrayList<String> imgFileNames = FileUtils.getAllImages(classDir, true);
        double[] centroidVector = null;
        for (String imgFileName : imgFileNames) {
            BufferedImage img = ImageIO.read(new File(imgFileName));
            lireFeature.extract(img);
            double[] featureVector = lireFeature.getDoubleHistogram();
            if (centroidVector == null) {
                centroidVector = featureVector;
            } else {
                int i = 0;
                for (double item : centroidVector) {
                    centroidVector[i] = item + featureVector[i];
                    i++;
                }
            }
            //double[] dh = lireFeature.getDoubleHistogram();
            //byte[] byteRep = lireFeature.getByteArrayRepresentation(); // <- feature in bytes
            //String featureString = lireFeature.getStringRepresentation();
            //System.out.println("done " + imgFileName + ": " + featureString);
        }

        // fix the centroid Vector, divide by nnumber of training docs
        int i = 0;
        //System.out.print("centroid: ");
        for (double item: centroidVector) {
            centroidVector[i] /= imgFileNames.size();
            //System.out.print(centroidVector[i] + " ");
            i++;
        }
        // TODO: double[] -> to byte[] later
        // vector to LF for simplified distance calc
        //LireFeature centroidLireFeature = new ColorLayout();
        //centroidLireFeature.setByteArrayRepresentation(centroidVector);
        return new ReceiptClassRocchio(centroidVector, className);
    }

}
