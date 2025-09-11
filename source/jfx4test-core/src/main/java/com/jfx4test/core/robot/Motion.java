package com.jfx4test.framework.robot;


/**
 * Enumeration holding the three simplest types of motion between two 2D points
 * a = (x₁, y₁) and b = (x₂, y₂). Given any two points in the plane we can construct
 * a right-angled triangle where the hypotenuse is the straight-line between a and b.
 * <p>
 * <pre><code>
 * +-----------------------→ +x
 * |    d        b
 * |    |        *
 * |    |      * *
 * |    |    *   *
 * |    |  *     *
 * |    |*       *
 * |    **********
 * |    a        c
 * |
 * v
 * +y
 * </code></pre>
 * <p>
 * Traveling in a straight-line between a and b (that is, tracing the hypotenuse) is
 * {@code DIRECT}. Traveling first from a to c and then from c to b is {@code HORIZONTAL_FIRST}.
 * Traveling first from a to d and then from d to b is {@code VERTICAL_FIRST}. {@code DEFAULT}
 * means that no specific type of motion was explicitly requested.
 */
public enum Motion {
    DEFAULT,
    DIRECT,
    HORIZONTAL_FIRST,
    VERTICAL_FIRST,
}

