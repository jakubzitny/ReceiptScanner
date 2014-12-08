package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d_rc on 08/12/14.
 */
public class ReceiptData {

    private String mClass;
    private double mTotal;
    private String mRawText;
    private List<ReceiptItem> mItems = new ArrayList<ReceiptItem>();

    public String getReceiptClass() {
        return mClass;
    }

    public void setReceiptClass(String receiptClass) {
        mClass = receiptClass;
    }

    public double getAmount() {
        return mTotal;
    }

    public void setAmount(double total) {
        mTotal = total;
    }

    public List<ReceiptItem> getItems() {
        return mItems;
    }

    public String getRawText() {
        return mRawText;
    }

    public void setRawText(String rawText) {
        mRawText = rawText;
    }

    public static ReceiptData loremIpsum() {
        ReceiptData receiptData = new ReceiptData();
        receiptData.setAmount(125);
        receiptData.setReceiptClass("generic");
        return receiptData;
    }
}
