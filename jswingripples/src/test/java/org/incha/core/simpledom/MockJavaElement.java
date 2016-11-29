package org.incha.core.simpledom;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaModelException;

public class MockJavaElement implements IJavaElement {
    private final String elementName;
    protected IJavaElement parent;

    /**
     *
     */
    public MockJavaElement(final String elementName) {
        super();
        this.elementName = elementName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") final Class arg0) {
        IJavaElement parent = getParent();
        while (parent != null) {
            if (parent.getClass().isAssignableFrom(arg0)) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#exists()
     */
    @Override
    public boolean exists() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getAncestor(int)
     */
    @Override
    public IJavaElement getAncestor(final int ancestorType) {

        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getAttachedJavadoc(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public String getAttachedJavadoc(final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getCorrespondingResource()
     */
    @Override
    public IResource getCorrespondingResource() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getElementName()
     */
    @Override
    public String getElementName() {
        return elementName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getElementType()
     */
    @Override
    public int getElementType() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getHandleIdentifier()
     */
    @Override
    public String getHandleIdentifier() {
        return getElementName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getJavaModel()
     */
    @Override
    public IJavaModel getJavaModel() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getJavaProject()
     */
    @Override
    public IJavaProject getJavaProject() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getOpenable()
     */
    @Override
    public IOpenable getOpenable() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getParent()
     */
    @Override
    public IJavaElement getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getPath()
     */
    @Override
    public IPath getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getPrimaryElement()
     */
    @Override
    public IJavaElement getPrimaryElement() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getResource()
     */
    @Override
    public IResource getResource() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getSchedulingRule()
     */
    @Override
    public ISchedulingRule getSchedulingRule() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getUnderlyingResource()
     */
    @Override
    public IResource getUnderlyingResource() throws JavaModelException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#isStructureKnown()
     */
    @Override
    public boolean isStructureKnown() throws JavaModelException {
        return true;
    }
}
