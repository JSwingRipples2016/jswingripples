package org.incha.ui.classview;

import java.util.Comparator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class MemberComparator implements Comparator<JSwingRipplesEIGNode> {
    /**
     * Default constructor.
     */
    public MemberComparator() {
        super();
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final JSwingRipplesEIGNode o1, final JSwingRipplesEIGNode o2) {
        return compare(o1.getNodeIMember(), o2.getNodeIMember());
    }
    private int compare(final IMember m1, final IMember m2) {
        int result = compareAsTypeNotType(m1, m2);
        if (result != 0) {
            return result;
        }

        result = compareAsFieldNotField(m1, m2);
        if (result != 0) {
            return result;
        }

        return m1.getElementName().compareTo(m2.getElementName());
    }

    /**
     * @param m1
     * @param m2
     * @return
     */
    private int compareAsFieldNotField(final IMember m1, final IMember m2) {
        if (m1 instanceof IField && !(m2 instanceof IField)) {
            return -1;
        }
        if (m2 instanceof IField && !(m1 instanceof IField)) {
            return 1;
        }
        return 0;
    }

    /**
     * @param m1
     * @param m2
     * @return
     */
    private int compareAsTypeNotType(final IMember m1, final IMember m2) {
        if (m1 instanceof IType && !(m2 instanceof IType)) {
            return -1;
        }
        if (m2 instanceof IType && !(m1 instanceof IType)) {
            return 1;
        }
        return 0;
    }
}
