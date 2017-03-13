package com.atlassian.clover.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a single class or it's equivalent (an interface, trait etc).
 *
 * Implemented interfaces:
 * <ul>
 *     <li>SourceInfo - represents content of the whole class</li>
 *     <li>HasClasses, HasMethods, HasStatements - code entities which can be declared inside a class
 *     (on the lop level)</li>
 *     <li>HasContextFilter - set of custom statement/method contexts for filtering</li>
 *     <li>HasMetrics, HasAggregatedMetrics - code metrics for the class</li>
 *     <li>HasParent - parent class/method/file for this class</li>
 * </ul>
 */
public interface ClassInfo extends
        SourceInfo, EntityContainer,
        HasClasses, HasMethods, HasStatements,
        HasContextFilter, HasMetrics, HasAggregatedMetrics, HasParent {

    @Override
    String getName();

    String getQualifiedName();

    ModifiersInfo getModifiers();

    /**
     * Returns a class in which this class is declared (case for inner classes) or <code>null</code> otherwise.
     *
     * @return ClassInfo containing class or <code>null</code>
     */
    @Nullable
    ClassInfo getContainingClass();

    /**
     * Returns a method in which this class (an anonymous inline class for instance) is declared or <code>null</code>
     * otherwise.
     *
     * @return MethodInfo containing method or <code>null</code>
     */
    @Nullable
    MethodInfo getContainingMethod();

    /**
     * Returns a file in which this class is declared.
     *
     * @return FileInfo file containing this class
     */
    @Nullable
    FileInfo getContainingFile();

    boolean isTestClass();

    /**
     * Returns list of inner classes declared on the top level of the class. It does not return classes declared inside
     * other inner classes, i.e nested more than one level. Exact content depends on a programming language, e.g.:
     *
     * <ul>
     *     <li>Java and Groovy - it does not return anonymous inline classes (they're treated like statements)</li>
     *     <li>Scala - t.b.d. </li>
     * </ul>
     *
     * @return List&lt;? extends ClassInfo&gt; - list of classes or empty list if none
     */
    @Override
    @NotNull
    List<? extends ClassInfo> getClasses();

    /**
     * Returns list of methods declared on the top level of the class. It does not return methods declared in nested
     * classes or methods declared inside other methods etc. Exact content depends on a programming language, e.g.:
     *
     * <ul>
     *     <li>Java - traditional methods plus lambda functions assigned to a field (java 8 or above)</li>
     *     <li>Groovy - methods, extra methods generated by groovyc (getters and setters for instance)</li>
     *     <li>Scala - t.b.d. </li>
     * </ul>
     *
     * @return List&lt;? extends MethodInfo&gt; - list of methods or empty list if none
     */
    @Override
    @NotNull
    List<? extends MethodInfo> getMethods();

    /**
     * Returns list of statements declared on the to level of the class, i.e. outside methods. It does not apply to all
     * programming languages:
     * <ul>
     *     <li>Java - not applicable, code put in static initializer blocks is treated as a part of a constructor</li>
     *     <li>Groovy - not applicable</li>
     *     <li>Scala - t.b.d.</li>
     * </ul>
     *
     * @return List&lt;? extends StatementInfo&gt; - list of statements or empty list if none
     */
    @Override
    @NotNull
    List<? extends StatementInfo> getStatements();

    /**
     * Returns true if this class does not contain any nested entities (method or inner classes)
     *
     * @return boolean - true if getMethods() is empty and getClasses() is empty
     */
    boolean isEmpty();

    PackageInfo getPackage();
}