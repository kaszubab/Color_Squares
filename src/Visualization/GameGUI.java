package Visualization;

import Map.GameMap;
import Map.GameTile;
import Map.Vector2D;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameGUI implements IMapObserver{

    private GameMap map;
    private Pane root;

    public GameGUI(GameMap map) {
        this.map = map;
        visualize();
        map.addObserver(this);
    }

    @Override
    public void positionChanged(Vector2D position, int player) {

        Tile tile1 = new Tile(1200 / this.map.getWidth(), 800 / this.map.getHeight(), playerToColor(player));
        tile1.setTranslateX(position.x * 1200 / this.map.getWidth());
        tile1.setTranslateY(position.y * 800 / this.map.getHeight());

        this.root.getChildren().set(position.x * map.getWidth() + position.y , tile1);
    }

    public Pane getVisualization() {
        return root;
    }

    private Color playerToColor(int player) {
        switch (player) {
            case 0:
                return Color.RED;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.PINK;
        }
        return null;
    }

    public void occupiedTile (Vector2D tile) {
        Tile tile1 = new Tile(1200 / this.map.getWidth(), 800 / this.map.getHeight(), Color.GREY);
        tile1.setTranslateX(tile.x * 1200 / this.map.getWidth());
        tile1.setTranslateY(tile.y * 800 / this.map.getHeight());

        this.root.getChildren().set(tile.x * map.getWidth() + tile.y , tile1);
    }


    private void visualize() {

        Pane root = new Pane();
        root.setPrefSize(1200, 800);



        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                Tile tile;
                tile = new Tile(800 / this.map.getWidth(), 800 / this.map.getHeight(), Color.WHITE);

                tile.setTranslateX(i * 800 / this.map.getWidth());
                tile.setTranslateY(j * 800 / this.map.getHeight());
                root.getChildren().add(tile);
            }
        }


        this.root =  root;
    }

    private class Tile extends StackPane {
        public Tile(int x, int y, Color color) {
            Rectangle border = new Rectangle(x, y);
            border.setFill(color);

            border.setStroke(Color.BLACK);
            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }
    }


}
