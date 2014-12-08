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
        } else {
            return new GenericCharacterRecognizer();
        }
    }

}
