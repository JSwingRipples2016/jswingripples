package org.incha.ui.classview;

import java.awt.image.BufferedImage;


public class MethodIcon extends AbstractMemberIcon {
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getDefaultImage()
     */
    @Override
    protected BufferedImage getDefaultImage() {
        return Images.DEFAULT_METHOD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getPrivateImage()
     */
    @Override
    protected BufferedImage getPrivateImage() {
        return Images.PRIVATE_METHOD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getPublicImage()
     */
    @Override
    protected BufferedImage getPublicImage() {
        return Images.PUBLIC_METHOD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getProtectedImage()
     */
    @Override
    protected BufferedImage getProtectedImage() {
        return Images.PROTECTED_METHOD;
    }
}
