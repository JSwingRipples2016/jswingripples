package org.incha.core.jswingripples.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomUtils;
import org.incha.core.jswingripples.MethodOverrideTester;
import org.incha.core.jswingripples.TypeHierarchySupport;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.TaskProgressMonitor;
import org.incha.ui.util.SubProgressMonitor;

public class Analyzer extends InteractiveTask {
    private static final Log log = LogFactory.getLog(Analyzer.class);

    private boolean accountForPolymorphism=false;
    private final TaskProgressMonitor monitor;
    private JSwingRipplesEIG eig;

    public Analyzer(final JSwingRipplesEIG eig, final TaskProgressMonitor mon) {
        this.monitor=mon;
        this.eig = eig;
        this.monitor.addThreadedTask(this); // tell the monitor to watch this thread
    }

    public Analyzer(final JSwingRipplesEIG eig, final TaskProgressMonitor mon, TaskListener listener) {
        this(eig, mon);
        this.listener = listener;
    }

    public void setAccountForPolymorphismMode(final boolean accountForPolymorphism) {
        this.accountForPolymorphism=accountForPolymorphism;
    }

    @Override
    public void run() {
        try {
            final ICompilationUnit[] units = JavaDomUtils.getCompilationUnits(eig.getJavaProject(), monitor);
            if (units.length > 0) {
                //initially add all nodes
                for (final ICompilationUnit u : units) {
                    final List<IJavaElement> allNodes = JavaDomUtils.getAllNodes(u);
                    for (final IJavaElement el : allNodes) {
                        if (el instanceof IMember) {
                            eig.addNode((IMember) el);
                        }
                    }
                }
                final Map<IMethod, HashSet<IMethod>> overrides = loadMembers(units);
                processEdges(units, overrides);
            }
        } catch (Exception e) {
            listener.taskFailure();
        }
        listener.taskSuccessful();
    }

    private Map<IMethod,HashSet <IMethod>> loadMembers(final ICompilationUnit[] units) {
        final Map<IMethod, HashSet<IMethod>> overridesMap = new HashMap<IMethod, HashSet<IMethod>>();

        try {
            monitor.setTaskName("Looking for classes in the project");
            final IType types[] = JavaDomUtils.getAllTypes(units);

            final TaskProgressMonitor typesMonitor=new SubProgressMonitor(monitor);

            final TypeHierarchySupport hierarchy = new TypeHierarchySupport(types);
            final MethodOverrideTester tester=new MethodOverrideTester(null, hierarchy);
            final HashSet <IMethod> overrides=new HashSet <IMethod>();

            try {
                typesMonitor.beginTask("Populating database", types.length);
                for (int i = 0; i < types.length; i++) {
                    final IType type = types[i];
                    if (!type.isBinary() && !(type.isMember())) {
                        typesMonitor.setTaskName("Process class " + type.getElementName());

                        final IMethod[] methods=type.getMethods();

                        final TaskProgressMonitor tmpMonitor = new SubProgressMonitor(typesMonitor);
                        tmpMonitor.beginTask("Process methods of "
                                + type.getFullyQualifiedName() , methods.length);

                        try {
                            for (int j= 0;j < methods.length;j++) {
                                tmpMonitor.setTaskName("Process method " + methods[j] + " of " + type.getElementName());

                                if (accountForPolymorphism) {
                                        if (hierarchy.getSuperclass(type) != null) {
                                            tester.setFocusType(type);
                                            tester.findAllDeclaringMethods(methods[j],true,overrides);

                                            tmpMonitor.worked(1);
                                            if (overrides.size()>0) {
                                                for (final IMethod m : overrides) {
                                                    if (!overridesMap.containsKey(m)) overridesMap.put(m, new HashSet<IMethod> ());
                                                    overridesMap.get(m).add(methods[j]);
                                                }
                                            }
                                            tmpMonitor.worked(2);
                                            overrides.clear();
                                        }
                                }

                                tmpMonitor.worked(j);
                            }
                        } finally {
                            tmpMonitor.done();
                        }

                    }
                    if (monitor.isCanceled()) {
                        return overridesMap;
                    }

                    typesMonitor.worked(i);
                }
            }
            finally {
                typesMonitor.done();
            }
        } catch (final Exception e) {
            log.error(e);
        }

        return overridesMap;
    }

    private void processEdges(final ICompilationUnit[] units,
            final Map<IMethod, HashSet<IMethod>> overrides) {
        final TaskProgressMonitor subMonitor= new SubProgressMonitor(JSwingRipplesApplication.getInstance().getProgressMonitor());
        try {
            subMonitor.beginTask("Analysing files",units.length);

            //----------------------
            for (int i=0;i<units.length;i++) {
                final ICompilationUnit unit = units[i];
                processEdges(unit, units, overrides);
                subMonitor.worked(i);
                subMonitor.setTaskName("Analyzing "+unit.getElementName()+" ("+ i +" out of "+units.length+")");

            }
            subMonitor.done();
        } catch (final Exception e) {
            log.error(e);
        }
    }

    /**
     * @param unit
     * @param units
     * @param overrides
     * @throws JavaModelException
     */
    private void processEdges(
            final ICompilationUnit unit,
            final ICompilationUnit[] units,
            final Map<IMethod, HashSet<IMethod>> overrides) throws JavaModelException {
        if (monitor.isCanceled()) {
            return;
        }

        final Set<Edge> edges = EdgesCalculator.getEdges(eig.getJavaProject(), unit, units);
        final Iterator<Edge> iter = edges.iterator();
        while (iter.hasNext()) {
            final Edge edge = iter.next();
            final IMember mem1= edge.getFrom();
            final IMember mem2= edge.getTo();
            final JSwingRipplesEIGNode node1= eig.getNode(mem1);
            final JSwingRipplesEIGNode node2= eig.getNode(mem2);

            if (node1!=null && node2!=null) {
                eig.addEdge(node1, node2);
                if (accountForPolymorphism)
                    if ((mem1 instanceof IMethod) && (mem2 instanceof IMethod)) {
                        if (overrides.containsKey(mem2))
                            for (final IMethod m : overrides.get(mem2)) {
                                eig.addEdge(node1, eig.getNode(m));
                            }
                    }
            }
        }
    }
}
