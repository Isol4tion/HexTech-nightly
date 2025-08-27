package me.hextech.remapped;

import java.util.ArrayList;
import me.hextech.remapped.Vec3;

/*
 * Exception performing whole class analysis ignored.
 */
public class AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl {
    private Vec3 loc;
    private AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl parent;
    private ArrayList<Vec3> path;
    private double squareDistanceToFromTarget;
    private double cost;
    private double totalCost;

    public AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl(Vec3 loc, AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
        this.loc = loc;
        this.parent = parent;
        this.path = path;
        this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        this.cost = cost;
        this.totalCost = totalCost;
    }

    public Vec3 getLoc() {
        return this.loc;
    }

    public void setLoc(Vec3 loc) {
        this.loc = loc;
    }

    public AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl getParent() {
        return this.parent;
    }

    public void setParent(AStarCustomPathFinder_GIsiOdvkuKbgFcjrKqrl parent) {
        this.parent = parent;
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void setPath(ArrayList<Vec3> path) {
        this.path = path;
    }

    public double getSquareDistanceToFromTarget() {
        return this.squareDistanceToFromTarget;
    }

    public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
        this.squareDistanceToFromTarget = squareDistanceToFromTarget;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
