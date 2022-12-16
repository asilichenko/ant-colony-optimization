package ua.in.asilichenko.antcolony.domain;

/**
 * Probability of choosing the edge at some vertex.
 * Where "edge" represents vertex connected to the current vertex.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 11.12.2022
 */
public record Probability(double value, int edge) {
}
