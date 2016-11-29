package org.incha.core.jswingripples.parser.samples;

public class TypeWithMethodInfovation {
    @SuppressWarnings("null")
    void invokeMethod() {
        final Object obj = null;
        //this is call only for
        ((TypeWithLocalVariableDependencies) obj).methodWithDeclarationDependency();
    }
}
