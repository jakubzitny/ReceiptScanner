package logic.classification;

import model.ReceiptClass;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 */
public interface IClassificator {

    public ReceiptClass classify(BufferedImage logo) throws IOException;

}
