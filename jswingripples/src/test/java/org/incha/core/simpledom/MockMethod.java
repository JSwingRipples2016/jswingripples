package org.incha.core.simpledom;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class MockMethod extends MockParent implements IMethod {
    /**
     * @param elementName
     */
    public MockMethod(final String elementName) {
        super(elementName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getCategories()
     */
    @Override
    public String[] getCategories() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getClassFile()
     */
    @Override
    public IClassFile getClassFile() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getCompilationUnit()
     */
    @Override
    public ICompilationUnit getCompilationUnit() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getDeclaringType()
     */
    @Override
    public IType getDeclaringType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getFlags()
     */
    @Override
    public int getFlags() throws JavaModelException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getJavadocRange()
     */
    @Override
    public ISourceRange getJavadocRange() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getOccurrenceCount()
     */
    @Override
    public int getOccurrenceCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getTypeRoot()
     */
    @Override
    public ITypeRoot getTypeRoot() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getType(java.lang.String, int)
     */
    @Override
    public IType getType(final String name, final int occurrenceCount) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#isBinary()
     */
    @Override
    public boolean isBinary() {
        // TODO Auto-generated method stub
        return false;
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
     * @see org.eclipse.jdt.core.ISourceReference#getNameRange()
     */
    @Override
    public ISourceRange getNameRange() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#copy(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void copy(final IJavaElement container, final IJavaElement sibling,
            final String rename, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#delete(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void delete(final boolean force, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#move(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void move(final IJavaElement container, final IJavaElement sibling,
            final String rename, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#rename(java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void rename(final String name, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

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
     * @see org.eclipse.jdt.core.IMethod#getDefaultValue()
     */
    @Override
    public IMemberValuePair getDefaultValue() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getExceptionTypes()
     */
    @Override
    public String[] getExceptionTypes() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getTypeParameterSignatures()
     */
    @Override
    public String[] getTypeParameterSignatures() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getTypeParameters()
     */
    @Override
    public ITypeParameter[] getTypeParameters() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getNumberOfParameters()
     */
    @Override
    public int getNumberOfParameters() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getParameters()
     */
    @Override
    public ILocalVariable[] getParameters() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getKey()
     */
    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getParameterNames()
     */
    @Override
    public String[] getParameterNames() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getParameterTypes()
     */
    @Override
    public String[] getParameterTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getRawParameterNames()
     */
    @Override
    public String[] getRawParameterNames() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getReturnType()
     */
    @Override
    public String getReturnType() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getSignature()
     */
    @Override
    public String getSignature() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#getTypeParameter(java.lang.String)
     */
    @Override
    public ITypeParameter getTypeParameter(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#isConstructor()
     */
    @Override
    public boolean isConstructor() throws JavaModelException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#isMainMethod()
     */
    @Override
    public boolean isMainMethod() throws JavaModelException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#isLambdaMethod()
     */
    @Override
    public boolean isLambdaMethod() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#isResolved()
     */
    @Override
    public boolean isResolved() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMethod#isSimilar(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public boolean isSimilar(final IMethod method) {
        // TODO Auto-generated method stub
        return false;
    }

}
