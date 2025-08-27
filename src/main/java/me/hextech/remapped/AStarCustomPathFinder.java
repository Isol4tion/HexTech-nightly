package me.hextech.remapped;

import java.util.Comparator;

public class AStarCustomPathFinder implements Comparator<AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl> {
   public int compare(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o1, AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl o2) {
      return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
   }
}
