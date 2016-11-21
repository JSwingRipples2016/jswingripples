package org.incha.compiler.dom;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.SourceElementParser;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.core.CompilationUnitElementInfo;
import org.eclipse.jdt.internal.core.CompilationUnitStructureRequestor;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.incha.ui.TaskProgressMonitor;
import org.incha.ui.util.NullMonitor;
import org.incha.ui.util.SubProgressMonitor;

/**
 * Parser and builder of tree of java elements
 *
 */
public class JavaDomBuilder {
    private final CompilerOptions options;
    private final String javaProject;
    private static JavaModelCacheExt cache = initializeJavaModelManager();

    /**
     *
     */
    public JavaDomBuilder(final String javaProject, final String javaLevel) {
        super();
        //set compiler options.
        final Map<?, ?> opts = JavaCore.getOptions();
        JavaCore.setComplianceOptions(javaLevel, opts);
        options = new CompilerOptions(opts);
        this.javaProject = javaProject;
    }
    
    /**
     * Factory method to create the dom builder
     * @param file file to parse.
     */
    public JavaDomBuilder(final String javaProject) {
        this(javaProject, JavaCore.VERSION_1_7);
    }
    
    /**
     * Builds the ICompilationUnit updating the progressMonitor
     * @param file The file
     * @param progressMonitor progress monitor.
     * @return the type.
     */
    public ICompilationUnit build(final File file, final TaskProgressMonitor progressMonitor) {
        final TaskProgressMonitor subMonitor = progressMonitor == null
                ? new NullMonitor() : new SubProgressMonitor(progressMonitor);
        subMonitor.beginTask("Compile file " + file.getName(), 1);
        try {
            return build(file);
        } finally {
            subMonitor.done();
        }
    }
    
    /**
     * Builds the ICompilationUnit
     * @param file
     * @return the type
     */
    public ICompilationUnit build(final File file) {
        final SimpleCompilationUnit u = new SimpleCompilationUnit(javaProject, file);

        final SourceElementParser sourceParser = new SourceElementParser(null, new DefaultProblemFactory(),
                options, true, false);

        final Map<Object, Object> localInfo = new HashMap<Object, Object>();
        final CompilationUnitStructureRequestor requester = new CompilationUnitStructureRequestor(u,
                new CompilationUnitElementInfo(), localInfo){
            {
                this.parser = sourceParser;
            }
        };
        sourceParser.setRequestor(requester);
        sourceParser.parseCompilationUnit(u, true, new NullProgressMonitor());

        //search and set compiled element name to simple package fragment.
        final IPackageDeclaration p = searchCompiledPackageFragment(localInfo);
        u.setPackageName(p.getElementName());

        //merge compilation info
        for (final Map.Entry<Object, Object> e : localInfo.entrySet()) {
            if (e.getKey() instanceof IJavaElement) {
                cache.putInfo((IJavaElement) e.getKey(), e.getValue());
            }
        }

        //set roots to compilation unit
        final IJavaElement[] types = findAllChildren(localInfo.keySet());
        u.setChildren(types);

        return u;
    }
    /**
     * @param keySet
     * @return
     */
    private IJavaElement[] findAllChildren(final Set<Object> keySet) {
        final List<IJavaElement> list = new LinkedList<IJavaElement>();
        for (final Object object : keySet) {
            final IJavaElement e = (IJavaElement) object;
            if (e.getParent() instanceof ICompilationUnit) {
                list.add(e);
            }
        }
        return list.toArray(new IJavaElement[list.size()]);
    }
    /**
     * @param map
     * @return
     */
    private IPackageDeclaration searchCompiledPackageFragment(
            final Map<Object, Object> map) {
        for (final Object obj : map.keySet()) {
            if (obj instanceof IPackageDeclaration) {
                return (IPackageDeclaration) obj;
            }
        }
        return null;
    }
    
    /**
     * Ocupamos reflexion para cambiar en JavaModelManager el field de cache.
     * No es ideal ya que es reflection, pero no se encontro otra forma
     * @return
     */
   private static JavaModelCacheExt initializeJavaModelManager() {
       try {
           // set up cache
           final Field cacheField = JavaModelManager.class.getDeclaredField("cache");
           cacheField.setAccessible(true);
           final JavaModelCacheExt cache = new JavaModelCacheExt();
           cacheField.set(JavaModelManager.getJavaModelManager(), cache);
           return cache;
       } catch (final Exception e) {
           e.printStackTrace();
           return null;
        }
    }
}
