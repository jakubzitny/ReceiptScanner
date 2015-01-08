package model;

/**
 * Created by d_rc on 07/01/15.
 */
public class ReceiptClassRocchio extends ReceiptClass {

    private double[] mCentroidVector;

    public ReceiptClassRocchio(double[] cv, String name) {
        super(name);
        mCentroidVector = cv;
    }

    public double[] getCentroidVector() {
        return mCentroidVector;
    }

    public void setCentroidVector(double[] mCentroidVector) {
        this.mCentroidVector = mCentroidVector;
    }

}
