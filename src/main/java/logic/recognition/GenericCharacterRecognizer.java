package logic.recognition;

import model.Receipt;
import model.ReceiptData;

/**
 * Created by d_rc on 08/12/14.
 */
public class GenericCharacterRecognizer implements ICharacterRecognizer {

    public static final String CLASSIDENTIFIER = "generic";

    @Override
    public ReceiptData scan(Receipt receipt) {
        ReceiptData data = ReceiptData.loremIpsum();
        data.setReceiptClass(CLASSIDENTIFIER);
        return data;
    }

}
