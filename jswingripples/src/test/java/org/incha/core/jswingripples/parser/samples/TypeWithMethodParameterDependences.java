package org.incha.core.jswingripples.parser.samples;

public class TypeWithMethodParameterDependences {
    /**
     * @param anyDependency any dependence.
     */
    public TypeWithMethodParameterDependences(final AnyDependency anyDependency) {
        super();
    }

    AnyDependency methodWithReturn() {
        return null;
    }
    void methodWithParameter(final AnyDependency dependency) {
    }
}
