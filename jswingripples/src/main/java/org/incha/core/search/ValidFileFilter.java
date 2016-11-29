package org.incha.core.search;

import java.io.File;
import java.io.FileFilter;

/**
 * Accepts files if they can be handled.
 * Created by fcocl_000 on 08-05-2016.
 */
public class ValidFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return !pathname.isHidden() && pathname.exists() && pathname.canRead();
    }
}
