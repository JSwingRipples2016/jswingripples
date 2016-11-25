package org.incha.compiler.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.JavaModelCache;

public class JavaModelCacheExt extends JavaModelCache {
    /**
     * Default constructor.
     */
    public JavaModelCacheExt() {
        super();
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.core.JavaModelCache#putInfo(org.eclipse.jdt.core.IJavaElement, java.lang.Object)
     */
    /**
     * Este cambio de visibilidad
     * es necesario ya que el metodo de org.eclipse.jdt es protected
     * y necesitamos ocuparlo
     * en JavaDomBuilder
     */
    @Override
    public void putInfo(final IJavaElement element, final Object info) {
        super.putInfo(element, info);
    }
}
