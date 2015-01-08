package util;

import model.ReceiptClassRocchio;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by d_rc on 06/01/15.
 * inspiration at http://bit.ly/1KjBSzD
 */
public class TrainClassifier {

    private static final String trainDirPath = "/Users/d_rc/Desktop/train2/";
    private static final String testOnePath = "/Users/d_rc/Desktop/startest_po.jpg";
    //private static final String testOnePath = "/Users/d_rc/Desktop/mfutest2.jpg";

    //private static final Class<? extends LireFeature> defaultFeatureExtractor = ColorLayout.class;
    private static final Class<? extends LireFeature> defaultFeatureExtractor = EdgeHistogram.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = ScalableColor.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = CEDD.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = FCTH.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = JpegCoefficientHistogram.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = SimpleColorHistogram.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = Tamura.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = Gabor.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = JointHistogram.class;
    //private static final Class<? extends LireFeature> defaultFeatureExtractor = OpponentHistogram.class;

    private static class DirectoryFilter implements FilenameFilter {
        @Override
        public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
        }
    }

    public static void index (List<String> classes) throws IOException {
        // Creating a CEDD document builder and indexing all files.
        //DocumentBuilder builder = DocumentBuilderFactory.getCEDDDocumentBuilder();
        ChainedDocumentBuilder builder = new ChainedDocumentBuilder();
        builder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
        builder.addBuilder(DocumentBuilderFactory.getEdgeHistogramBuilder());
        builder.addBuilder(DocumentBuilderFactory.getOpponentHistogramDocumentBuilder());

        // Creating an Lucene IndexWriter
        IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION,
                new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
        IndexWriter iw = new IndexWriter(FSDirectory.open(new File("index")), conf);

        for (String className: classes) {
            System.out.println("training " + className);
            File classDir = new File(trainDirPath + className);
            ArrayList<String> images = FileUtils.getAllImages(classDir, true);

            // Iterating through images building the low level features
            for (String imageFilePath: images) {
                String imageFileName = imageFilePath.substring(0, imageFilePath.indexOf(".jpg")).substring(29);
                try {
                    BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                    Document document = builder.createDocument(img, imageFileName);
                    document.add(new StringField("class", className, Field.Store.YES));
                    iw.addDocument(document);
                } catch (Exception e) {
                    System.err.println("Error reading image or indexing it.");
                    e.printStackTrace();
                }
            }
        }
        iw.close();
    }

    public static void classify(List<String> classes) throws IOException {
        IndexReader ir = DirectoryReader.open(FSDirectory.open(new File("index")));

        for (int i = 0; i < ir.maxDoc(); i++) {
            org.apache.lucene.document.Document doc = ir.document(i);
            Terms terms = ir.getTermVector(i, "featureCEDD");
            Terms terms2 = ir.getTermVector(i, "descriptorEdgeHistogram");
            Terms terms3 = ir.getTermVector(i, "featOpHist");
            Terms terms4 = ir.getTermVector(i, "class");
            System.out.println(doc.get("class"));
//            TermsEnum termsEnum = terms.iterator(null);
//            BytesRef text;
//            TermVector tfIdf = TermVector.create();
//            while((text = termsEnum.next()) != null) {
//                String termString = text.utf8ToString();
//                Term termInstance  = new Term(DEFAULT_SEARCH_FIELD, termString);
//                long tf = termsEnum.totalTermFreq(); // <---- for this doc
//                long df = mReader.docFreq(termInstance); // <---- total doc freq
//                double tfIdfForTerm = tf * (mReader.getDocCount(DEFAULT_SEARCH_FIELD)/df);
//                tfIdf.addMember(tfIdfForTerm, termInstance);
//            }
//            mDocuments.get(Integer.parseInt(doc.get("newId"))).setTermVector(tfIdf);
        }


        System.exit(42);

//        ImageSearcher searcher = ImageSearcherFactory.createCEDDImageSearcher(10);
//        //ImageSearcher searcher = ImageSearcherFactory.createDefaultSearcher();
//
//        ImageSearchHits hits = searcher.search(img, ir);
//        for (int i = 0; i < hits.length(); i++) {
//            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
//            System.out.println(hits.score(i) + ": \t" + fileName);
//        }
    }

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

        File trainDir = new File(trainDirPath);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));

        //index(classes);
        //classify(classes);
        List<ReceiptClassRocchio> receiptClassRocchios = calcFeatures(classes);

        testOne(testOnePath, receiptClassRocchios);


    }

    private static void testOne(String testImgPath, List<ReceiptClassRocchio> receiptClassRocchios) throws IOException, IllegalAccessException, InstantiationException {
        BufferedImage testImg = ImageIO.read(new File(testImgPath));
        Class<? extends LireFeature> featureExtractor = defaultFeatureExtractor;
        LireFeature lireFeature = featureExtractor.newInstance();
        lireFeature.extract(testImg);
        double[] featureVector = lireFeature.getDoubleHistogram();
        System.out.println("thisc: " + lireFeature.getStringRepresentation());

        ReceiptClassRocchio closestReceiptClassRocchio = null;
        double closestSimilarity = Double.MIN_VALUE;
        for (ReceiptClassRocchio receiptClassRocchio : receiptClassRocchios) {
            double cs = calcCosineSimilarity(receiptClassRocchio.getCentroidVector(), featureVector);
            System.out.println(cs + " (cs with " + receiptClassRocchio.getIdentifier() + ")");
            if (cs > closestSimilarity) {
                closestSimilarity = cs;
                closestReceiptClassRocchio = receiptClassRocchio;
            }
        }
        System.out.println("Closest is " + closestReceiptClassRocchio.getIdentifier());
    }

    private static double calcCosineSimilarity(double[] centroidVector, double[] featureVector) {
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

    private static List<ReceiptClassRocchio> calcFeatures(List<String> classes) throws IllegalAccessException, InstantiationException, IOException {
        // choose the features to extract
        LinkedList<Class<? extends LireFeature>> featureExtractors = new LinkedList<Class<? extends LireFeature>>();
        featureExtractors.add(defaultFeatureExtractor);
        //featureExtractors.add(ColorLayout.class);
        //featureExtractors.add(EdgeHistogram.class);
        //featureExtractors.add(ScalableColor.class);
        //featureExtractors.add(CEDD.class);
        //featureExtractors.add(FCTH.class);
        ////featureExtractors.add(JCD.class);
        //featureExtractors.add(JpegCoefficientHistogram.class);
        //featureExtractors.add(SimpleColorHistogram.class);
        //featureExtractors.add(Tamura.class);
        //featureExtractors.add(Gabor.class);
        //featureExtractors.add(JointHistogram.class);
        //featureExtractors.add(OpponentHistogram.class);
        ////featureExtractors.add(PHOG.class);
        ////featureExtractors.add(LuminanceLayout.class);

        for (Class<? extends LireFeature> featureExtractor: featureExtractors) {
            List<ReceiptClassRocchio> receiptClassRocchios = new ArrayList<ReceiptClassRocchio>();
            LireFeature lireFeature = featureExtractor.newInstance();
            System.out.println();
            System.out.println(lireFeature.getFeatureName());
            for (String className : classes) {
                System.out.println();
                System.out.println(className + ": ");

                File classDir = new File(trainDirPath + className);
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

                   String featureString = lireFeature.getStringRepresentation();
                   //double[] dh = lireFeature.getDoubleHistogram();
                   //byte[] byteRep = lireFeature.getByteArrayRepresentation(); // <- feature in bytes
                   System.out.println("done " + imgFileName + ": " + featureString);
                }

                // fix the centroid Vector, divide by nnumber of training docs
                int i = 0;
                System.out.print("centroid: ");
                for (double item: centroidVector) {
                    centroidVector[i] /= imgFileNames.size();
                    System.out.print(centroidVector[i] + " ");
                    i++;
                }
                // vector to LF for simplified distance calc
                // TODO: double[] -> to byte[] later
                //LireFeature centroidLireFeature = new ColorLayout();
                //centroidLireFeature.setByteArrayRepresentation(centroidVector);
                System.out.println();
                receiptClassRocchios.add(new ReceiptClassRocchio(centroidVector, className));
            }
            return receiptClassRocchios;
        }
        return null;
    }

}
