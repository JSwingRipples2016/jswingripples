package org.incha.core.jswingripples.parser.samples;

public class TypeWithLocalVariableDependencies {
    static {
        new StaticInitializerDependency();
    }
    {
        new InitializerDependency();
    }

    /**
     *
     */
    public TypeWithLocalVariableDependencies() {
        super();
        //initialization dependency
        new InitializationInConstructorDependency();
    }
    public TypeWithLocalVariableDependencies(final int param) {
        //declaration dependency
        @SuppressWarnings("unused")
        final DeclarationInConstructorDependency declarationDependency;
    }

    void methodWithDeclarationDependency() {
        @SuppressWarnings("unused")
        final DeclarationInMethodDependency declarationDependency;
    }
    void methodWithInitializationDependency() {
        //initialization dependency
        new InitializationInMethodDependency();
    }
}
