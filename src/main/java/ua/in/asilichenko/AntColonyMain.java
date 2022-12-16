package ua.in.asilichenko;

import com.google.common.base.Stopwatch;
import ua.in.asilichenko.antcolony.cost.MatrixCostFunction;
import ua.in.asilichenko.antcolony.domain.Ant;
import ua.in.asilichenko.antcolony.domain.ArrayPheromoneMatrix;
import ua.in.asilichenko.antcolony.samples.Sample;
import ua.in.asilichenko.antcolony.service.AntColony;
import ua.in.asilichenko.antcolony.service.AntColonyWithCostFunction;
import ua.in.asilichenko.antcolony.util.StringUtils;

import java.time.LocalTime;

import static ua.in.asilichenko.antcolony.samples.Sample.*;

/**
 * Main class for launching Ant Colony search samples.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 */
public class AntColonyMain {

    /**
     * @param threads    number of ants in parallel
     * @param iterations number of iterations with parallel ants
     * @param alpha      pheromone influence
     * @param beta       edge cost influence
     * @param rho        pheromone evaporation speed
     */
    record Config(int threads, long iterations, double alpha, double beta, double rho) {

    }

    @SuppressWarnings("CommentedOutCode")
    public static void main(String[] args) {
        System.out.println(LocalTime.now() + " >>> Started");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        sample5();
//        sample6();
//        sample17();
//        sample26();

        System.out.println(LocalTime.now() + " >>> Finished. Elapsed: " + stopwatch.elapsed());
    }

    @SuppressWarnings("unused")
    private static void sample5() {
        search(SAMPLE_5,
                new Config(
                        3,
                        5,

                        10,
                        0.001,
                        0.8
                )
        );
    }

    @SuppressWarnings("unused")
    private static void sample6() {
        search(SAMPLE_6,
                new Config(
                        10,
                        10,

                        10,
                        0,
                        0.6
                )
        );
    }

    @SuppressWarnings("unused")
    private static void sample17() {
        search(SAMPLE_17,
                new Config(
                        1000,
                        1000,

                        1.6,
                        0.4,
                        0.006
                )
        );
    }

    @SuppressWarnings("unused")
    private static void sample26() {
        search(SAMPLE_26,
                new Config(
                        1000,
                        1000,

                        1.6,
                        0.4,
                        0.006
                )
        );
    }

    private static void search(Sample sample, Config config) {
        final long[][] costMtx = sample.costMtx();
        final double[][] pheromoneMtx = new double[costMtx.length][costMtx.length];

        final AntColony antColony = new AntColonyWithCostFunction(
                new MatrixCostFunction(costMtx),
                config.alpha,
                config.beta,
                new ArrayPheromoneMatrix(pheromoneMtx, config.rho),
                costMtx.length);

        final Ant bestAnt = antColony.search(config.threads, config.iterations);
        final String probabilities = StringUtils.obtainProbabilities(costMtx.length, antColony);
        final String pheromoneMtxString = StringUtils.obtainPheromoneLevels(pheromoneMtx);

        System.out.println();
        System.out.println(bestAnt.path() + "\t" + bestAnt.totalCost());
        System.out.println("Expected: " + sample.expected());
        System.out.println();
        System.out.println(probabilities);
        System.out.println();
        System.out.println(pheromoneMtxString);
    }
}