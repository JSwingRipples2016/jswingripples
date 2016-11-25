package org.incha.core.search;

import java.io.File;
import java.io.FileFilter;

/**
 * Abstract class for filtering files based on file extension.
 * Created by fcocl_000 on 08-05-2016.
 */
abstract class AbstractExtensionFilter implements FileFilter {

    /**
     * Returns the filter's accepted file extension.
     * @return the accepted file extension.
     */
    protected abstract String getFileExtension();

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(getFileExtension());
    }
}
