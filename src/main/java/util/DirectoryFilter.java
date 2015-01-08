package util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by d_rc on 07/01/15.
 */
public class DirectoryFilter implements FilenameFilter {
    @Override
    public boolean accept(File current, String name) {
        return new File(current, name).isDirectory();
    }
}
