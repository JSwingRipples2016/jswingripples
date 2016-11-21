package org.incha.ui.classview;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;

public abstract class AbstractJavaElementIcon implements Icon {
    public static final int PUBLIC = 0;
    public static final int PRIVATE = 1;
    public static final int DEFAULT = 2;
    public static final int PROTECTED = 3;

    private int accessType = PUBLIC;

    private int hierarchyOffet;

    private boolean isExpanded;
    private boolean hasChildren;

    /**
     * @param COLLAPSED collapsed image.
     * @param expandedImage expanded image.
     * @param classImage class image.
     */
    public AbstractJavaElementIcon() {
        super();
    }
    /**
     * @param accessType the accessType to set
     */
    public void setAccessType(final int accessType) {
        this.accessType = accessType;
    }
    /**
     * @return
     */
    protected int getHierarchyOffset() {
        return hierarchyOffet ;
    }
    /**
     * @param offset the offset to set
     */
    public void setHierarchyOffset(final int offset) {
        this.hierarchyOffet = offset;
    }
    /**
     * @return the accessType
     */
    protected int getAccessType() {
        return accessType;
    }
    /* (non-Javadoc)
     * @see javax.swing.Icon#getIconWidth()
     */
    @Override
    public final int getIconWidth() {
        int width = getHierarchyOffset() + getWidthPrivate();
        if (hasChildren()) {
            width += 20;
        }
        return width;
    }
    /**
     * @return
     */
    protected abstract int getWidthPrivate();

    /* (non-Javadoc)
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    @Override
    public final void paintIcon(final Component c, final Graphics g, final int x, final int y) {
        if (!g.getClipBounds().intersects(
                new Rectangle(0, 0, getIconWidth(), getIconHeight()))) {
            return;
        }

        int offset = getHierarchyOffset();
        if (hasChildren()) {
            if (isExpanded()) {
                g.drawImage(Images.EXPANDED, offset, 0, c);
            } else {
                g.drawImage(Images.COLLAPSED, offset, 0, c);
            }
            offset += 20;
        }

        paintIcon(offset, g, c.getWidth(), c.getHeight());
    }
    /**
     * @param offset offset.
     * @param g graphics context.
     * @param width visual area width
     * @param height visial area height
     */
    protected abstract void paintIcon(int offset, Graphics g, int width, int height);
    /**
     * @param hasChildren
     */
    public void setHasChildren(final boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    /**
     * @return the hasChildren
     */
    protected boolean hasChildren() {
        return hasChildren;
    }
    /**
     * @param isExpanded the isExpanded to set
     */
    public void setExpanded(final boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
    /**
     * @return the isExpanded
     */
    public boolean isExpanded() {
        return isExpanded;
    }
}
