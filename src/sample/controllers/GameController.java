package sample.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import sample.models.Center;
import sample.models.Desk;
import sample.models.Piece;

import javax.imageio.ImageIO;

public class GameController {

    private Image image = new Image("sample/images/defaultPicture.jpg");
    private Timeline timeline;
    private ArrayList<Image> piecesToSave;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button selectImageButton;

    @FXML
    private Label selectImageLabel;

    @FXML
    private ImageView selectedImage;

    @FXML
    private Button normalLevelButton;

    @FXML
    private Button easyLevelButton;

    @FXML
    private Button hardLevelButton;

    @FXML
    private Label chooseLevelLabel;

    @FXML
    private VBox vBox;

    @FXML
    private Button shuffleButton;

    @FXML
    private Button checkResultButton;

    @FXML
    private Button savePiecesButton;

    @FXML
    private Button exitGameButton;


    @FXML
    void initialize() {
        this.selectImageLabel.setText("Choose other picture");
        this.selectImageButton.setVisible(true);
        this.hardLevelButton.setVisible(true);
        this.easyLevelButton.setVisible(true);
        this.normalLevelButton.setVisible(true);
        this.chooseLevelLabel.setVisible(true);
        this.shuffleButton.setVisible(false);
        this.checkResultButton.setVisible(false);
        this.savePiecesButton.setVisible(false);
        this.exitGameButton.setVisible(false);
        this.vBox.setVisible(false);
        easyLevelButton.setOnAction(event -> createGame(3));
        normalLevelButton.setOnAction(event -> createGame(5));
        hardLevelButton.setOnAction(event -> createGame(7));

        selectImageButton.setOnAction(event -> {
            Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG =
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg =
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG =
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
            //Show open file dialog
            File file = fileChooser.showOpenDialog(stage);
            //cropping image
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                this.image = SwingFXUtils.toFXImage(bufferedImage, null);
                if(image.getHeight() > vBox.getHeight() || image.getWidth() > vBox.getWidth()){
                    int newHeight = (int) image.getHeight();
                    int newWidth = (int) image.getWidth();
                    if(image. getHeight() > vBox.getHeight())
                        newHeight = (int) vBox.getHeight();
                    if(image.getWidth() > vBox.getWidth())
                        newWidth = (int) vBox.getWidth();
                    PixelReader pixelReader = image.getPixelReader();
                    WritableImage newImage = new WritableImage(pixelReader, newWidth, newHeight);
                    this.image = newImage;
                }
                selectedImage.setImage(this.image);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    private void createGame(int minNumberOfPiecesInLine){
        this.selectImageLabel.setText("Example");
        this.selectImageButton.setVisible(false);
        this.hardLevelButton.setVisible(false);
        this.easyLevelButton.setVisible(false);
        this.normalLevelButton.setVisible(false);
        this.chooseLevelLabel.setVisible(false);
        this.shuffleButton.setVisible(true);
        this.checkResultButton.setVisible(true);
        this.savePiecesButton.setVisible(true);
        this.exitGameButton.setVisible(true);
        this.vBox.setVisible(true);
        int numberOfColumns;
        int numberOfRows;
        int pieceSize;
        if(image.getHeight() > image.getWidth()){
            numberOfColumns = minNumberOfPiecesInLine;
            numberOfRows = (int) (image.getHeight()*minNumberOfPiecesInLine/image.getWidth());
            pieceSize = (int) image.getWidth()/minNumberOfPiecesInLine;
        }
        else {
            numberOfRows = minNumberOfPiecesInLine;
            numberOfColumns = (int) (image.getWidth()*minNumberOfPiecesInLine/image.getHeight());
            pieceSize = (int) image.getHeight()/minNumberOfPiecesInLine;
        }
        //create dest for pieces
        Desk desk = new Desk(numberOfColumns, numberOfRows, pieceSize);
        vBox.setVisible(true);
        vBox.getChildren().addAll(desk);
        final List<Piece> pieces  = new ArrayList<>();
        final ArrayList<Image> piecesView = new ArrayList<>();
        //split image to pieces
        for (int col = 0; col < numberOfColumns; col++) {
            for (int row = 0; row < numberOfRows; row++) {
                int x = col * pieceSize;
                int y = row * pieceSize;
                WritableImage clip = new WritableImage(image.getPixelReader(), x, y, pieceSize, pieceSize);
                piecesView.add(clip);
                Piece.centerSet.add(new Center(x, y, true));
                final Piece piece = new Piece(pieceSize, image, x, y,
                        desk.getWidth(), desk.getHeight());
                pieces.add(piece);
            }
        }
        this.piecesToSave = piecesView;
        desk.getChildren().addAll(pieces);
        //shuffle all pieces
        shuffleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (timeline != null) timeline.stop();
                timeline = new Timeline();
                for (final Piece piece : pieces) {
                    double shuffleX = Math.random() *
                            (desk.getWidth() - pieceSize + 48 ) -
                            24 - piece.getCorrectX();
                    double shuffleY = Math.random() *
                            (desk.getHeight() - pieceSize + 30 ) -
                            15 - piece.getCorrectY();
                    timeline.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1),
                                    new KeyValue(piece.translateXProperty(), shuffleX),
                                    new KeyValue(piece.translateYProperty(), shuffleY)));
                    int position = getRandomNumber(0, 3);
                    for(int i = 0; i < position; i++)
                        piece.changePosition();
                }
                for (Center center: Piece.centerSet){
                    center.setActive(false);
                }
                timeline.playFromStart();
            }
        });
        //check correct data
        checkResultButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label secondLabel = new Label("You win!!!");
                for(Piece piece: pieces){
                    if((piece.getTranslateX() != 0 || piece.getTranslateY() != 0)
                            || (piece.getRotation() != 0)) {
                        secondLabel.setText("You lose(");
                    }
                }
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Result");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(200);
                newWindow.setY(100);

                newWindow.show();
            }
        });

        savePiecesButton.setOnAction(event -> {
            Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stage);
            String path = selectedDirectory.getAbsolutePath();
            int i = 0;
            for (Image image: piecesToSave){
                saveImage(image, path, i);
                i++;
            }
        });

        exitGameButton.setOnAction(event -> {
            vBox.getChildren().remove(desk);
            initialize();
        });
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static void saveImage(Image image, String path, int number) {
        File outputFile = new File(path+ "\\image" + Integer.toString(number) + ".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}