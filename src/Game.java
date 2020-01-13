import Visualization.GameGUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import java.util.concurrent.atomic.AtomicInteger;

import Map.GameMap;


public class Game extends Application {
    public static void main(String [] args) {

        launch(args);

    }


    private int currentPlayer;

    private GameSettings game;

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

        HBox hb = new HBox();
        hb.setPrefWidth(box1.getPrefWidth());

        Button button1 = new Button();
        button1.setPrefSize(box1.getPrefWidth()/2, 100);
        button1.setText("Play Normal Game");

        Button button2 = new Button();
        button2.setPrefSize(box1.getPrefWidth()/2, 100);
        button2.setText("Play Randomized Game");

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ((field1.getText() != null && !field1.getText().isEmpty())
                        && (field2.getText() != null && !field2.getText().isEmpty())
                        && (field3.getText() != null && !field3.getText().isEmpty())
                        && (field4.getText() != null && !field4.getText().isEmpty())
                        ) {

                    initialStage.close();

                    game = new GameSettings(Integer.parseInt(field1.getText()),
                            Integer.parseInt(field2.getText()),
                            Integer.parseInt(field3.getText()),
                            Double.parseDouble(field4.getText()),
                            false);

                    Timeline timeline1 = initMap(game, root);

                    timeline1.setCycleCount(Timeline.INDEFINITE);

                    timeline1.play();



                }
            }
        });


        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ((field1.getText() != null && !field1.getText().isEmpty())
                        && (field2.getText() != null && !field2.getText().isEmpty())
                        && (field3.getText() != null && !field3.getText().isEmpty())
                        && (field4.getText() != null && !field4.getText().isEmpty())
                        ) {

                    initialStage.close();

                    game = new GameSettings(Integer.parseInt(field1.getText()),
                            Integer.parseInt(field2.getText()),
                            Integer.parseInt(field3.getText()),
                            Double.parseDouble(field4.getText()),
                            true);

                    Timeline timeline1 = initMap(game, root);

                    timeline1.setCycleCount(Timeline.INDEFINITE);

                    timeline1.play();



                }
            }
        });



        hb.getChildren().add(button1);
        hb.getChildren().add(button2);

        box1.getChildren().add(field1);
        box1.getChildren().add(field2);
        box1.getChildren().add(field3);
        box1.getChildren().add(field4);
        box1.getChildren().add(hb);

        root.getChildren().add(box1);


        initialStage.setScene(new Scene(root));
        initialStage.show();


    }



    private Timeline initMap(GameSettings game, Pane root) {

        GameMap map = new GameMap(game.mapWidth, game.mapHeight, game.playerCount, game.obstacleRatio);

        GameGUI GUI = new GameGUI(map);

        root = GUI.getVisualization( );



        AtomicInteger counter1 = new AtomicInteger();


        Stage oneGenerator = new Stage();


        timeline1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (currentPlayer == game.playerCount) {

                    map.makeSingleTurn();

                    if (map.isGameFinished()) {

                        timeline1.stop();


                        scoreScene(map, oneGenerator, GUI);

                    }
                }
            }
        }));


        oneGenerator.setScene(new Scene(root));

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getSceneX();
                double y = event.getSceneY();

                if (!GUI.insertPlayerAtPosition(x, y, currentPlayer)) {
                    oneGenerator.setTitle("Wrong position");
                }
                else {
                        currentPlayer++;
                }

            }
        });


        oneGenerator.show();

        return timeline1;
    }


    private void scoreScene(GameMap map, Stage gameStage, GameGUI GUI) {

        Pane root = new Pane();
        root.setPrefSize(400, 300);

        Stage newStage = new Stage();
        newStage.setTitle("Scores");


        VBox box1 = new VBox();
        box1.setPrefWidth(root.getPrefWidth());


        box1.setSpacing(20);

        for (int i = 0; i < currentPlayer; i++) {
            Text field1 = new Text();
            field1.setText("Player " + i + " : " + map.getPlayerTilesCount(i));
            field1.setFont(Font.font("Comic Sans",20));
            field1.setFill(GUI.playerToColor(i));
            box1.getChildren().add(field1);
        }

        Button button1 = new Button();
        button1.setPrefSize(box1.getPrefWidth(), 100);
        button1.setText("Replay");


        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                gameStage.close();
                newStage.close();

                currentPlayer = 0;

                Timeline timeline1 = initMap(game, root);

                timeline1.setCycleCount(Timeline.INDEFINITE);

                timeline1.play();

            }
        });

        box1.getChildren().add(button1);

        root.getChildren().add(box1);


        newStage.setScene(new Scene(root));
        newStage.show();

    }

    private class GameSettings {

        public int playerCount;
        public int mapWidth;
        public int mapHeight;
        public double obstacleRatio;
        public boolean randomized;

        GameSettings(int pC, int W, int H, double O, boolean randomized) {
            this.playerCount = pC;
            this.mapWidth = W;
            this.mapHeight = H;
            this.obstacleRatio = O;
            this.randomized = randomized;
        }
    }

}

