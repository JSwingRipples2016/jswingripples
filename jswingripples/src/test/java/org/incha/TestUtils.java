package org.incha;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

public final class TestUtils {

    private TestUtils() {}

    /**
     * @param class1
     * @return
     */
    public static File findSourceForClass(final Class<?> clazz) throws IOException {
        //find this source file
        //get class file
        final String className = clazz.getName();
        final URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        final File file = new File(URLDecoder.decode(location.getFile(),
                System.getProperty("file.encoding")));

        //find project root.
        String root = null;
        File current = file;
        while (current != null) {
            if (current.isDirectory() && new File(current, "pom.xml").exists()) {
                root = current.getAbsolutePath();
                break;
            }
            current = current.getParentFile();
        }

        final String relativePath = className.replace('.', File.separatorChar) + ".java";

        return findByRelativePath(new File(root), relativePath);
    }

    /**
     * @param parent
     * @param relativePath
     * @return
     */
    private static File findByRelativePath(final File parent, final String relativePath) {
        if (parent.isDirectory()) {
            final File file = new File(parent.getAbsolutePath() + File.separator + relativePath);
            if (file.exists()) {
                return file;
            }

            for (final File child : parent.listFiles()) {
                final File f = findByRelativePath(child, relativePath);
                if (f != null) {
                    return f;
                }
            }
        }
        return null;
    }
}
