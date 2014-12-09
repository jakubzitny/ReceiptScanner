package logic.recognition;

import model.Receipt;
import model.ReceiptData;

import java.io.IOException;

/**
 * Created by d_rc on 06/12/14.
 */
public class BillaCharacterRecognizer extends GenericCharacterRecognizer {

    public static final String CLASSIDENTIFIER = "Billa";

    @Override
    public ReceiptData scan(Receipt receipt) throws IOException {
        String rawText = scanText(receipt.getOriginalImage());
        // TODO: process raw billa
        ReceiptData data = ReceiptData.loremIpsum();
        data.setReceiptClass(CLASSIDENTIFIER);
        data.setRawText(rawText);
        return data;
    }
}
