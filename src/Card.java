import javax.swing.*;

public class Card{

    private Suits suit;
    private int value;
    private int colour;
    private int style = 1;
    private boolean isFaceUp = false;
    private ImageIcon image;
    private ImageIcon faceDownImage;

    private double x;
    private double y;
    private double w;
    private double h;

    private boolean isFocused = false;
    private String tableLocation = "stock";

    public String getTableLocation() {
        return tableLocation;
    }

    public void setTableLocation(String tableLocation) {
        this.tableLocation = tableLocation;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public void setLocation(double x, double y, double w, double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ImageIcon getImage() {
        if (isFaceUp) {
            return image;
        } else {
            return faceDownImage;
        }
    }

    public Card(Suits suit, int value, int colour) {
        this.suit = suit;
        this.value = value;
        this.colour = colour;
        setImage(suit,value);

        faceDownImage = new ImageIcon("images/backs/card_back_red.png");
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    public Suits getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    private void setImage(Suits suit, int value){
        char suitKey = suit.toString().charAt(0);
        this.image = new ImageIcon("images/style1/"+value+suitKey+".png");
    }

    public void switchStyle() {
        style = style == 1 ? 2 : 1;
        image = new ImageIcon("images/style" + style + "/" + value + suit.toString().charAt(0) + ".png");
    }

    public int getColour() {
        return colour;
    }

    public void setFaceDownImage(String colour) {
        faceDownImage = new ImageIcon("images/backs/card_back_" + colour + ".png");
    }
}
