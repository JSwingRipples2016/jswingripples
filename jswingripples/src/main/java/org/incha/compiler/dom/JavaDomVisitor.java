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

public interface JavaDomVisitor {
    /**
     * @param e import container element.
     * @return TODO
     */
    boolean startVisit(IImportContainer e);
    /**
     * @param e field element.
     * @return TODO
     */
    boolean startVisit(IField e);
    /**
     * @param e initializer element.
     * @return TODO
     */
    boolean startVisit(IInitializer e);
    /**
     * @param e method element.
     * @return TODO
     */
    boolean startVisit(IMethod e);
    /**
     * @param e type element.
     */
    boolean startVisit(IType e);
    /**
     * @param e package fragment element.
     */
    boolean startVisit(IPackageFragment e);
    /**
     * @param e type root element.
     */
    boolean startVisit(ITypeRoot e);
    /**
     * @param e
     * @return TODO
     */
    boolean startVisit(ILocalVariable e);
    /**
     * @param e
     * @return TODO
     */
    boolean startVisit(IPackageDeclaration e);

    /**
     * @param e import container element.
     */
    void endVisit(IImportContainer e);
    /**
     * @param e field element.
     */
    void endVisit(IField e);
    /**
     * @param e initializer element.
     */
    void endVisit(IInitializer e);
    /**
     * @param e method element.
     */
    void endVisit(IMethod e);
    /**
     * @param e type element.
     */
    void endVisit(IType e);
    /**
     * @param e package fragment element.
     */
    void endVisit(IPackageFragment e);
    /**
     * @param e type root element.
     */
    void endVisit(ITypeRoot e);
    /**
     * @param e
     */
    void endVisit(ILocalVariable e);
    /**
     * @param e
     */
    void endVisit(IPackageDeclaration e);
}
