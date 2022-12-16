package ua.in.asilichenko.antcolony.service;

import ua.in.asilichenko.antcolony.domain.Ant;
import ua.in.asilichenko.antcolony.domain.PheromoneMatrix;
import ua.in.asilichenko.antcolony.domain.Probability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Ant Colony Optimization base class
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 08.12.2022
 */
public abstract class AntColony {

    /**
     * Influence of pheromone level.
     */
    protected final double alpha;

    /**
     * Influence of edge cost.
     */
    protected final double beta;

    /**
     * Pheromone matrix.
     */
    protected final PheromoneMatrix pheromoneMatrix;

    /**
     * Path must contain such number of vertices.
     */
    protected final int pathSize;

    /**
     * Ant travelled by the best (shortest) path.
     */
    private Ant bestAnt = null;

    /**
     * @param alpha           pheromone influence
     * @param beta            cost influence
     * @param pheromoneMatrix pheromone matrix
     * @param pathSize        path must contain such number of vertices
     */
    public AntColony(double alpha, double beta, PheromoneMatrix pheromoneMatrix, int pathSize) {
        this.alpha = alpha;
        this.beta = beta;
        this.pheromoneMatrix = pheromoneMatrix;
        this.pathSize = pathSize;
    }

    /**
     * Calculate denominator of probability equation.
     *
     * @param i     current vertex
     * @param edges linked vertices (not visited)
     * @return denominator value
     */
    protected double calcTauEtaSum(int i, List<Integer> edges) {
        double retval = 0;
        for (int j : edges) retval += tauEta(i, j);
        return retval;
    }

    /**
     * Calculate probabilities for linked vertices.
     *
     * @param i     current vertex
     * @param edges linked vertices (not visited)
     * @return list of probabilities
     */
    public List<Probability> calcProbabilities(int i, List<Integer> edges) {
        final List<Probability> retval = new ArrayList<>();
        final double linkedEdgesTauEtaSum = calcTauEtaSum(i, edges);
        for (int j : edges) retval.add(new Probability(tauEta(i, j) / linkedEdgesTauEtaSum, j));
        return retval;
    }

    /**
     * Send a single ant through a graph.
     *
     * @return ant returned from a trip
     */
    public Ant trace() {
        final List<Integer> path = new ArrayList<>();
        long totalCost = 0;

        path.add(0);
        for (int step = 0, i = 0; step < pathSize - 1; step++) {
            //
            final List<Integer> edges = obtainEdges(i, path);
            final List<Probability> probabilities = calcProbabilities(i, edges);

            final List<Probability> cumulativeSum = cumulativeSum(probabilities);
            final double random = ThreadLocalRandom.current().nextDouble();

            int prevI = i;
            for (Probability prob : cumulativeSum) {
                if (random < prob.value()) {
                    i = prob.edge();
                    break;
                }
            }
            path.add(i);
            totalCost += cost(prevI, i);
            //
        }
        path.add(0);
        totalCost += cost(path.get(path.size() - 2), 0);
        return new Ant(path, totalCost);
    }

    /**
     * Obtain vertices which are connected with current except visited ones.
     *
     * @param i    relative vertex
     * @param path visited vertices
     * @return list of vertices connected with vertex i except visited
     */
    protected abstract List<Integer> obtainEdges(int i, List<Integer> path);

    /**
     * Cost (length) of the edge (i, j).
     *
     * @param i vertex
     * @param j vertex
     * @return cost (length) of the edge
     */
    protected abstract long cost(int i, int j);

    /**
     * Pheromone influence.
     *
     * @param pheromone level of pheromone on an edge
     * @return influence of pheromone on an edge
     */
    protected double tau(double pheromone) {
        return Math.pow(pheromone, alpha);
    }

    /**
     * Quality of edge/full path by its cost.
     *
     * @param cost cost of edge or full path
     * @return quality of such cost
     */
    protected double quality(long cost) {
        return 1d / cost;
    }

    /**
     * Influence of edge cost.
     *
     * @param cost edge cost
     * @return influence of edge cost
     */
    protected double eta(long cost) {
        return Math.pow(quality(cost), beta);
    }

    /**
     * Multiplication of Tau and Eta, resulting quality of edge according to pheromone level and cost.
     *
     * @param i current vertex
     * @param j linked vertex
     * @return total quality of the edge (i, j)
     */
    protected double tauEta(int i, int j) {
        return tau(pheromoneMatrix.get(i, j)) * eta(cost(i, j));
    }

    /**
     * Calculate cumulative sum for edges.
     *
     * @param probabilities probabilities of linked (not visited) edges
     * @return cumulative sum for edges
     */
    protected List<Probability> cumulativeSum(List<Probability> probabilities) {
        final List<Probability> retval = new ArrayList<>();

        double sum = 0;
        for (Probability entry : probabilities) retval.add(new Probability(sum += entry.value(), entry.edge()));

        return retval;
    }

    /**
     * Update pheromone level on all edges according to ant trails.
     *
     * @param ants ants after their travels.
     */
    protected void addPheromone(List<Ant> ants) {
        pheromoneMatrix.evaporate();
        for (Ant ant : ants) {
            final double quality = quality(ant.totalCost());
            final List<Integer> path = ant.path();
            for (int i = 0, j = 1; j < path.size() - 1; i = j++) pheromoneMatrix.add(path.get(i), path.get(j), quality);
        }
    }

    /**
     * Test if testCost < bestCost.
     *
     * @param testCost cost of new ant is pretended to be the best
     * @param bestCost current the best cost
     * @return cost comparision
     */
    protected int compare(long testCost, long bestCost) {
        return Long.compare(testCost, bestCost);
    }

    /**
     * Test if new ant is better than current the best.
     *
     * @param ant new ant pretended to be the best
     */
    private synchronized void claimNewBest(Ant ant) {
        if (null == bestAnt || compare(ant.totalCost(), bestAnt.totalCost()) < 0) bestAnt = ant;
    }

    /**
     * Launch the Ant Colony to find the best (shortest) path.
     *
     * @param threads    number of parallel threads (ants during one iteration).
     * @param iterations the number of iterations during which the colony will launch a bunch of ants to search
     * @return ant found the best (shortest) path
     */
    public Ant search(int threads, long iterations) {
        for (long i = 0; i < iterations; i++) {
            final List<Ant> ants = Collections.synchronizedList(new ArrayList<>());
            IntStream.range(0, threads).parallel().forEach(k -> {
                final Ant ant = trace();
                ants.add(ant);
                claimNewBest(ant);
            });
            addPheromone(ants);
        }
        return bestAnt;
    }
}
