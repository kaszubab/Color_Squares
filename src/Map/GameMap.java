package Map;

import Visualization.GameGUI;
import Visualization.IMapObserver;

import java.util.*;

public class  GameMap {

    private Vector2D leftLowerCorner;
    private Vector2D rightUpperCorner;
    private List<IMapObserver> observerList;

    private ArrayList<List<Vector2D>> playerTiles;
    private int [] playersTileCount;
    private int playerCount;

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

        this.playerCount = playerQuantity;
        this.playersTileCount = new int[playerQuantity];
        this.playerTiles =  new ArrayList<>();


        for (int i = 0; i < playerQuantity; i++) {
            this.playersTileCount[i] = 0;
            this.playerTiles.add(new LinkedList<>());
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
        if (observer instanceof GameGUI) {
            for (int i = 0; i < this.getWidth(); i++) {
                for (int j = 0; j < this.getHeight(); j++) {
                    if (this.tiles[i][j].isObstacle()) {
                        ((GameGUI) observer).occupiedTile(new Vector2D(i,j));
                    }
                }
            }
        }
    }

    public void removeObserver(IMapObserver observer) {
        observerList.remove(observer);
    }

    private void positionChanged(Vector2D position, int player) {
        for (IMapObserver x : observerList) {
            x.positionChanged(position, player);
        }
    }

    private void colorNeighbouringSquares(Vector2D square, int player, LinkedList<Vector2D> tilesToAdd) {
        if (inMap(square.add(MapDirection.NORTH.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.NORTH.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                positionChanged(newPosition,player);
                tilesToAdd.add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.EAST.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.EAST.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                positionChanged(newPosition,player);
                tilesToAdd.add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.SOUTH.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.SOUTH.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                positionChanged(newPosition,player);
                tilesToAdd.add(newPosition);
            }
        }
        if (inMap(square.add(MapDirection.WEST.toUnitVector()))) {
            Vector2D newPosition = square.add(MapDirection.WEST.toUnitVector());
            if (tiles[newPosition.x][newPosition.y].occupy(player)) {
                playersTileCount[player]++;
                positionChanged(newPosition,player);
                tilesToAdd.add(newPosition);
            }
        }
    }

    private void expandTerritory(int player) {
        LinkedList<Vector2D> tilesToAdd = new LinkedList<>();

        this.playerTiles.get(player).forEach(k -> {
            colorNeighbouringSquares(k, player, tilesToAdd);
        });

        this.playerTiles.set(player, new LinkedList<>());

        tilesToAdd.forEach(k -> this.playerTiles.get(player).add(k));
    }

    public void makeSingleTurn() {
        List<Integer> playerOrder = new ArrayList<>(this.playerCount);
        for (int i = 0; i < this.playerCount; i++) {
            playerOrder.add(i);
        }
        Collections.shuffle(playerOrder);

        for (int i = 0; i < this.playerCount; i++) {
            expandTerritory(playerOrder.get(i));
        }


    }

    public boolean isGameFinished() {
        boolean finished = true;

        for (List<Vector2D> k : playerTiles) {
            if (k.size() != 0) finished=false;
        }

        return finished;
    }

    public int getWidth() {
        return this.rightUpperCorner.x+1;
    }

    public int getHeight() {
        return this.rightUpperCorner.y+1;
    }


    public void placePlayer(Vector2D playerPosition, int playerNumber) {
        this.playersTileCount[playerNumber]++;
        this.playerTiles.get(playerNumber).add(playerPosition);
        positionChanged(playerPosition, playerNumber);
    }

    private boolean inMap(Vector2D position) {
        return position.follows(this.leftLowerCorner) && position.precedes(this.rightUpperCorner);
    }



}
