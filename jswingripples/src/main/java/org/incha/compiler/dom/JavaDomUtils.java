package org.incha.compiler.dom;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.JavaProject;
import org.incha.ui.TaskProgressMonitor;
import org.incha.utils.FileVisitor;
import org.incha.utils.IoUtils;

/**
 *  Utilities para 
 *
 */
public final class JavaDomUtils {
	
	/**
     * Solamente tiene metodos estaticos, no deberia ser nunca instanseada
     */
    private JavaDomUtils() {}

    public static void visit(final IJavaElement e, final JavaDomVisitor v) throws JavaModelException {
        if(startJavaElement(v, e)) {
            if (e instanceof ICompilationUnit) {
                for (final IType type : ((ICompilationUnit) e).getTypes()) {
                    visit(type, v);
                }
            } else if (e instanceof IParent) {
                final IJavaElement[] childs = ((IParent) e).getChildren();
                for (final IJavaElement child : childs) {
                    visit(child, v);
                }
            }
        }
        endJavaElement(v, e);
    }

    /**
     * @param v visitor.
     * @param e current element.
     */
    private static boolean startJavaElement(final JavaDomVisitor v, final IJavaElement e) {
        if (e instanceof IImportContainer) {
            return v.startVisit((IImportContainer) e);
        } else if (e instanceof IPackageDeclaration){
            return v.startVisit((IPackageDeclaration) e);
        } else if (e instanceof ILocalVariable){
            return v.startVisit((ILocalVariable) e);
        } else if (e instanceof IField){
            return v.startVisit((IField) e);
        } else if (e instanceof IInitializer){
            return v.startVisit((IInitializer) e);
        } else if (e instanceof IMethod){
            return v.startVisit((IMethod) e);
        } else if (e instanceof IType){
            return v.startVisit((IType) e);
        } else if (e instanceof IPackageFragment){
            return v.startVisit((IPackageFragment) e);
        } else if (e instanceof ITypeRoot){
            return v.startVisit((ITypeRoot) e);
        }

        return false;
    }

    /**
     * @param v visitor.
     * @param e java element.
     */
    private static void endJavaElement(final JavaDomVisitor v, final IJavaElement e) {
        if (e instanceof IImportContainer) {
            v.endVisit((IImportContainer) e);
        } else if (e instanceof IPackageDeclaration){
            v.endVisit((IPackageDeclaration) e);
        } else if (e instanceof ILocalVariable){
            v.endVisit((ILocalVariable) e);
        } else if (e instanceof IField){
            v.endVisit((IField) e);
        } else if (e instanceof IInitializer){
            v.endVisit((IInitializer) e);
        } else if (e instanceof IMethod){
            v.endVisit((IMethod) e);
        } else if (e instanceof IType){
            v.endVisit((IType) e);
        } else if (e instanceof IPackageFragment){
            v.endVisit((IPackageFragment) e);
        } else if (e instanceof ITypeRoot){
            v.endVisit((ITypeRoot) e);
        }
    }
    public static ICompilationUnit[] getCompilationUnits(
            final JavaProject project, final TaskProgressMonitor monitor) throws IOException {
        final int numberOfFiles = getNumberOfSources(project);
        final JavaDomBuilder builder = new JavaDomBuilder(project.getName());
        monitor.beginTask("Compile project " + project.getName(), numberOfFiles);

        final List<ICompilationUnit> units = new LinkedList<ICompilationUnit>();

        try {
            final List<File> sources = project.getBuildPath().getSources();
            for (final File file : sources) {
                IoUtils.visitTo(file, new FileVisitor() {
                    @Override
                    public void exit(final File t) throws IOException {
                    }
                    @Override
                    public boolean enter(final File t) throws IOException {
                        if (monitor.isCanceled()) {
                            return false;
                        }
                        if (t.isFile() && t.getName().endsWith(".java")) {
                            units.add(builder.build(t, null));
                            monitor.worked(monitor.getProgress() + 1);
                        }
                        return true;
                    }
                });
            }
        } finally {
            monitor.done();
        }

        return units.toArray(new ICompilationUnit[units.size()]);
    }

    /**
     * @param project
     * @return
     * @throws IOException
     */
    private static int getNumberOfSources(final JavaProject project) throws IOException {
        final AtomicInteger i = new AtomicInteger();
        final List<File> sources = project.getBuildPath().getSources();
        for (final File file : sources) {
            IoUtils.visitTo(file, new FileVisitor() {
                @Override
                public void exit(final File t) throws IOException {
                }
                @Override
                public boolean enter(final File t) throws IOException {
                    if (t.isFile() && t.getName().endsWith(".java")) {
                        i.addAndGet(1);
                    }
                    return true;
                }
            });
        }
        return 0;
    }
    /**
     * @param units
     * @return
     * @throws JavaModelException
     */
    public static IType[] getAllTypes(final ICompilationUnit[] units) throws JavaModelException {
        final List<IType> types = new LinkedList<IType>();
        for (final ICompilationUnit unit : units) {
            for (final IType t : unit.getTypes()) {
                types.add(t);
            }
        }
        return types.toArray(new IType[types.size()]);
    }

    /**
     * @param type the type to test.
     * @param types type.
     * @return
     * @throws JavaModelException
     */
    public static boolean hasSupertype(final IType type, final IType[] types) throws JavaModelException {
        final String superTypeName = type.getSuperclassName();
        if (superTypeName != null) {
            for (final IType t : types) {
                if (superTypeName.equals(t.getFullyQualifiedName())) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Returns top declaring type of the IMember object by recursivly walking through the types that declare the object.
     * @param member
     *  IMember object to evaluate
     * @return
     *  Top declaring type of this object, or object itself if is top in the nesting hierarchy
     */
    public static IType getTopDeclaringType(final IMember member) {
        if (member==null) return null;
        if ((member.getDeclaringType()==null) && (member instanceof IType)) return (IType) member;

        IType type=member.getDeclaringType();

        while (type.getDeclaringType()!=null) {
            type=type.getDeclaringType();
        }

        return type;
    }

    /**
     * @param unit
     * @return
     * @throws JavaModelException
     */
    public static List<IJavaElement> getAllNodes(final ICompilationUnit unit) throws JavaModelException {
        final List<IJavaElement> result = new LinkedList<IJavaElement>();
        visit(unit, new JavaDomVisitorAdapter() {
            @Override
            public boolean startVisit(final IPackageDeclaration e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final ILocalVariable e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IPackageFragment e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IType e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IMethod e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IInitializer e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IField e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IImportContainer e) {
                result.add(e);
                return true;
            }
        });
        return result;
    }
    /**
     * @param unit
     * @return
     * @throws JavaModelException
     */
    public static List<IMember> getAllMembers(final ICompilationUnit unit) throws JavaModelException {
        final List<IMember> result = new LinkedList<IMember>();
        visit(unit, new JavaDomVisitorAdapter() {
            @Override
            public boolean startVisit(final IType e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IMethod e) {
                result.add(e);
                return true;
            }
            @Override
            public boolean startVisit(final IField e) {
                result.add(e);
                return true;
            }
        });
        return result;
    }
}
