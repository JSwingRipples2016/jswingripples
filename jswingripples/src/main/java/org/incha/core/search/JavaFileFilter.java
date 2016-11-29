package org.incha.core.search;

/**
 * Only accepts java files (with the .java extension).
 * Created by fcocl_000 on 08-05-2016.
 */
public class JavaFileFilter extends AbstractExtensionFilter {

    @Override
    public String getFileExtension() {
        return ".java";
    }
}
