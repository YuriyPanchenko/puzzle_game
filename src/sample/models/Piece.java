package sample.models;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.*;

public class Piece extends Parent {
    public int size;
    private final double correctX;
    private final double correctY;
    private double startDragX;
    private double startDragY;
    private Shape pieceStroke;
    private Shape pieceClip;
    private ImageView imageView = new ImageView();
    private Point2D dragAnchor;
    public static ArrayList<Center> centerSet= new ArrayList<>();
    private int rotation = 0;

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Piece(int size, Image image, final double correctX, final double correctY,
                 final double deskWidth, final double deskHeight) {
        this.size = size;
        this.correctX = correctX;
        this.correctY = correctY;
        // configure clip
        pieceClip = createPiece();
        pieceClip.setFill(Color.WHITE);
        pieceClip.setStroke(null);
        // add a stroke
        pieceStroke = createPiece();
        pieceStroke.setFill(null);
        pieceStroke.setStroke(Color.BLACK);
        // create image view
        imageView.setImage(image);
        imageView.setClip(pieceClip);
        setFocusTraversable(true);
        getChildren().addAll(imageView, pieceStroke);
        // turn on caching so the jigsaw piece is fasr to draw when dragging
        setCache(true);
        // start in inactive state
        /*setInactive();*/
        // add listeners to support dragging
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton mouseButton = event.getButton();
                if(event.getButton() == MouseButton.SECONDARY)
                {
                    changePosition();
                }
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Iterator iterator = Piece.centerSet.iterator();
                while (iterator.hasNext()){
                    Center center = (Center) iterator.next();
                    if((center.getxCoordinate() == getTranslateX() + correctX)
                    && (center.getyCoordinate() == getTranslateY() + correctY))
                        center.setActive(false);
                }
                toFront();
                startDragX = getTranslateX();
                startDragY = getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());

            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Iterator iterator = Piece.centerSet.iterator();
                while (iterator.hasNext()){
                    Center currentCenter = (Center) iterator.next();
                    if (getTranslateX() < (currentCenter.getxCoordinate() + 25 - correctX) &&
                            getTranslateX() > (currentCenter.getxCoordinate() - 25 - correctX) &&
                            getTranslateY() < (currentCenter.getyCoordinate() + 25 - correctY)
                            && getTranslateY() > (currentCenter.getyCoordinate() - 25 - correctY)
                            && currentCenter.isActive() == false) {
                        setTranslateX(currentCenter.getxCoordinate() - correctX);
                        setTranslateY(currentCenter.getyCoordinate() - correctY);
                        currentCenter.setActive(true);
                    }
                }

            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                double newTranslateX = startDragX
                        + me.getSceneX() - dragAnchor.getX();
                double newTranslateY = startDragY
                        + me.getSceneY() - dragAnchor.getY();
                double minTranslateX = - 245f - correctX;
                double maxTranslateX = (deskWidth - size + 250f ) - correctX;
                double minTranslateY = - 230f - correctY;
                double maxTranslateY = (deskHeight - size + 270f ) - correctY;
                if ((newTranslateX> minTranslateX ) &&
                        (newTranslateX< maxTranslateX) &&
                        (newTranslateY> minTranslateY) &&
                        (newTranslateY< maxTranslateY)) {
                    setTranslateX(newTranslateX);
                    setTranslateY(newTranslateY);
                }
            }
        });
    }

    public void changePosition() {
        //Use this Circle to help turn piece
        Circle circle = new Circle(5);
        circle.setFill(Color.RED);
        circle.setCenterX(correctX + size/2);
        circle.setCenterY(correctY + size/2);

        //Add the Rotate to the ImageView's Transforms
        Rotate rotate = new Rotate();
        rotate.setPivotX(circle.getCenterX());//Set the Pivot's X to be the same location as the Circle's X. This is only used to help you see the Pivot's point
        rotate.setPivotY(circle.getCenterY());//Set the Pivot's Y to be the same location as the Circle's Y. This is only used to help you see the Pivot's point
        imageView.getTransforms().add(rotate);//Add the Rotate to the ImageView
        rotate.setAngle(rotate.getAngle() + 90);
        rotation = (rotation+1)%4;
    }

    private Shape createPiece() {
        Shape shape = createPieceRectangle();
        shape.setTranslateX(correctX);
        shape.setTranslateY(correctY);
        return shape;
    }

    private Rectangle createPieceRectangle() {
        Rectangle rec = new Rectangle();
        rec.setWidth(size);
        rec.setHeight(size);
        return rec;
    }

    public double getCorrectX() { return correctX; }

    public double getCorrectY() { return correctY; }

}
