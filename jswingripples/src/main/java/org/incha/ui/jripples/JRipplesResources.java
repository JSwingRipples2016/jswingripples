package org.incha.ui.jripples;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JComponent;

public final class JRipplesResources {
    @SuppressWarnings("serial")
    private static final JComponent component = new JComponent(){};

    /**
     * Default constructor.
     */
    private JRipplesResources() {
        super();
    }

    /**
     * @param resource resource name.
     * @return image.
     */
    public static Image getImage(final String resource) {
        final MediaTracker tracker = new MediaTracker(component);

        final URL url = JRipplesResources.class.getClassLoader().getResource(resource);
        final Image image = url == null ? null : Toolkit.getDefaultToolkit().getImage(url);

        synchronized(tracker) {
            tracker.addImage(image, 17);
            try {
                tracker.waitForID(17, 0);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return image;
    }

    /**
     * @param resource
     * @return buffered image from given resource.
     */
    public static BufferedImage getBufferedImage(final String resource) {
        final Image im = getImage(resource);
        final BufferedImage image = new BufferedImage(
                im.getWidth(component), im.getHeight(component),
                BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics2D g = image.createGraphics();
        try {
            g.drawImage(im, 0, 0, null);
        } finally {
            g.dispose();
        }
        return image;
    }
}
