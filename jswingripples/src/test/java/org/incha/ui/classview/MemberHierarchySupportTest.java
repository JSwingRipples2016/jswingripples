package org.incha.ui.classview;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.incha.TestUtils;
import org.incha.compiler.dom.JavaDomBuilder;
import org.incha.compiler.dom.JavaDomUtils;
import org.incha.compiler.dom.JavaDomVisitorAdapter;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.util.NullMonitor;

public class MemberHierarchySupportTest extends TestCase {
    private List<JSwingRipplesEIGNode> members = new LinkedList<JSwingRipplesEIGNode>();
    private final JSwingRipplesEIG eig = new JSwingRipplesEIG(new JavaProject("test"));

    //named member
    public static class TypeChildForTest {
        public int field;
        public void method() {}
    }
    //anonymous memeber
    public Runnable anotymousMember = new Runnable() {
        @Override
        public void run() {}
    };

    /**
     * Default constructor.
     */
    public MemberHierarchySupportTest() {
        super();
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final File sourcePath = TestUtils.findSourceForClass(MemberHierarchySupportTest.class);

        final JavaDomBuilder builder = new JavaDomBuilder("test");
        final ICompilationUnit unit = builder.build(sourcePath, new NullMonitor());

        //get all members from compilation unit
        JavaDomUtils.visit(unit, new JavaDomVisitorAdapter(){
            @Override
            public boolean startVisit(final IField e) {
                visitIMember(e);
                return true;
            }
            /* (non-Javadoc)
             * @see org.incha.compiler.dom.JavaDomVisitorAdapter#startVisit(org.eclipse.jdt.core.IMethod)
             */
            @Override
            public boolean startVisit(final IMethod e) {
                visitIMember(e);
                return true;
            }
            /* (non-Javadoc)
             * @see org.incha.compiler.dom.JavaDomVisitorAdapter#startVisit(org.eclipse.jdt.core.IType)
             */
            @Override
            public boolean startVisit(final IType e) {
                visitIMember(e);
                return true;
            }
            private void visitIMember(final IMember member) {
                final JSwingRipplesEIGNode node = new JSwingRipplesEIGNode(eig, member);
                members.add(node);
            }
        });
    }
    /**
     * @param name test case name.
     */
    public MemberHierarchySupportTest(final String name) {
        super(name);
    }


    public void testGetRootTypes() {
        final MemberHierarchySupport support = new MemberHierarchySupport(members);
        final JSwingRipplesEIGNode[] roots = support.getRootTypes();
        assertEquals(1, roots.length);
        assertEquals(MemberHierarchySupportTest.class.getName(),
                ((IType) roots[0].getNodeIMember()).getFullyQualifiedName());
    }
    /**
     * Tests get parent method.
     */
    public void testGetParent() {
        final MemberHierarchySupport support = new MemberHierarchySupport(members);
        final JSwingRipplesEIGNode[] roots = support.getRootTypes();
        final JSwingRipplesEIGNode root = roots[0];
        assertNull(support.getParent(root));

        final JSwingRipplesEIGNode[] children = support.getChildren(root);
        assertTrue(root == support.getParent(children[0]));
    }
    /**
     * Tests get depth method.
     */
    public void testGetDepth() {
        final MemberHierarchySupport support = new MemberHierarchySupport(members);
        final JSwingRipplesEIGNode typeChildForTest = getMemberByName("TypeChildForTest");
        final JSwingRipplesEIGNode methodRun = getMemberByName("run");
        final JSwingRipplesEIGNode method = getMemberByName("method");

        assertEquals(0, support.getHierarchyDepth(support.getRootTypes()[0]));
        assertEquals(1, support.getHierarchyDepth(typeChildForTest));
        assertEquals(3, support.getHierarchyDepth(methodRun));
        assertEquals(2, support.getHierarchyDepth(method));
    }
    /**
     * @param string
     * @return
     */
    private JSwingRipplesEIGNode getMemberByName(final String string) {
        final MemberHierarchySupport support = new MemberHierarchySupport(members);
        for (final JSwingRipplesEIGNode m : support.getMembers()) {
            if (string.equals(m.getNodeIMember().getElementName())) {
                return m;
            }
        }
        return null;
    }
}
