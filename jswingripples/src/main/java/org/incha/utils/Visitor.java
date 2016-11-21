package org.incha.utils;


/**
 * @param <T> The type of elements in recursive storage.
 */
public interface Visitor <T> {
    /**
     * Enter to hierarchical object.
     * @param t the object to visit.
     * @return true of should stop recursion.
     * @throws Exception the exception of entering to given element.
     */
    boolean enter(T t) throws Exception;
    /**
     * Exit from hierarchical object.
     * @param t the object to visit.
     * @throws Exception the exception of entering to given element.
     */
    void exit(T t) throws Exception;
}
