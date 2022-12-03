import java.awt.*;

public class Rectangle {
    private double x;
    private double y;
    private double halfWidth;
    private double halfHeight;
    private Color color;
    public Rectangle(double x, double y, double halfWidth, double halfHeight) {
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        color = Color.red;
    }

    public Rectangle(double x, double y, double halfWidth, double halfHeight, Color color) {
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHalfWidth() {
        return halfWidth;
    }

    public void setHalfWidth(double halfWidth) {
        this.halfWidth = halfWidth;
    }

    public double getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(double halfHeight) {
        this.halfHeight = halfHeight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean inBounds(double MouseX, double MouseY){
        return ((MouseX <= x + halfWidth) && (MouseX >= x - halfWidth) &&
                (MouseY <= y + halfHeight) && (MouseY >= y - halfHeight));
    }

    public boolean inBounds(){
        return inBounds(StdDraw.mouseX(), StdDraw.mouseY());
    }

    public void draw(){
        StdDraw.setPenColor(color);
        StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
    }

}

class Button extends Rectangle{
    private String text = "";

    public Button(double x, double y, double halfWidth, double halfHeight, String text) {
        super(x, y, halfWidth, halfHeight);
        this.text = text;
    }
    public Button(double x, double y, double halfWidth, double halfHeight, Color color, String text) {
        super(x, y, halfWidth, halfHeight, color);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(){
        super.draw();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(getX(), getY(), text);
    }

}