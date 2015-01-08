package model;

/**
 * Created by d_rc on 08/12/14.
 * TODO: abstract and subs for different classifiers
 */
public class ReceiptClass {

    protected static final String TRAINDIR = "/Users/d_rc/Desktop/train/";
    public static final String TRAIN_DIR_PATH = "/Users/d_rc/Desktop/train2/";
    public static final String TRAINIMGFORMAT = ".jpg";

    protected String mIdentifier;

    public ReceiptClass(String className) {
        mIdentifier = className;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

}
