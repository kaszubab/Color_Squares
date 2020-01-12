package Map;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class  GameMap {

    private Vector2D leftLowerCorner;
    private Vector2D rightUpperCorner;
    private List<IMapObserver> observerList;

    private List<Vector2D> [] playerTiles;
    private int [] playersTileCount;



    private GameTile [][] tiles;

    public GameMap(int width,int height, int playerQuantity, double obstacleRatio) {
        this.observerList = new LinkedList<>();
        this.leftLowerCorner = new Vector2D(0,0);
        this.rightUpperCorner = new Vector2D(width-1, height-1);
        tiles = new GameTile[height][width];


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new GameTile();
            }
        }

        this.playersTileCount = new int[playerQuantity];
        this.playerTiles = new List[playerQuantity];

        for (int i = 0; i < playerQuantity; i++) {
            this.playersTileCount[i] = 0;
            this.playerTiles[i] = new LinkedList<>();
        }

        Random random = new Random();
        for (int i = 0; i < (this.rightUpperCorner.x + 1) * (this.rightUpperCorner.y + 1);) {

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

    public void placePlayer(Vector2D playerPosition, int playerNumber) {
        this.playersTileCount[playerNumber]++;
        this.playerTiles[playerNumber].add(playerPosition);
    }

    private boolean inMap(Vector2D position) {
        return position.follows(this.leftLowerCorner) && position.precedes(this.rightUpperCorner);
    }



}
