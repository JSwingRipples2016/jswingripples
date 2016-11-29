package org.incha.compiler.dom;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.incha.utils.IoUtils;

/**
 * Simplificador del CompilationUnit de eclipse
 */
public final class SimpleCompilationUnit extends CompilationUnit {
    private final File file;
    private IJavaElement[] children;
    /**
     * @param parent
     * @param file
     */
    public SimpleCompilationUnit(final String projectName, final File file) {
        super(new SimplePackageFramgent(projectName), file.getName(), DefaultWorkingCopyOwner.PRIMARY);
        this.file = file;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getContents()
     */
    @Override
    public char[] getContents() {
        try {
            return new String(IoUtils.getBytes(file)).toCharArray();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getSource()
     */
    @Override
    public String getSource() {
        return new String(getContents());
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getMainTypeName()
     */
    @Override
    public char[] getMainTypeName() {
        return getFileName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getFileName()
     */
    @Override
    public char[] getFileName() {
        return file.getAbsolutePath().toCharArray();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.JavaElement#getParent()
     */
    @Override
    public SimplePackageFramgent getParent() {
        return (SimplePackageFramgent) super.getParent();
    }
    /**
     * @param name
     */
    public void setPackageName(final String name) {
        getParent().setElementName(name);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#validateCompilationUnit(org.eclipse.core.resources.IResource)
     */
    @Override
    protected IStatus validateCompilationUnit(final IResource resource) {
        return Status.OK_STATUS;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void generateInfos(final Object info, final HashMap newElements, final IProgressMonitor monitor)
            throws JavaModelException {
        newElements.put(this, info);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.JavaElement#getChildren()
     */
    @Override
    public IJavaElement[] getChildren() throws JavaModelException {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(final IJavaElement[] children) {
        this.children = children;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getPath()
     */
    @Override
    public IPath getPath() {
        return Path.fromOSString(file.getAbsolutePath());
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.CompilationUnit#getPackageDeclarations()
     */
    @Override
    public IPackageDeclaration[] getPackageDeclarations()
            throws JavaModelException {
        return super.getPackageDeclarations();
    }
}
