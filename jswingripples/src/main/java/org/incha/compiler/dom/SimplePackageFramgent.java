package org.incha.compiler.dom;

import org.eclipse.jdt.internal.core.PackageFragment;

/**
 * Simplificadot de PackageFragment
 */
class SimplePackageFramgent extends PackageFragment {
    private String elementName = "";
    private final String projectName;

    /**
     * @param projectName
     */
    public SimplePackageFramgent(final String projectName) {
        super(null, new String[0]);
        this.projectName = projectName;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.PackageFragment#internalIsValidPackageName()
     */
    @Override
    protected boolean internalIsValidPackageName() {
        return true;
    }
    /**
     * @return the elementName
     */
    @Override
    public String getElementName() {
        return elementName;
    }
    /**
     * @param elementName the elementName to set
     */
    public void setElementName(final String elementName) {
        this.elementName = elementName;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.JavaElement#getHandleMemento(java.lang.StringBuffer)
     */
    @Override
    protected void getHandleMemento(final StringBuffer buff) {
        buff.append(projectName);
        buff.append('=');
        buff.append('/');
        buff.append(getHandleMementoDelimiter());
        escapeMementoName(buff, getElementName());
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.PackageFragment#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SimplePackageFramgent)) {
            return false;
        }

        final SimplePackageFramgent other = (SimplePackageFramgent) o;
        return getElementName().equals(other.getElementName());
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.PackageFragment#hashCode()
     */
    @Override
    public int hashCode() {
        return getElementName().hashCode();
    }
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    @Override
//    protected void generateInfos(final Object info, final HashMap newElements, final IProgressMonitor monitor)
//            throws JavaModelException {
//        newElements.put(this, info);
//    }
}
