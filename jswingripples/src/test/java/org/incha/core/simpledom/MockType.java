package org.incha.core.simpledom;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

@SuppressWarnings("deprecation")
public class MockType extends MockParent implements IType {
    private String superClassName;
    private String[] superInterfaceNames = {};

    /**
     * @param elementName element name.
     */
    public MockType(final String elementName) {
        super(elementName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getCategories()
     */
    @Override
    public String[] getCategories() throws JavaModelException {
        return new String[0];
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getClassFile()
     */
    @Override
    public IClassFile getClassFile() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getCompilationUnit()
     */
    @Override
    public ICompilationUnit getCompilationUnit() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getDeclaringType()
     */
    @Override
    public IType getDeclaringType() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getFlags()
     */
    @Override
    public int getFlags() throws JavaModelException {
        return 0;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getJavadocRange()
     */
    @Override
    public ISourceRange getJavadocRange() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getOccurrenceCount()
     */
    @Override
    public int getOccurrenceCount() {
        return 0;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getTypeRoot()
     */
    @Override
    public ITypeRoot getTypeRoot() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getType(java.lang.String, int)
     */
    @Override
    public IType getType(final String name, final int occurrenceCount) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#isBinary()
     */
    @Override
    public boolean isBinary() {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSource()
     */
    @Override
    public String getSource() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSourceRange()
     */
    @Override
    public ISourceRange getSourceRange() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getNameRange()
     */
    @Override
    public ISourceRange getNameRange() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#copy(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void copy(final IJavaElement container, final IJavaElement sibling,
            final String rename, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#delete(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void delete(final boolean force, final IProgressMonitor monitor)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#move(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void move(final IJavaElement container, final IJavaElement sibling,
            final String rename, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#rename(java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void rename(final String name, final boolean replace, final IProgressMonitor monitor)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IAnnotatable#getAnnotation(java.lang.String)
     */
    @Override
    public IAnnotation getAnnotation(final String name) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IAnnotatable#getAnnotations()
     */
    @Override
    public IAnnotation[] getAnnotations() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final ICompletionRequestor requestor) throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final ICompletionRequestor requestor, final WorkingCopyOwner owner)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final CompletionRequestor requestor) throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final CompletionRequestor requestor, final IProgressMonitor monitor)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final CompletionRequestor requestor, final WorkingCopyOwner owner)
            throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void codeComplete(final char[] snippet, final int insertion, final int position,
            final char[][] localVariableTypeNames, final char[][] localVariableNames,
            final int[] localVariableModifiers, final boolean isStatic,
            final CompletionRequestor requestor, final WorkingCopyOwner owner,
            final IProgressMonitor monitor) throws JavaModelException {
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createField(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IField createField(final String contents, final IJavaElement sibling,
            final boolean force, final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createInitializer(java.lang.String, org.eclipse.jdt.core.IJavaElement, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IInitializer createInitializer(final String contents,
            final IJavaElement sibling, final IProgressMonitor monitor)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createMethod(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IMethod createMethod(final String contents, final IJavaElement sibling,
            final boolean force, final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createType(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IType createType(final String contents, final IJavaElement sibling,
            final boolean force, final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#findMethods(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public IMethod[] findMethods(final IMethod method) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getChildrenForCategory(java.lang.String)
     */
    @Override
    public IJavaElement[] getChildrenForCategory(final String category)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getField(java.lang.String)
     */
    @Override
    public IField getField(final String name) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getFields()
     */
    @Override
    public IField[] getFields() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getFullyQualifiedName()
     */
    @Override
    public String getFullyQualifiedName() {
        return getElementName();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getFullyQualifiedName(char)
     */
    @Override
    public String getFullyQualifiedName(final char enclosingTypeSeparator) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getFullyQualifiedParameterizedName()
     */
    @Override
    public String getFullyQualifiedParameterizedName()
            throws JavaModelException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getInitializer(int)
     */
    @Override
    public IInitializer getInitializer(final int occurrenceCount) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getInitializers()
     */
    @Override
    public IInitializer[] getInitializers() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getKey()
     */
    @Override
    public String getKey() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getMethod(java.lang.String, java.lang.String[])
     */
    @Override
    public IMethod getMethod(final String name, final String[] parameterTypeSignatures) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getMethods()
     */
    @Override
    public IMethod[] getMethods() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getPackageFragment()
     */
    @Override
    public IPackageFragment getPackageFragment() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperclassName()
     */
    @Override
    public String getSuperclassName() throws JavaModelException {
        return superClassName;
    }
    /**
     * @param superClassName the superClassName to set
     */
    public void setSuperClassName(final String superClassName) {
        this.superClassName = superClassName;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperclassTypeSignature()
     */
    @Override
    public String getSuperclassTypeSignature() throws JavaModelException {
        return getSuperclassName();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperInterfaceTypeSignatures()
     */
    @Override
    public String[] getSuperInterfaceTypeSignatures() throws JavaModelException {
        return getSuperInterfaceNames();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperInterfaceNames()
     */
    @Override
    public String[] getSuperInterfaceNames() throws JavaModelException {
        return superInterfaceNames;
    }
    /**
     * @param superInterfaceNames the superInterfaceNames to set
     */
    public void setSuperInterfaceNames(final String[] superInterfaceNames) {
        this.superInterfaceNames = superInterfaceNames;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeParameterSignatures()
     */
    @Override
    public String[] getTypeParameterSignatures() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeParameters()
     */
    @Override
    public ITypeParameter[] getTypeParameters() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getType(java.lang.String)
     */
    @Override
    public IType getType(final String name) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeParameter(java.lang.String)
     */
    @Override
    public ITypeParameter getTypeParameter(final String name) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeQualifiedName()
     */
    @Override
    public String getTypeQualifiedName() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeQualifiedName(char)
     */
    @Override
    public String getTypeQualifiedName(final char enclosingTypeSeparator) {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypes()
     */
    @Override
    public IType[] getTypes() throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isAnonymous()
     */
    @Override
    public boolean isAnonymous() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isClass()
     */
    @Override
    public boolean isClass() throws JavaModelException {
        return true;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isEnum()
     */
    @Override
    public boolean isEnum() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isInterface()
     */
    @Override
    public boolean isInterface() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isAnnotation()
     */
    @Override
    public boolean isAnnotation() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isLocal()
     */
    @Override
    public boolean isLocal() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isMember()
     */
    @Override
    public boolean isMember() throws JavaModelException {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isResolved()
     */
    @Override
    public boolean isResolved() {
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#loadTypeHierachy(java.io.InputStream, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy loadTypeHierachy(final InputStream input,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newSupertypeHierarchy(final IProgressMonitor monitor)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newSupertypeHierarchy(
            final ICompilationUnit[] workingCopies, final IProgressMonitor monitor)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newSupertypeHierarchy(final IWorkingCopy[] workingCopies,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newSupertypeHierarchy(final WorkingCopyOwner owner,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final IJavaProject project,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final IJavaProject project,
            final WorkingCopyOwner owner, final IProgressMonitor monitor)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final IProgressMonitor monitor)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final ICompilationUnit[] workingCopies,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final IWorkingCopy[] workingCopies,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ITypeHierarchy newTypeHierarchy(final WorkingCopyOwner owner,
            final IProgressMonitor monitor) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String)
     */
    @Override
    public String[][] resolveType(final String typeName) throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public String[][] resolveType(final String typeName, final WorkingCopyOwner owner)
            throws JavaModelException {
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isLambda()
     */
    @Override
    public boolean isLambda() {
        return false;
    }
}
