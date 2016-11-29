package org.incha.core.simpledom;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class MockLocalVariable extends MockJavaElement implements
        ILocalVariable {

    /**
     * @param elementName
     */
    public MockLocalVariable(final String elementName) {
        super(elementName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSource()
     */
    @Override
    public String getSource() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSourceRange()
     */
    @Override
    public ISourceRange getSourceRange() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IAnnotatable#getAnnotation(java.lang.String)
     */
    @Override
    public IAnnotation getAnnotation(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IAnnotatable#getAnnotations()
     */
    @Override
    public IAnnotation[] getAnnotations() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#getNameRange()
     */
    @Override
    public ISourceRange getNameRange() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#getTypeSignature()
     */
    @Override
    public String getTypeSignature() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#isParameter()
     */
    @Override
    public boolean isParameter() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#getFlags()
     */
    @Override
    public int getFlags() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#getDeclaringMember()
     */
    @Override
    public IMember getDeclaringMember() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ILocalVariable#getTypeRoot()
     */
    @Override
    public ITypeRoot getTypeRoot() {
        // TODO Auto-generated method stub
        return null;
    }
}
