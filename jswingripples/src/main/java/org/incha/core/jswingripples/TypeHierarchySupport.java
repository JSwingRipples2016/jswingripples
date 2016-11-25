package org.incha.core.jswingripples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class TypeHierarchySupport {
    /**
     * The map of class name/type
     */
    private final Map<String, IType> typesMap = new HashMap<String, IType>();
    /**
     * The map of super classes and super interfaces.
     */
    private final Map<String, Set<String>> superInterfaces = new HashMap<String, Set<String>>();

    /**
     * Default constructor.
     * @throws JavaModelException
     */
    public TypeHierarchySupport(final IType[] types) throws JavaModelException {
        super();

        for (final IType type : types) {
            final String name = type.getFullyQualifiedName();
            typesMap.put(name, type);

            final Set<String> interfaces = new HashSet<String>();
            superInterfaces.put(name, interfaces);
            for (final String ifName : type.getSuperInterfaceNames()) {
                interfaces.add(ifName);
            }
        }
    }

    /**
     * @param type the type.
     * @return the super type.
     * @throws JavaModelException
     */
    public IType getSuperclass(final IType type) throws JavaModelException {
        return typesMap.get(type.getSuperclassName());
    }

    /**
     * @param type the type to inspect.
     * @return array of super interfaces.
     */
    public IType[] getSuperInterfaces(final IType type) {
        final String className = type.getFullyQualifiedName();
        final Set<String> tmp = new HashSet<String>(superInterfaces.get(className));
        final Set<String> interfaces = new HashSet<String>(superInterfaces.get(className));

        //resolve super/super/super dependencies
        while (!tmp.isEmpty()) {
            //get and remove first element.
            final Iterator<String> iter = tmp.iterator();
            final String currentInterface = iter.next();
            iter.remove();

            interfaces.add(currentInterface);

            //process super interfaces
            final Set<String> superSuper = superInterfaces.get(currentInterface);
            if (superSuper != null) {
                for (final String ss : superSuper) {
                    //not add self and not add already processed
                    if (!interfaces.contains(ss) && !ss.equals(className)) {
                        //add super super interfaces to processing
                        tmp.add(ss);
                    }
                }
            }
        }

        //get types by names.
        final List<IType> types = new LinkedList<IType>();
        for (final String ifc : interfaces) {
            final IType t = typesMap.get(ifc);
            if (t != null) {
                types.add(t);
            }
        }
        return types.toArray(new IType[types.size()]);
    }
}
