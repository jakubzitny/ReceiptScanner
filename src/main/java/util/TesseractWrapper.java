package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by d_rc on 08/12/14.
 */
public class TesseractWrapper {

    private static final String CMD = "tesseract.sh";

    public static String runTesseract(String imgLoc) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(CMD + " " + imgLoc);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}
