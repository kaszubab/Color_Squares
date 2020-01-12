import Visualization.GameGUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import Map.Vector2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import Map.GameMap;


public class Game extends Application {
    public static void main(String [] args) {

        launch(args);

    }


    @Override
    public void start(Stage primaryStage) {


        Pane root = new Pane();
        root.setPrefSize(1200, 800);

        Stage initialStage = new Stage();
        initialStage.setTitle("Welcome to Color Squares");
        GameMap map = initMap(20, 20, 4, 0.1);
        GameGUI GUI = new GameGUI(map);

        map.placePlayer(new Vector2D(0,0), 0);
        map.placePlayer(new Vector2D(10,0), 1);
        map.placePlayer(new Vector2D(0,10), 2);
        map.placePlayer(new Vector2D(10,10), 3);


        initialStage.setScene(new Scene(root));
        initialStage.show();


    }



    private GameMap initMap(int width, int height, int playerQuantity, double obstacleRatio) {
        return new GameMap(width, height, playerQuantity, obstacleRatio);
    }

}

