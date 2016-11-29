package org.incha.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class IoUtils {
    private static class FileDeleteVisitor implements FileVisitor {
        /**
         * @param t the file.
         */
        @Override
        public void exit(final File t) {
            delete(t);
        }
        /**
         * @param t the file
         * @return should recursing next.
         */
        @Override
        public boolean enter(final File t) {
            return true;
        }
        /**
         * @param f the file.
         */
        protected void delete(final File f) {
            f.delete();
        }
    }
    /**
     * The buffer size.
     */
    private static final int BUFF_SIZE = 512;
    private static final String ENCODING = System.getProperty("file.encoding");

    /**
	 * The constructor.
	 */
	private IoUtils() {
		super();
	}

    /**
     * @param in the input stream.
     * @param out the output stream.
     * @throws IOException Input/Output exception.
     */
    public static void copy(final InputStream in, final OutputStream out) throws IOException {
        copyStream(in, out);
    }
    /**
     * @param in input.
     * @return the bytes from input.
     * @throws IOException exeption of reading bytes from input stream.
     */
    public static byte[] getBytes(final InputStream in) throws IOException {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        copy(in, bout);
        return bout.toByteArray();
    }
	/**
	 * @param url the URL.
	 * @return the property file.
	 * @throws IOException Input/Output exception.
	 */
	public static Map<String, String> loadPropertiesFromXml(final URL url) throws IOException {
		//load properties
		final Properties p = new Properties();
		final InputStream in = url.openStream();
		try {
			p.loadFromXML(in);
		} finally {
			in.close();
		}

		//move properties to string map.
		final Map<String, String> map = new HashMap<String, String>();
		for (final Map.Entry<Object, Object> e : p.entrySet()) {
			map.put(e.getKey().toString(), e.getValue().toString());
		}

		return map;
	}
	/**
	 * @param in the input stream.
	 * @param out the output stream.
	 * @throws IOException Input/Output exception.
	 */
	public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
		final byte[] buff = new byte[BUFF_SIZE];
		int len;
		while ((len = in.read(buff)) > -1) {
			out.write(buff, 0, len);
			out.flush();
		}
	}
	/**
	 * @param file deletes file recursively.
	 * @throws IOException the exception.
	 */
	public static void deleteRecursive(final File file) throws IOException {
	    visitTo(file, new FileDeleteVisitor());
	}
	/**
	 * @param to the folder.
	 * @param file the file to recursive copy
	 * @throws IOException is thrown if unable to copy file.
	 */
	public static void copyRecursiveTo(final File file, final File to) throws IOException {
		if (file.isDirectory()) {
			final File nextParent = new File(to, file.getName());
			nextParent.mkdir();

			for (final File f : file.listFiles()) {
				copyRecursiveTo(f, nextParent);
			}
		} else {
			copyFileContent(file, new File(to, file.getName()));
		}
	}
	/**
	 * @param source the source file.
	 * @param dest the destination file.
	 * @throws IOException exception of copy file.
	 */
	public static void copyFileContent(final File source, final File dest) throws IOException {
		final InputStream in = new FileInputStream(source);
		try {
			final OutputStream out = new FileOutputStream(dest);
			try {
				copyStream(in, out);
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
	/**
	 * @param url the URL.
	 * @return the file for given URL.
	 * @throws IOException input/output exception.
	 */
	public static File getFileFromUrl(final URL url) throws IOException {
        final String name = URLDecoder.decode(url.getFile(), ENCODING);
	    final File f = new File(name);
	    if (!f.exists()) {
	        throw new FileNotFoundException("Failed to find the file for URL: " + url);
	    }

	    return f;
	}
	/**
	 * @param root the root.
	 * @param visitor the visitor.
	 * @throws IOException Input/Output exception.
	 */
	public static void visitTo(final File root, final FileVisitor visitor) throws IOException {
	    if (visitor.enter(root)) {
            if (root.isDirectory()) {
                for (final File f : root.listFiles()) {
                    visitTo(f, visitor);
                }
            }
	    }
	    visitor.exit(root);
	}

    /**
     * @param file source file.
     * @return file content.
     * @throws IOException
     */
    public static byte[] getBytes(final File file) throws IOException {
        final InputStream in = new FileInputStream(file);
        try {
            return getBytes(in);
        } finally {
            in.close();
        }
    }
}
