package org.incha.ui.classview;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class RootTypeIcon extends AbstractJavaElementIcon {
    public static int ICON_WIDTH = 40;

    //type of type
    public static final int CLASS = 0;
    public static final int INTERFACE = 1;
    public static final int ENUM = 2;

    //access modifier

    private int type = CLASS;

    private Image markImage;

    /**
     * @param COLLAPSED collapsed image.
     * @param EXPANDED expanded image.
     * @param CLASS class image.
     */
    public RootTypeIcon() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.swing.Icon#getIconHeight()
     */
    @Override
    public int getIconHeight() {
        return 20;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractJavaElementIcon#getWidthPrivate()
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
        final BufferedImage typeImage = getTypeImage();
        g.drawImage(typeImage, offset, 0, null);

        //drow mark image
        if (markImage != null) {
            g.drawImage(markImage, offset, 0, null);
        }

        final BufferedImage accessTypeImage = getAccessTypeImage();
        if (accessTypeImage != null) {
            g.drawImage(accessTypeImage,
                    getIconWidth() - accessTypeImage.getWidth() + 6,
                    height - accessTypeImage.getHeight(),
                    null);
        }
    }

    /**
     * @return
     */
    protected BufferedImage getAccessTypeImage() {
        //method images
        switch(getAccessType()) {
            case PROTECTED:
                return Images.PROTECTED_METHOD;
            case DEFAULT:
                return Images.DEFAULT_METHOD;
            case PRIVATE:
                return Images.PRIVATE_METHOD;
                default:
                    return null;
        }
    }

    /**
     * @return
     */
    protected BufferedImage getTypeImage() {
        final BufferedImage typeImage;
        switch (type) {
            case INTERFACE:
                typeImage = Images.INTERFACE;
                break;
            case ENUM:
                typeImage = Images.ENUM;
                break;
            case CLASS:
                typeImage = Images.CLASS;
                break;
                default:
                    typeImage = Images.CLASS;
        }
        return typeImage;
    }
    /**
     * @param type the type to set
     */
    public void setType(final int type) {
        this.type = type;
    }
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param markImage
     */
    public void setMarkImage(final Image markImage) {
        this.markImage = markImage;
    }
    /**
     * @return the markImage
     */
    public Image getMarkImage() {
        return markImage;
    }
}
