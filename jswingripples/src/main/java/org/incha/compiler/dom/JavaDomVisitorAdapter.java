package org.incha.compiler.dom;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;

/**
 * Default implementation of JavaDomVisitor
 *
 */
public class JavaDomVisitorAdapter implements JavaDomVisitor {
    /**
     * Default constructor.
     */
    public JavaDomVisitorAdapter() {
        super();
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IImportContainer)
     */
    @Override
    public boolean startVisit(final IImportContainer e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IField)
     */
    @Override
    public boolean startVisit(final IField e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IInitializer)
     */
    @Override
    public boolean startVisit(final IInitializer e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public boolean startVisit(final IMethod e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IType)
     */
    @Override
    public boolean startVisit(final IType e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IPackageFragment)
     */
    @Override
    public boolean startVisit(final IPackageFragment e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.ITypeRoot)
     */
    @Override
    public boolean startVisit(final ITypeRoot e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.ILocalVariable)
     */
    @Override
    public boolean startVisit(final ILocalVariable e) {
        return true;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.dom.PackageDeclaration)
     */
    @Override
    public boolean startVisit(final IPackageDeclaration e) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IImportContainer)
     */
    @Override
    public void endVisit(final IImportContainer e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IField)
     */
    @Override
    public void endVisit(final IField e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IInitializer)
     */
    @Override
    public void endVisit(final IInitializer e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public void endVisit(final IMethod e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IType)
     */
    @Override
    public void endVisit(final IType e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.IPackageFragment)
     */
    @Override
    public void endVisit(final IPackageFragment e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.ITypeRoot)
     */
    @Override
    public void endVisit(final ITypeRoot e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.ILocalVariable)
     */
    @Override
    public void endVisit(final ILocalVariable e) {
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#endVisit(org.eclipse.jdt.core.dom.PackageDeclaration)
     */
    @Override
    public void endVisit(final IPackageDeclaration e) {
    }
}
