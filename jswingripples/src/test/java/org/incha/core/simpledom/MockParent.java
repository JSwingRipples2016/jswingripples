package org.incha.core.simpledom;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;

public class MockParent extends MockJavaElement implements IParent {
    protected final List<IJavaElement> children = new LinkedList<IJavaElement>();

    /**
     * @param name element name.
     */
    public MockParent(final String elementName) {
        super(elementName);
    }

    public void add(final MockJavaElement e) {
        children.add(e);
        e.parent = this;
    }
    public void remove(final MockJavaElement e) {
        if (e != null) {
            children.remove(e);
            e.parent = null;
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IParent#getChildren()
     */
    @Override
    public IJavaElement[] getChildren() throws JavaModelException {
        return children.toArray(new IJavaElement[children.size()]);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IParent#hasChildren()
     */
    @Override
    public boolean hasChildren() throws JavaModelException {
        return children.size() > 0;
    }
}
