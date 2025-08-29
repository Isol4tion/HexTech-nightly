package me.hextech.api.utils.path;

import java.util.Comparator;

/*
 * Exception performing whole class analysis ignored.
 */
public class AStarCustomPathFinder
        implements Comparator<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> {
    @Override
    public int compare(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o1, AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o2) {
        return (int) (o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
    }
}
