package ua.in.asilichenko.antcolony.domain;

/**
 * Base class for storage information of pheromone levels on a graph.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 14.12.2022
 */
public abstract class PheromoneMatrix {

    /**
     * Evaporation speed.
     */
    protected final double rho;

    /**
     * @param rho evaporation speed
     */
    protected PheromoneMatrix(double rho) {
        this.rho = rho;
    }

    /**
     * Obtain current pheromone level on the edge.
     *
     * @param i vertex
     * @param j vertex
     * @return pheromones on (i, j) edge
     */
    public abstract double get(int i, int j);

    /**
     * Add some pheromones on the edge.
     *
     * @param i     vertex
     * @param j     vertex
     * @param value pheromone value to add
     */
    public abstract void add(int i, int j, double value);

    /**
     * Evaporate some pheromone.
     */
    public abstract void evaporate();
}
