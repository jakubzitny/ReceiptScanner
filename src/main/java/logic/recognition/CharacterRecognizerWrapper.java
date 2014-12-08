package logic.recognition;

import model.ReceiptClass;

/**
 * Created by d_rc on 08/12/14.
 * TODO: bad design, use abstract class + think :)
 */
public class CharacterRecognizerWrapper {

    public static ICharacterRecognizer dispatch (ReceiptClass receiptClass) {
        if (receiptClass.getIdentifier().equals("kaufland")) {
            return new KauflandCharacterRecognizer();
        } else if (receiptClass.getIdentifier().equals("starbucks")) {
            return new StarbucksCharacterRecognizer();
        } else if (receiptClass.getIdentifier().equals("mf")) {
            return new MFCharacterRecognizer();
        } else if (receiptClass.getIdentifier().equals("billa")) {
            return new BillaCharacterRecognizer();
        } else {
            return new GenericCharacterRecognizer();
        }
    }

}
