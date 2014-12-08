package logic.recognition;

import model.Receipt;
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

    public String scanText(BufferedImage image) throws IOException {
        // save image to tmp
        File tmpImgFile =  File.createTempFile("tmpImg", ".jpg");
        ImageIO.write(image, "jpg", tmpImgFile);
        return TesseractWrapper.runTesseract(tmpImgFile.getAbsolutePath());
    }

    @Override
    public ReceiptData scan(Receipt receipt) throws IOException {
        ReceiptData data = ReceiptData.loremIpsum();
        data.setReceiptClass(CLASSIDENTIFIER);
        return data;
    }

}
