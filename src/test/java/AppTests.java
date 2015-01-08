import logic.classification.ClassificatorFactory;
import logic.classification.IClassificator;
import logic.classification.RocchioClassificator;
import model.Receipt;
import model.ReceiptClass;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.utils.FileUtils;
import org.junit.Test;
import util.DirectoryFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppTests {

    private static final double DELTA = 1e-10;
    private static final String TEST_DIR_PATH = "/Users/d_rc/Desktop/test/";
    private IClassificator classificator = ClassificatorFactory.getClassificator();

    //public void testOne(String filePath, double amount, String classIdentifier) {
    //    ReceiptData data = ReceiptData.loremIpsum();
    //    File file = new File(filePath);
    //    try {
    //        Receipt receipt = new Receipt(file);
    //        // find logo
    //        BufferedImage logo = receipt.separateLogo();
    //        // classify logo
    //        ReceiptClass receiptClass = classificator.classify(logo);
    //        System.out.println(receiptClass.getIdentifier());
    //        // ocr
    //        ICharacterRecognizer ocr = CharacterRecognizerFactory.dispatch(receiptClass);
    //        data = ocr.scan(receipt);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //        fail();
    //    }
    //    assertEquals(amount, data.getAmount(), DELTA);
    //    assertEquals(classIdentifier, data.getReceiptClass());
    //}

    @Test
    public void testAll() throws IOException {
        File trainDir = new File(TEST_DIR_PATH);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));
        for (String className : classes) {
            //System.out.println(className + ":");
            File classDir = new File(TEST_DIR_PATH + className);
            ArrayList<String> imgFileNames = FileUtils.getAllImages(classDir, true);
            for (String imgFileName : imgFileNames) {
                Receipt receipt = new Receipt(new File(imgFileName));
                BufferedImage logo = receipt.separateLogo();
                ReceiptClass receiptClass = classificator.classify(logo);
                //System.out.println("got " + receiptClass.getIdentifier() + " (expected " + className + ")");
            }
            System.out.println("");
        }
    }

    //@Test
    public void testAllSurf() throws IOException {
        classificator = ClassificatorFactory.getSurfSimpleClassificator();
        File trainDir = new File(TEST_DIR_PATH);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));
        for (String className : classes) {
            File classDir = new File(TEST_DIR_PATH + className);
            ArrayList<String> imgFileNames = FileUtils.getAllImages(classDir, true);
            for (String imgFileName : imgFileNames) {
                Receipt receipt = new Receipt(new File(imgFileName));
                BufferedImage logo = receipt.separateLogo();
                ReceiptClass receiptClass = classificator.classify(logo);
                System.out.println("got " + receiptClass.getIdentifier() + " (expected " + className + ")");
            }
            System.out.println("");
        }
    }

    ////@Test
    //public void testCvut() {
    //    ReceiptData data = ReceiptData.loremIpsum();
    //    File file = new File("/Users/d_rc/Desktop/test/Billa/IMAG1382.jpg");
    //    try {
    //        Receipt receipt = new Receipt(file);
    //        // find logo
    //        BufferedImage logo = receipt.separateLogo();
    //        // classify logo
    //        ReceiptClass receiptClass = classificator.classify(logo);
    //        System.out.println("-> " + receiptClass.getIdentifier());
    //    } catch (Exception e) {
//
    //    }
    //}
//
    //@Test
    public void testClassification() throws IOException {
        File trainDir = new File(TEST_DIR_PATH);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));
        for (String className : classes) {
            System.out.println("Correct for " + className + ":");
            File classDir = new File(TEST_DIR_PATH + className);
            ArrayList<String> imgFileNames = FileUtils.getAllImages(classDir, true);
            int j = 0;
            for (String imgFileName : imgFileNames) {
                Receipt receipt = new Receipt(new File(imgFileName));
                BufferedImage logo = receipt.separateLogo();
                for (Class<? extends LireFeature> featureExtractor: RocchioClassificator.getAvailableFeatureExtractors()) {
                    //System.out.println("-retraining with " + featureExtractor.getName());
                    RocchioClassificator myClassificator = (RocchioClassificator) ClassificatorFactory.getRocchioClassificator(featureExtractor);
                    ReceiptClass receiptClass = myClassificator.classify(logo);
                    //System.out.println(receiptClass.getIdentifier() + "(expected " + className + ")");
                    if (receiptClass.getIdentifier().equals(className)) {
                        System.out.println("-> " + featureExtractor.getSimpleName());
                    } else {
                        System.out.println("none");
                    }
                }
            }
            System.out.println("");
        }
    }

    //@Test
    public void testSegmentation() throws IOException {
        File trainDir = new File(TEST_DIR_PATH);
        List<String> classes = Arrays.asList(trainDir.list(new DirectoryFilter()));
        for (String className : classes) {
            File classDir = new File(TEST_DIR_PATH + className);
            ArrayList<String> imgFileNames = FileUtils.getAllImages(classDir, true);
            int j = 0;
            for (String imgFileName : imgFileNames) {
                Receipt receipt = new Receipt(new File(imgFileName));
                BufferedImage logo = receipt.separateLogo();
                ImageIO.write(logo, "png", new File("/Users/d_rc/Desktop/logos/"+ j++ +".jpg"));
            }
            System.out.println("");
        }
    }

}
