package logic.recognition;

import model.Receipt;
import model.ReceiptData;

import java.io.IOException;

/**
 * Created by d_rc on 08/12/14.
 */
public interface ICharacterRecognizer {

    //public ICharacterRecognizer create (ReceiptClass receiptClass);
    public ReceiptData scan (Receipt receipt) throws IOException;

}
