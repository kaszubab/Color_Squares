import Visualization.GameGUI;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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


    GameMap map;

    private Timeline timeline1 = new Timeline();


    @Override
    public void start(Stage primaryStage) {


        Pane root = new Pane();
        root.setPrefSize(400, 300);


        Stage initialStage = new Stage();
        initialStage.setTitle("Welcome to Color Squares");


        VBox box1 = new VBox();
        box1.setPrefWidth(root.getPrefWidth());


        box1.setSpacing(20);

        TextField field1 = new TextField();
        field1.setPromptText("Enter number of players (between 2 and 4)");

        TextField field2 = new TextField();
        field2.setPromptText("Enter width of the map");

        TextField field3 = new TextField();
        field3.setPromptText("Enter height of the map");

        TextField field4 = new TextField();
        field4.setPromptText("Set percentage of obstacles");

        Button button1 = new Button();
        button1.setPrefSize(box1.getPrefWidth(), 100);
        button1.setText("Submit");

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ((field1.getText() != null && !field1.getText().isEmpty())
                        && (field2.getText() != null && !field2.getText().isEmpty())
                        && (field3.getText() != null && !field3.getText().isEmpty())
                        && (field4.getText() != null && !field4.getText().isEmpty())
                        ) {
                    initialStage.close();
                    Timeline timeline1 = initMap(Integer.parseInt(field2.getText()),
                            Integer.parseInt(field3.getText()), Integer.parseInt(field1.getText()),
                            Double.parseDouble(field4.getText()), root);

                    timeline1.setCycleCount(Timeline.INDEFINITE);
                    timeline1.play();


                }
            }
        });

        box1.getChildren().add(field1);
        box1.getChildren().add(field2);
        box1.getChildren().add(field3);
        box1.getChildren().add(field4);
        box1.getChildren().add(button1);

        root.getChildren().add(box1);


        initialStage.setScene(new Scene(root));
        initialStage.show();







    }


    private class MyTimer extends AnimationTimer {

        private long time;

        @Override
        public void handle(long now) {

            if(now - time  >= 1000L) {
                time = now;
                doHandle();

            }

        }

        private void doHandle() {
            map.makeSingleTurn();
            System.out.println("dzialam");
        }
    }



    private Timeline initMap(int width, int height, int playerQuantity, double obstacleRatio, Pane root) {

        GameMap map = new GameMap(width, height, playerQuantity, obstacleRatio);

        this.map = map;
        GameGUI GUI = new GameGUI(map);

        map.placePlayer(new Vector2D(0,0), 0);
        map.placePlayer(new Vector2D(10,0), 1);
        map.placePlayer(new Vector2D(0,10), 2);
        map.placePlayer(new Vector2D(10,10), 3);

        root = GUI.getVisualization();

        AtomicInteger counter1 = new AtomicInteger();



        timeline1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                map.makeSingleTurn();
                if (map.isGameFinished()) timeline1.stop();
            }
        }));

        Stage oneGenerator = new Stage();

        oneGenerator.setScene(new Scene(root));
        oneGenerator.show();

        return timeline1;
    }

}

