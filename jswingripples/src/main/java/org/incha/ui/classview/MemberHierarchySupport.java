package org.incha.ui.classview;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class MemberHierarchySupport {
    private List<JSwingRipplesEIGNode> nodes = new LinkedList<JSwingRipplesEIGNode>();
    private Map<IMember, JSwingRipplesEIGNode> membersToNodes = new HashMap<IMember, JSwingRipplesEIGNode>();

    /**
     * Default constructor.
     */
    public MemberHierarchySupport(final Collection<JSwingRipplesEIGNode> listOfMembers) {
        super();

        //create maps.
        for (final JSwingRipplesEIGNode node : listOfMembers) {
            nodes.add(node);
            membersToNodes.put(node.getNodeIMember(), node);
        }
    }
    /**
     * @param m the member.
     * @return parent registered in given support.
     */
    public JSwingRipplesEIGNode getParent(final JSwingRipplesEIGNode m) {
        return membersToNodes.get(m.getNodeIMember().getParent());
    }
    /**
     * @param m the member.
     * @return the hierarchy depth of given member, i.e. number of parents hierarchically.
     */
    public int getHierarchyDepth(final JSwingRipplesEIGNode m) {
        int depth = 0;
        JSwingRipplesEIGNode current = m;
        while (current != null) {
            current = getParent(current);
            if (current != null) {
                depth++;
            }
        }

        return depth;
    }
    /**
     * @return
     */
    public JSwingRipplesEIGNode[] getRootTypes() {
        final List<JSwingRipplesEIGNode> roots = new LinkedList<JSwingRipplesEIGNode>();
        for (final JSwingRipplesEIGNode m : getMembers()) {
            if (m.getNodeIMember() instanceof IType && isTopNode(m)) {
                roots.add(m);
            }
        }
        return roots.toArray(new JSwingRipplesEIGNode[roots.size()]);
    }
    /**
     * @param m
     * @return
     */
    private boolean isTopNode(final JSwingRipplesEIGNode m) {
        final IJavaElement parent = m.getNodeIMember().getParent();
        return parent == null || parent instanceof ICompilationUnit;
    }
    /**
     * @return list of members.
     */
    public List<JSwingRipplesEIGNode> getMembers() {
        return new LinkedList<JSwingRipplesEIGNode>(nodes);
    }
    /**
     * @param parent parent.
     * @return array of children.
     */
    public JSwingRipplesEIGNode[] getChildren(final JSwingRipplesEIGNode parent) {
        try {
            final IJavaElement[] elements = parent.getNodeIMember().getChildren();
            final List<JSwingRipplesEIGNode> set = new LinkedList<JSwingRipplesEIGNode>();
            for (final IJavaElement e : elements) {
                final JSwingRipplesEIGNode eigNode = this.membersToNodes.get(e);
                if (eigNode != null) {
                    set.add(eigNode);
                }
            }
            return set.toArray(new JSwingRipplesEIGNode[set.size()]);
        } catch (final JavaModelException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param parent the parent.
     * @return
     */
    public boolean hasChildren(final JSwingRipplesEIGNode parent) {
        return getChildren(parent).length > 0;
    }

    /**
     * Clears data.
     */
    public void clear() {
        this.nodes.clear();
        this.membersToNodes.clear();
    }
}
