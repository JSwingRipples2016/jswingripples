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
import org.incha.ui.TaskProgressMonitor;

/**
 * Bundles the JavaDomVisitorAdapter to update the TaskProgressMonitor
 *
 */
public class JavaDomVisitorByMonitor extends JavaDomVisitorAdapter {
    private final TaskProgressMonitor monitor;
    private final JavaDomVisitor visitor;
    /**
     * @param visitor DOM visitor.
     * @param monitor task montor;
     */
    public JavaDomVisitorByMonitor(final JavaDomVisitor visitor, final TaskProgressMonitor monitor) {
        this.monitor = monitor;
        this.visitor = visitor;
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IImportContainer)
     */
    @Override
    public boolean startVisit(final IImportContainer e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IField)
     */
    @Override
    public boolean startVisit(final IField e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IInitializer)
     */
    @Override
    public boolean startVisit(final IInitializer e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public boolean startVisit(final IMethod e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IType)
     */
    @Override
    public boolean startVisit(final IType e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IPackageFragment)
     */
    @Override
    public boolean startVisit(final IPackageFragment e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.ITypeRoot)
     */
    @Override
    public boolean startVisit(final ITypeRoot e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.ILocalVariable)
     */
    @Override
    public boolean startVisit(final ILocalVariable e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
    /* (non-Javadoc)
     * @see org.incha.compiler.dom.JavaDomVisitor#startVisit(org.eclipse.jdt.core.IPackageDeclaration)
     */
    @Override
    public boolean startVisit(final IPackageDeclaration e) {
        return !monitor.isCanceled() && visitor.startVisit(e);
    }
}
