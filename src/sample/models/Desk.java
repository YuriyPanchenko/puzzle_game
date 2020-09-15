package sample.models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
public class Desk extends Pane {
    public Desk(int numOfColumns, int numOfRows, int pieceSize) {
        setStyle("-fx-background-color: #cccccc; " +
                "-fx-border-color: #464646; " +
                "-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );");
        double DESK_WIDTH = pieceSize * numOfColumns;
        double DESK_HEIGHT = pieceSize * numOfRows;
        setPrefSize(DESK_WIDTH,DESK_HEIGHT);
        setMaxSize(DESK_WIDTH, DESK_HEIGHT);
        autosize();
        // create path for lines
        Path grid = new Path();
        grid.setStroke(Color.rgb(70, 70, 70));
        getChildren().add(grid);
        // create vertical lines
        for (int col = 0; col < numOfColumns - 1; col++) {
            grid.getElements().addAll(
                    new MoveTo(pieceSize + pieceSize * col, 5),
                    new LineTo(pieceSize + pieceSize * col, pieceSize * numOfRows - 5)
            );
        }

        // create horizontal lines
        for (int row = 0; row < numOfRows - 1; row++) {
            grid.getElements().addAll(
                    new MoveTo(5, pieceSize + pieceSize * row),
                    new LineTo(pieceSize * numOfColumns - 5, pieceSize + pieceSize * row)
            );
        }
    }
    @Override protected void layoutChildren() {}
}
