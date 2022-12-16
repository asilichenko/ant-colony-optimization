package ua.in.asilichenko.antcolony.domain;

import java.util.List;

/**
 * An Ant that has traveled through the graph in search of the best path so found some path with some cost.
 * <p>
 * Copyright (C) 2022 Oleksii Sylichenko (a.silichenko@gmail.com)
 * <p>
 * License: LGPL-3.0-or-later
 *
 * @author Oleksii Sylichenko (a.silichenko@gmail.com)
 * Creation date: 08.12.2022
 */
public record Ant(List<Integer> path, long totalCost) {
}
