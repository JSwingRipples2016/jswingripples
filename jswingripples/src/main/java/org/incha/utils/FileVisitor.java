package org.incha.utils;

import java.io.File;
import java.io.IOException;


public interface FileVisitor extends Visitor<File> {
    /**
     * @see com.au.odessaum.util.Visitor#enter(java.lang.Object)
     */
    @Override
    public boolean enter(File t) throws IOException;
    /**
     * @see com.au.odessaum.util.Visitor#exit(java.lang.Object)
     */
    @Override
    public void exit(File t) throws IOException;
}
