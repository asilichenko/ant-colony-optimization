package ua.in.asilichenko.antcolony.domain;

/**
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 14.12.2022
 */
public class ArrayPheromoneMatrix extends PheromoneMatrix {

    private final double[][] matrix;

    @SuppressWarnings("unused")
    public ArrayPheromoneMatrix(double[][] matrix) {
        this(matrix, 0);
    }

    @SuppressWarnings("unused")
    public ArrayPheromoneMatrix(double[][] matrix, double rho) {
        super(rho);
        this.matrix = matrix;
        initMatrix();
    }

    /**
     * Setup initial pheromone level on all edges.
     */
    private void initMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j) continue;
                matrix[i][j] = 1;
            }
        }
    }

    @Override
    public double get(int i, int j) {
        return matrix[i][j];
    }

    @Override
    public void add(int i, int j, double value) {
        matrix[i][j] += value;
    }

    @Override
    public void evaporate() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] *= (1 - rho);
            }
        }
    }
}
