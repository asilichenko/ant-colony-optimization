package ua.in.asilichenko.antcolony.cost;

/**
 * A cost function that stores all costs in a matrix.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 14.12.2022
 */
public class MatrixCostFunction extends CostFunction {

    /**
     * Cost matrix.
     */
    private final long[][] matrix;

    /**
     * @param matrix cost matrix
     */
    public MatrixCostFunction(long[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public long cost(int i, int j) {
        return matrix[i][j];
    }
}
