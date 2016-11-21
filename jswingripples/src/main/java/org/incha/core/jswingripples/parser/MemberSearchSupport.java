package org.incha.core.jswingripples.parser;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;

public class MemberSearchSupport {
    /**
     * Compilation unit.
     */
    private final ICompilationUnit unit;

    /**
     * @param unit compilation unit.
     */
    public MemberSearchSupport(final ICompilationUnit unit) {
        this.unit = unit;
    }

    public IMember getOwnerMember(final int offset, final int len) throws JavaModelException {
        for (final IJavaElement e : unit.getChildren()) {
            if (e instanceof IMember) {
                final IMember owner = getOwnerMember((IMember) e, offset, len);
                if (owner != null) {
                    return owner;
                }
            }
        }
        return null;
    }

    /**
     * @param member current owner.
     * @param offset offset.
     * @param len length.
     * @return
     * @throws JavaModelException
     */
    private IMember getOwnerMember(final IMember member, final int offset, final int len)
            throws JavaModelException {
        if (contains(member, offset, len)) {
            for (final IJavaElement e : member.getChildren()) {
                if (e instanceof IMember) {
                    final IMember m = getOwnerMember((IMember) e, offset, len);
                    if (m != null) {
                        return m;
                    }
                }
            }
            return member;
        }
        return null;
    }
    /**
     * @param m
     * @param offset
     * @param len
     * @return
     * @throws JavaModelException
     */
    private boolean contains(final IMember m, final int offset, final int len) throws JavaModelException {
        final ISourceRange range = m.getSourceRange();
        return range.getOffset() <= offset && range.getOffset() + range.getLength() >= offset + len;
    }
}
