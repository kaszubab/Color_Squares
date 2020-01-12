package Visualization;

import Map.Vector2D;

public interface IMapObserver {
    void positionChanged(Vector2D square, int player);
}