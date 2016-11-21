package org.incha.core.simpledom;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IBufferFactory;
import org.eclipse.jdt.core.ICodeCompletionRequestor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

@SuppressWarnings("deprecation")
public class MockCompilationUnit extends MockParent implements ICompilationUnit {
    private final IType[] roots;
    /**
     * Default constructor.
     */
    public MockCompilationUnit(final String name, final IType... roots) {
        super(name);
        this.roots = roots;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#close()
     */
    @Override
    public void close() throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#findRecommendedLineSeparator()
     */
    @Override
    public String findRecommendedLineSeparator() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#getBuffer()
     */
    @Override
    public IBuffer getBuffer() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#hasUnsavedChanges()
     */
    @Override
    public boolean hasUnsavedChanges() throws JavaModelException {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#isConsistent()
     */
    @Override
    public boolean isConsistent() throws JavaModelException {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#isOpen()
     */
    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#makeConsistent(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void makeConsistent(final IProgressMonitor progress)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#open(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void open(final IProgressMonitor progress) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IOpenable#save(org.eclipse.core.runtime.IProgressMonitor, boolean)
     */
    @Override
    public void save(final IProgressMonitor progress, final boolean force)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#commit(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void commit(final boolean force, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#findSharedWorkingCopy(org.eclipse.jdt.core.IBufferFactory)
     */
    @Override
    public IJavaElement findSharedWorkingCopy(final IBufferFactory bufferFactory) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#getOriginal(org.eclipse.jdt.core.IJavaElement)
     */
    @Override
    public IJavaElement getOriginal(final IJavaElement workingCopyElement) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#getOriginalElement()
     */
    @Override
    public IJavaElement getOriginalElement() {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#getSharedWorkingCopy(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.jdt.core.IBufferFactory, org.eclipse.jdt.core.IProblemRequestor)
     */
    @Override
    public IJavaElement getSharedWorkingCopy(final IProgressMonitor monitor,
            final IBufferFactory factory, final IProblemRequestor problemRequestor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#getWorkingCopy()
     */
    @Override
    public IJavaElement getWorkingCopy() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#getWorkingCopy(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.jdt.core.IBufferFactory, org.eclipse.jdt.core.IProblemRequestor)
     */
    @Override
    public IJavaElement getWorkingCopy(final IProgressMonitor monitor,
            final IBufferFactory factory, final IProblemRequestor problemRequestor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#isBasedOn(org.eclipse.core.resources.IResource)
     */
    @Override
    public boolean isBasedOn(final IResource resource) {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#reconcile()
     */
    @Override
    public IMarker[] reconcile() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IWorkingCopy#reconcile(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void reconcile(final boolean forceProblemDetection,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ITypeRoot#findPrimaryType()
     */
    @Override
    public IType findPrimaryType() {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ITypeRoot#getElementAt(int)
     */
    @Override
    public IJavaElement getElementAt(final int position) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ITypeRoot#getWorkingCopy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ICompilationUnit getWorkingCopy(final WorkingCopyOwner owner,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
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
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.ICodeCompletionRequestor)
     */
    @Override
    public void codeComplete(final int offset, final ICodeCompletionRequestor requestor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.ICompletionRequestor)
     */
    @Override
    public void codeComplete(final int offset, final ICompletionRequestor requestor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.CompletionRequestor)
     */
    @Override
    public void codeComplete(final int offset, final CompletionRequestor requestor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void codeComplete(final int offset, final CompletionRequestor requestor,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.ICompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public void codeComplete(final int offset, final ICompletionRequestor requestor,
            final WorkingCopyOwner owner) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public void codeComplete(final int offset, final CompletionRequestor requestor,
            final WorkingCopyOwner owner) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeComplete(int, org.eclipse.jdt.core.CompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void codeComplete(final int offset, final CompletionRequestor requestor,
            final WorkingCopyOwner owner, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeSelect(int, int)
     */
    @Override
    public IJavaElement[] codeSelect(final int offset, final int length)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICodeAssist#codeSelect(int, int, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public IJavaElement[] codeSelect(final int offset, final int length,
            final WorkingCopyOwner owner) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#applyTextEdit(org.eclipse.text.edits.TextEdit, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public UndoEdit applyTextEdit(final TextEdit edit, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#becomeWorkingCopy(org.eclipse.jdt.core.IProblemRequestor, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void becomeWorkingCopy(final IProblemRequestor problemRequestor,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#becomeWorkingCopy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void becomeWorkingCopy(final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#commitWorkingCopy(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void commitWorkingCopy(final boolean force, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#createImport(java.lang.String, org.eclipse.jdt.core.IJavaElement, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IImportDeclaration createImport(final String name, final IJavaElement sibling,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#createImport(java.lang.String, org.eclipse.jdt.core.IJavaElement, int, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IImportDeclaration createImport(final String name, final IJavaElement sibling,
            final int flags, final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#createPackageDeclaration(java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IPackageDeclaration createPackageDeclaration(final String name,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#createType(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public IType createType(final String contents, final IJavaElement sibling,
            final boolean force, final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#discardWorkingCopy()
     */
    @Override
    public void discardWorkingCopy() throws JavaModelException {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#findElements(org.eclipse.jdt.core.IJavaElement)
     */
    @Override
    public IJavaElement[] findElements(final IJavaElement element) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#findWorkingCopy(org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
    public ICompilationUnit findWorkingCopy(final WorkingCopyOwner owner) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getAllTypes()
     */
    @Override
    public IType[] getAllTypes() throws JavaModelException {
        return getTypes();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getImport(java.lang.String)
     */
    @Override
    public IImportDeclaration getImport(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getImportContainer()
     */
    @Override
    public IImportContainer getImportContainer() {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getImports()
     */
    @Override
    public IImportDeclaration[] getImports() throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getPrimary()
     */
    @Override
    public ICompilationUnit getPrimary() {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getOwner()
     */
    @Override
    public WorkingCopyOwner getOwner() {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getPackageDeclaration(java.lang.String)
     */
    @Override
    public IPackageDeclaration getPackageDeclaration(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getPackageDeclarations()
     */
    @Override
    public IPackageDeclaration[] getPackageDeclarations()
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getType(java.lang.String)
     */
    @Override
    public IType getType(final String name) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getTypes()
     */
    @Override
    public IType[] getTypes() throws JavaModelException {
        return roots;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getWorkingCopy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ICompilationUnit getWorkingCopy(final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#getWorkingCopy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.jdt.core.IProblemRequestor, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ICompilationUnit getWorkingCopy(final WorkingCopyOwner owner,
            final IProblemRequestor problemRequestor, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#hasResourceChanged()
     */
    @Override
    public boolean hasResourceChanged() {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#isWorkingCopy()
     */
    @Override
    public boolean isWorkingCopy() {
        // TODO Auto-generated method stub
        return false;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#reconcile(int, boolean, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public CompilationUnit reconcile(final int astLevel,
            final boolean forceProblemDetection, final WorkingCopyOwner owner,
            final IProgressMonitor monitor) throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#reconcile(int, boolean, boolean, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public CompilationUnit reconcile(final int astLevel,
            final boolean forceProblemDetection, final boolean enableStatementsRecovery,
            final WorkingCopyOwner owner, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#reconcile(int, int, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public CompilationUnit reconcile(final int astLevel, final int reconcileFlags,
            final WorkingCopyOwner owner, final IProgressMonitor monitor)
            throws JavaModelException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ICompilationUnit#restore()
     */
    @Override
    public void restore() throws JavaModelException {
        // TODO Auto-generated method stub

    }


}
