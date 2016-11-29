package org.incha.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipVisitor implements FileVisitor {
    /**
     * The zip entry stack.
     */
    private Stack<ZipEntry> pathStack = new Stack<ZipEntry>();
    /**
     * The ZIP output stream.
     */
    protected final ZipOutputStream zout;

    /**
     * The constructor.
     * @param zout the ZIP output stream.
     */
    public ZipVisitor(final ZipOutputStream zout) {
        super();
        //this entry is pushed and never will user. It is only
        //for not make more difficult of recursion process.
        pathStack.push(new ZipEntry(""));
        this.zout = zout;
    }

    /**
     * @see com.au.odessaum.util.Visitor#enter(java.lang.Object)
     */
    @Override
    public boolean enter(final File file) throws IOException {
        final ZipEntry path = pathStack.peek();
        final boolean isFolder = file.isDirectory();
        final String entryName = path.getName() + file.getName() + (isFolder ? "/" : "");

        //create entry.
        final ZipEntry entry = new ZipEntry(entryName);
        entry.setTime(file.lastModified());
        //set file size.
        entry.setSize(isFolder ? 0L : file.length());

        //put next entry.
        zout.putNextEntry(entry);
        pathStack.push(entry);

        return true;
    }

    /**
     * @see com.au.odessaum.util.Visitor#exit(java.lang.Object)
     */
    @Override
    public void exit(final File file) throws IOException {
        final ZipEntry e = pathStack.pop();
        if(file.isFile()) {
            writeFileToZipOut(file, e);
        }

        zout.closeEntry();
    }

    /**
     * @param file the file to write to ZIP.
     * @param currentEntry the current ZIP entry.
     * @throws IOException Input/Output exception.
     */
    protected void writeFileToZipOut(final File file, final ZipEntry currentEntry)
            throws IOException {
        //write file content to ZIP output.
        final InputStream in = new FileInputStream(file);
        try {
            IoUtils.copyStream(in, zout);
        } finally {
            in.close();
        }
    }
}
