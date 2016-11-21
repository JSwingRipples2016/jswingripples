package org.incha.ui.classview;

import java.awt.image.BufferedImage;

import org.incha.ui.jripples.JRipplesResources;

public final class Images {
    // type images
    public static final BufferedImage COLLAPSED = JRipplesResources.getBufferedImage(
            "icons/expand.gif");
    public static final BufferedImage EXPANDED = JRipplesResources.getBufferedImage(
            "icons/collapse.gif");
    public static final BufferedImage CLASS = JRipplesResources.getBufferedImage(
            "icons/members/class_obj.gif");
    public static final BufferedImage INTERFACE = JRipplesResources.getBufferedImage(
            "icons/members/int_obj.gif");
    public static final BufferedImage ENUM = JRipplesResources.getBufferedImage(
            "icons/members/enum_obj.gif");

    //method images
    public static final BufferedImage PROTECTED_METHOD = JRipplesResources.getBufferedImage(
            "icons/members/methpro_obj.gif");
    public static final BufferedImage PUBLIC_METHOD = JRipplesResources.getBufferedImage(
            "icons/members/methpub_obj.gif");
    public static final BufferedImage PRIVATE_METHOD = JRipplesResources.getBufferedImage(
            "icons/members/methpri_obj.gif");
    public static final BufferedImage DEFAULT_METHOD = JRipplesResources.getBufferedImage(
            "icons/members/methdef_obj.gif");

    //field images
    public static final BufferedImage PUBLIC_FIELD = JRipplesResources.getBufferedImage(
            "icons/members/field_public_obj.gif");
    public static final BufferedImage PROTECTED_FIELD = JRipplesResources.getBufferedImage(
            "icons/members/field_protected_obj.gif");
    public static final BufferedImage PRIVATE_FIELD = JRipplesResources.getBufferedImage(
            "icons/members/field_private_obj.gif");
    public static final BufferedImage DEFAULT_FIELD = JRipplesResources.getBufferedImage(
            "icons/members/field_default_obj.gif");

    /**
     * Default constructor.
     */
    private Images() {
        super();
    }

}
