package logic.recognition;

import model.ReceiptClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by d_rc on 08/12/14.
 */
public class CharacterRecognizerFactory {

    private static final Map<String, Class> RECOGNIZER_MAP;
    static {
        RECOGNIZER_MAP = new HashMap<String, Class>();
        RECOGNIZER_MAP.put(GenericCharacterRecognizer.CLASSIDENTIFIER, GenericCharacterRecognizer.class);
        RECOGNIZER_MAP.put(KauflandCharacterRecognizer.CLASSIDENTIFIER, KauflandCharacterRecognizer.class);
        RECOGNIZER_MAP.put(StarbucksCharacterRecognizer.CLASSIDENTIFIER, StarbucksCharacterRecognizer.class);
        RECOGNIZER_MAP.put(MFCharacterRecognizer.CLASSIDENTIFIER, MFCharacterRecognizer.class);
        RECOGNIZER_MAP.put(BillaCharacterRecognizer.CLASSIDENTIFIER, BillaCharacterRecognizer.class);
    }

    public static ICharacterRecognizer dispatch (ReceiptClass receiptClass) {
        String subClassName = RECOGNIZER_MAP.get(receiptClass.getIdentifier()).getName();
        ICharacterRecognizer characterRecognizer = new GenericCharacterRecognizer();
        try {
            characterRecognizer = (ICharacterRecognizer) Class.forName(subClassName).newInstance();
        } catch (ClassNotFoundException e) {
            // ignore, stay generic
        } catch (IllegalAccessException e) {
            // ignore, stay generic
        } catch (InstantiationException e) {
            // ignore, stay generic
        }
        return characterRecognizer;
    }

    public static ICharacterRecognizer dispatchDummy(ReceiptClass receiptClass) {
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
