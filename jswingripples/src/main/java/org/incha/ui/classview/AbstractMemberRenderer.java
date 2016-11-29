package org.incha.ui.classview;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.JSwingRipplesIMemberServices;
import org.incha.core.search.Searcher;
import org.incha.ui.jripples.EIGStatusMarks;

public abstract class AbstractMemberRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 5646472411943179112L;

    protected final RootTypeIcon rootTypeIcon = new RootTypeIcon();
    protected final FieldIcon fieldIcon = new FieldIcon();
    protected final MethodIcon methodIcon = new MethodIcon();

    /**
     * Default constructor.
     */
    public AbstractMemberRenderer() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable t, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        setBackground(null);
        final JLabel label = (JLabel) super.getTableCellRendererComponent(
                t, value, isSelected, hasFocus, row, column);
        label.setHorizontalTextPosition(RIGHT);
        final AbstractHierarchicalView view = (AbstractHierarchicalView) t;

        final JSwingRipplesEIGNode node = (JSwingRipplesEIGNode) value;
        if (node == null) {
            return this;
        }

        try {
            if (column == 0) {
                final AbstractJavaElementIcon icon = getIcon(node);
                if (icon != null) { // initializer can have not any icon
                    icon.setAccessType(getAccessType(node));
                    icon.setHierarchyOffset(view.getHierarchyOffset(node));
                    icon.setHasChildren(view.hasChildren(node));
                    icon.setExpanded(view.isExpanded(node));
                }

                if (node.getNodeIMember() instanceof IType) {
                    //expanded state
                    final IType type = (IType) node.getNodeIMember();

                    //access type
                    final int flags = type.getFlags();

                    //type (interface/enum/class)
                    if (Flags.isEnum(flags)) {
                        rootTypeIcon.setType(RootTypeIcon.ENUM);
                    } else if (Flags.isInterface(flags)) {
                        rootTypeIcon.setType(RootTypeIcon.INTERFACE);
                    } else {
                        rootTypeIcon.setType(RootTypeIcon.CLASS);
                    }

                    final String mark = node.getMark();
                    rootTypeIcon.setMarkImage(
                            EIGStatusMarks.getImageDescriptorForMark(mark == null ? "" : mark));
                }

                if (node.getNodeIMember() instanceof IInitializer) {
                    label.setText("{...}");
                } else {
                    label.setText(node.getShortName());
                }
                label.setIcon(icon);
            }
        } catch (final JavaModelException e) {
            e.printStackTrace();
        }

        renderOtherColumn(label, node, column);
        return label;
    }

    /**
     * Get the icon corresponding the the node's type.
     * @param node the EIG node.
     * @return the corresponding icon.
     */
    private AbstractJavaElementIcon getIcon(final JSwingRipplesEIGNode node) {
        final IMember member = node.getNodeIMember();
        if (member instanceof IType) {
            return rootTypeIcon;
        } else if (member instanceof IField) {
            return fieldIcon;
        } else if (member instanceof IInitializer) {
            return methodIcon;
        } else if (member instanceof IMethod) {
            return methodIcon;
        }

        return null;
    }

    /**
     * @param label
     * @param member
     * @param column
     */
    protected abstract void renderOtherColumn(final JLabel label, final JSwingRipplesEIGNode member,
            final int column);
    /**
     * Generates the complete name for the given EIG node.
     * @param node the EIG node.
     * @return the node's full name.
     */
    protected String getFullName(final JSwingRipplesEIGNode node) {
        final IMember member = node.getNodeIMember();
        String fullName;
        if(member==null) return "";
        if (member instanceof IType) fullName=((IType) member).getFullyQualifiedName();
            else {fullName=JSwingRipplesIMemberServices.getTopDeclaringType(member).getFullyQualifiedName()+"::"+member.getElementName();

            }

        if (fullName!=null) return fullName;
        return "";
    }

    /**
     * @param member
     * @return
     * @throws JavaModelException
     */
    protected int getAccessType(final JSwingRipplesEIGNode member) throws JavaModelException {
        final int flags = member.getNodeIMember().getFlags();
        if (Flags.isPrivate(flags)) {
            return AbstractMemberIcon.PRIVATE;
        }
        if (Flags.isPackageDefault(flags)) {
            return AbstractMemberIcon.DEFAULT;
        }
        if (Flags.isProtected(flags)) {
            return AbstractMemberIcon.PROTECTED;
        }
        if (Flags.isPublic(flags)) {
            return AbstractMemberIcon.PUBLIC;
        }

        return 0;
    }
}
