package org.incha.ui.classview;

import java.awt.image.BufferedImage;

public class FieldIcon extends AbstractMemberIcon {
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getDefaultImage()
     */
    @Override
    protected BufferedImage getDefaultImage() {
        return Images.DEFAULT_FIELD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getPrivateImage()
     */
    @Override
    protected BufferedImage getPrivateImage() {
        return Images.PRIVATE_FIELD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getProtectedImage()
     */
    @Override
    protected BufferedImage getProtectedImage() {
        return Images.PROTECTED_FIELD;
    }
    /* (non-Javadoc)
     * @see org.incha.ui.stats.AbstractMemberIcon#getPublicImage()
     */
    @Override
    protected BufferedImage getPublicImage() {
        return Images.PUBLIC_FIELD;
    }
}
