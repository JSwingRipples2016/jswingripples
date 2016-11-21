package org.incha.core.jswingripples;

import junit.framework.TestCase;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.simpledom.MockType;

public class TypeHierarchySupportTest extends TestCase {
    /**
     * Default constructor.
     */
    public TypeHierarchySupportTest() {
        super();
    }
    /**
     * @param name the test case name.
     */
    public TypeHierarchySupportTest(final String name) {
        super(name);
    }

    public void testGetSuperclass() throws JavaModelException {
        final MockType t1 = new MockType("a.b.c.T1");
        final MockType t2 = new MockType("a.b.c.T2");
        final MockType t3 = new MockType("a.b.c.T3");

        t1.setSuperClassName(t2.getFullyQualifiedName());

        final TypeHierarchySupport support = new TypeHierarchySupport(new IType[]{t1, t2, t3});

        assertNotNull(support.getSuperclass(t1));
        assertNull(support.getSuperclass(t2));
        assertNull(support.getSuperclass(t3));

    }
    public void testGetSuperInterface() throws JavaModelException {
        final MockType t1 = new MockType("a.b.c.T1");
        final MockType t2 = new MockType("a.b.c.T2");
        final MockType t3 = new MockType("a.b.c.T3");

        t1.setSuperInterfaceNames(new String[]{t2.getFullyQualifiedName(), "arbrakadabra"});

        final TypeHierarchySupport support = new TypeHierarchySupport(new IType[]{t1, t2, t3});

        final IType[] sup = support.getSuperInterfaces(t1);
        assertEquals(1, sup.length);
        assertEquals(t2.getFullyQualifiedName(), sup[0].getFullyQualifiedName());
    }
    public void testGetSuperSuperInterface() throws JavaModelException {
        final MockType t1 = new MockType("a.b.c.T1");
        final MockType t2 = new MockType("a.b.c.T2");
        final MockType t3 = new MockType("a.b.c.T3");
        final MockType t4 = new MockType("a.b.c.T3");

        t1.setSuperInterfaceNames(new String[]{t2.getFullyQualifiedName()});
        t2.setSuperInterfaceNames(new String[]{t3.getFullyQualifiedName()});

        final TypeHierarchySupport support = new TypeHierarchySupport(new IType[]{t1, t2, t3, t4});

        final IType[] sup = support.getSuperInterfaces(t1);
        assertEquals(2, sup.length);
    }
    public void testCiclicInterfaces() throws JavaModelException {
        final MockType t1 = new MockType("a.b.c.T1");
        final MockType t2 = new MockType("a.b.c.T2");
        final MockType t3 = new MockType("a.b.c.T3");
        final MockType t4 = new MockType("a.b.c.T3");

        t1.setSuperInterfaceNames(new String[]{t2.getFullyQualifiedName()});
        t2.setSuperInterfaceNames(new String[]{t3.getFullyQualifiedName()});
        t3.setSuperInterfaceNames(new String[]{t2.getFullyQualifiedName()});
        t3.setSuperInterfaceNames(new String[]{t1.getFullyQualifiedName()});

        final TypeHierarchySupport support = new TypeHierarchySupport(new IType[]{t1, t2, t3, t4});

        final IType[] sup = support.getSuperInterfaces(t1);
        assertEquals(2, sup.length);
    }
}
