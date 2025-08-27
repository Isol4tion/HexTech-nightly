package me.hextech.remapped;

import java.util.Comparator;
import me.hextech.remapped.AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl;

/*
 * Exception performing whole class analysis ignored.
 */
public class AStarCustomPathFinder
implements Comparator<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> {
    @Override
    public int compare(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o1, AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o2) {
        return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
    }
}
