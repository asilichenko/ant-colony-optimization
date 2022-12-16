package ua.in.asilichenko.antcolony.util;

import ua.in.asilichenko.antcolony.domain.Probability;
import ua.in.asilichenko.antcolony.service.AntColony;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 16.12.2022
 */
public class StringUtils {

    /**
     * Do not print too small values, epsilon is a lower bound.
     */
    private static final double EPSILON = 1e-3;

    /**
     * String format of pheromone values
     */
    public static final String PHEROMONE_FORMAT = "%.4f";

    /**
     * Calculate final probabilities for all edges of the graph.
     *
     * @param verticesNumber number of vertices on graph
     * @param antColony      ant colony that searched for the best path
     * @return string matrix of probabilities in "dd%" format
     */
    public static String obtainProbabilities(int verticesNumber, AntColony antColony) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < verticesNumber; i++) {
            final List<Integer> edges = new ArrayList<>();
            for (int j = 0; j < verticesNumber; j++) if (i != j) edges.add(j);
            final List<Probability> probabilities = antColony.calcProbabilities(i, edges);

            boolean isStubPrinted = true;
            for (Probability p : probabilities) {
                if (p.edge() > i && isStubPrinted) {
                    isStubPrinted = false;
                    stringBuilder.append(String.format(" %4s", "-"));
                }
                stringBuilder.append(String.format("%4d%%", (int) (p.value() * 100d)));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Stringify pheromone matrix.
     *
     * @param matrix pheromone matrix
     * @return pheromone matrix to print
     */
    public static String obtainPheromoneLevels(double[][] matrix) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (double[] line : matrix) {
            stringBuilder.append("{");
            for (int j = 0; j < line.length; j++) {
                //
                final double val = line[j];
                stringBuilder.append(String.format(PHEROMONE_FORMAT, val < EPSILON ? 0 : val));
                if (j < line.length - 1) stringBuilder.append(", ");
                //
            }
            stringBuilder.append("},\n");
        }

        return stringBuilder.toString();
    }
}
