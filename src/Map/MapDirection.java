package Map;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;


    public String toString()
    {
        switch (this) {
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
        }
        return null;
    }

    public static MapDirection intToMapDirection(int val) {
        switch (val) {
            case 0: return NORTH;
            case 1: return EAST;
            case 2: return SOUTH;
            case 3: return WEST;
        }
        return null;
    }

    public MapDirection next() {
        switch (this) {
            case EAST: return SOUTH;
            case WEST: return NORTH;
            case NORTH: return EAST;
            case SOUTH: return WEST;
        }
        return null;
    }

    public MapDirection previous() {
        switch (this) {
            case EAST: return NORTH;
            case WEST: return SOUTH;
            case NORTH: return WEST;
            case SOUTH: return EAST;
        }
        return null;
    }

    public Vector2D toUnitVector() {
        switch (this) {
            case EAST: return new Vector2D(1, 0);
            case WEST: return new Vector2D(-1, 0);
            case NORTH: return new Vector2D(0, 1);
            case SOUTH: return new Vector2D(0, -1);
        }
        return null;
    }
}
