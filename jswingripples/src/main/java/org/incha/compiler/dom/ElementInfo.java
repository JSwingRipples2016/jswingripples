package org.incha.compiler.dom;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Bundles an IJavaElement and an Object Info
 *
 */
public class ElementInfo {
    private final IJavaElement element;
    private final Object info;
    /**
     * @param element
     * @param info
     */
    public ElementInfo(IJavaElement element, Object info) {
        super();
        this.element = element;
        this.info = info;
    }

    /**
     * @return the element
     */
    public IJavaElement getElement() {
        return element;
    }
    /**
     * @return the info
     */
    public Object getInfo() {
        return info;
    }
}
