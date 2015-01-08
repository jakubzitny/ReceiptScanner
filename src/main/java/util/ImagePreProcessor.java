package util;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

/**
 * Created by d_rc on 07/01/15.
 */
public class ImagePreProcessor {

    public static void main(String[] args) throws IOException {
        File imgFile = new File("/Users/d_rc/Desktop/preprocesstest.jpg");
        BufferedImage img = ImageIO.read(imgFile);
        ImageIO.write(desaturate(img), "png", new File("/Users/d_rc/Desktop/preprocessres.jpg"));
    }

    public static BufferedImage desaturate(BufferedImage source) {
        ColorConvertOp colorConvert =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(source, source);
        return source;
    }

    public static BufferedImage brighten(BufferedImage source) {
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        rescaleOp.filter(source, source);
        return source;
    }

}
