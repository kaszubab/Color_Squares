package Map;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class  GameMap {

    private Vector2D leftLowerCorner;
    private Vector2D rightUpperCorner;
    private List<IMapObserver> observerList;

    private ArrayList<List<Vector2D>> playerTiles;
    private int [] playersTileCount;
    private GameTile [][] tiles;

    public GameMap(int width,int height, int playerQuantity, double obstacleRatio) {
        this.observerList = new LinkedList<>();
        this.leftLowerCorner = new Vector2D(0,0);
        this.rightUpperCorner = new Vector2D(width-1, height-1);
        tiles = new GameTile[width][height];


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new GameTile();
            }
        }

        this.playersTileCount = new int[playerQuantity];
        this.playerTiles =  new ArrayList<>(playerQuantity);

        for (int i = 0; i < playerQuantity; i++) {
            this.playersTileCount[i] = 0;
            this.playerTiles.set(i, new LinkedList<>());
        }

        Random random = new Random();
        for (int i = 0; i < (this.rightUpperCorner.x + 1) * (this.rightUpperCorner.y + 1) * obstacleRatio;) {
            Vector2D position = new Vector2D(random.nextInt(this.rightUpperCorner.x+1), random.nextInt(this.rightUpperCorner.y+1));
            if (!this.tiles[position.x][position.y].isObstacle()) {
                this.tiles[position.x][position.y].setAsObstacle();
                i++;
            }
        }

    }
    public void addObserver(IMapObserver observer) {
        this.observerList.add(observer);
    }

    public void removeObserver(IMapObserver observer) {
        observerList.remove(observer);
    }

    private void positionChanged(Vector2D oldPosition) {
        for (IMapObserver x : observerList) {
            x.positionChanged( );
        }
    }

    private void colorNeighbouringSquares(Vector2D square, int player) {
        if (inMap(square.add(MapDirection.NORTH.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.NORTH.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                playerTiles.get(player).add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.EAST.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.EAST.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                playerTiles.get(player).add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.SOUTH.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.SOUTH.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                playerTiles.get(player).add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.WEST.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.WEST.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                playerTiles.get(player).add(newPosition);
            }
        }
    }

    private void expandTerritory(int player) {

        LinkedList<Vector2D> tilesToRemove = new LinkedList<>();

        this.playerTiles.get(player).forEach(k -> {
            colorNeighbouringSquares(k, player);
            tilesToRemove.add(k);
        });

        tilesToRemove.forEach(k -> this.playerTiles.get(player).remove(k));
    }

    public void placePlayer(Vector2D playerPosition, int playerNumber) {
        this.playersTileCount[playerNumber]++;
        this.playerTiles.get(playerNumber).add(playerPosition);
    }

    private boolean inMap(Vector2D position) {
        return position.follows(this.leftLowerCorner) && position.precedes(this.rightUpperCorner);
    }



}
