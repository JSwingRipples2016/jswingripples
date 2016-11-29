package org.incha.ui.classview;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class AbstractMemberIcon extends AbstractJavaElementIcon {
    private BufferedImage privateImage;
    private BufferedImage defaultImage;
    private BufferedImage publicImage;
    private BufferedImage protectedImage;

    /**
     * @param COLLAPSED collapsed image.
     * @param expandedImage expanded image.
     * @param classImage class image.
     */
    public AbstractMemberIcon() {
        super();
        loadAccessTypeImages();
    }

    /**
     * Loads an access type visualization image.
     */
    protected void loadAccessTypeImages() {
        this.privateImage = getPrivateImage();
        this.defaultImage = getDefaultImage();
        this.publicImage = getPublicImage();
        this.protectedImage = getProtectedImage();
    }
    /**
     * @return
     */
    protected abstract BufferedImage getDefaultImage();
    /**
     * @return
     */
    protected abstract BufferedImage getPublicImage();
    /**
     * @return
     */
    protected abstract BufferedImage getPrivateImage();
    /**
     * @return
     */
    protected abstract BufferedImage getProtectedImage();

    /* (non-Javadoc)
     * @see javax.swing.Icon#getIconHeight()
     */
    @Override
    public int getIconHeight() {
        return 20;
    }
    /**
     * @return
     */
    @Override
    protected int getWidthPrivate() {
        return 14;
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractJavaElementIcon#paintIcon(int, java.awt.Graphics)
     */
    @Override
    protected void paintIcon(final int offset, final Graphics g, final int width, final int height) {
        final BufferedImage img;
        switch (getAccessType()) {
            case PRIVATE:
                img = privateImage;
                break;
            case DEFAULT:
                img = defaultImage;
                break;
            case PROTECTED:
                img = protectedImage;
                break;
            default:
                img = publicImage;
                break;
        }

        g.drawImage(img, offset, 0, null);
    }
}
