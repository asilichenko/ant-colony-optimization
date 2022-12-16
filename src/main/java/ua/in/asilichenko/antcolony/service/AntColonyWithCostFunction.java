package ua.in.asilichenko.antcolony.service;

import ua.in.asilichenko.antcolony.cost.CostFunction;
import ua.in.asilichenko.antcolony.domain.PheromoneMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Ant Colony implementation using external cost measurer or container.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 10.12.2022
 */
public class AntColonyWithCostFunction extends AntColony {

    /**
     * Edge cost measurer or storage.
     */
    private final CostFunction costFunction;

    /**
     * @param costFunction    cost measurer
     * @param alpha           pheromone influence
     * @param beta            edge cost influence
     * @param pheromoneMatrix pheromone matrix
     * @param pathSize        number of vertices that must be visited at the path
     */
    public AntColonyWithCostFunction(
            CostFunction costFunction,
            double alpha, double beta,
            PheromoneMatrix pheromoneMatrix,
            int pathSize) {
        super(alpha, beta, pheromoneMatrix, pathSize);
        this.costFunction = costFunction;
    }

    @Override
    protected List<Integer> obtainEdges(int i, List<Integer> path) {
        final List<Integer> retval = new ArrayList<>();
        for (int j = 0; j < pathSize; j++) if (j != i && !path.contains(j)) retval.add(j);
        return retval;
    }

    @Override
    protected long cost(int i, int j) {
        return costFunction.cost(i, j);
    }
}
