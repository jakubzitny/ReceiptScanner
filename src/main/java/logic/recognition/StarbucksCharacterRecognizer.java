package logic.recognition;

import model.Receipt;
import model.ReceiptData;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by d_rc on 08/12/14.
 */
public class StarbucksCharacterRecognizer extends GenericCharacterRecognizer implements ICharacterRecognizer {

    public static final String CLASSIDENTIFIER = "starbucks";

    @Override
    public ReceiptData scan(Receipt receipt) throws IOException {
        String rawText = scanText(receipt.getOriginalImage());
        Pattern pattern = Pattern.compile("K p1atbe (.*?)\n");
        Matcher matcher = pattern.matcher(rawText);
        double price = 0;
        if (matcher.find()) {
            price = Double.parseDouble(matcher.group(1));
            System.out.println(matcher.group(1));
        }
        ReceiptData data = ReceiptData.loremIpsum();
        data.setAmount(price);
        data.setReceiptClass(CLASSIDENTIFIER);
        data.setRawText(rawText);
        return data;
    }
}
