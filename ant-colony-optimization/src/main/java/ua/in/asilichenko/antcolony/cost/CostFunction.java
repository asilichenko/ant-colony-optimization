package ua.in.asilichenko.antcolony.cost;

/**
 * A function that stores or calculates a cost of edges.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 08.12.2022
 */
public abstract class CostFunction {

    /**
     * Obtain a cost of the edge.
     *
     * @param i vertex
     * @param j vertex
     * @return cost of (i, j) edge
     */
    public abstract long cost(int i, int j);
}
