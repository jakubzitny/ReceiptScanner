package logic.recognition;

import model.Receipt;
import model.ReceiptClass;
import model.ReceiptData;
import util.TesseractWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 */
public class GenericCharacterRecognizer implements ICharacterRecognizer {

    public static final String CLASSIDENTIFIER = "generic";

    /**
     * factory method (for subclasses also)
     * redir to factory object
     * @param receiptClass the classificated class of a recognizing receipt
     * @return ICharacterRecognizer instance of proper class
     */
    public static ICharacterRecognizer create (ReceiptClass receiptClass) {
        return CharacterRecognizerFactory.dispatch(receiptClass);
    }

    public String scanText(BufferedImage image) throws IOException {
        // save image to tmp
        File tmpImgFile = File.createTempFile("tmpImg", ".jpg");
        ImageIO.write(image, "jpg", tmpImgFile);
        String rawData = TesseractWrapper.runTesseract(tmpImgFile.getAbsolutePath());
        tmpImgFile.delete();
        return rawData;
    }

    @Override
    public ReceiptData scan(Receipt receipt) throws IOException {
        String rawText = scanText(receipt.getOriginalImage());
        ReceiptData data = ReceiptData.loremIpsum();
        data.setReceiptClass(CLASSIDENTIFIER);
        data.setRawText(rawText);
        return data;
    }

}
