package model;

import java.awt.*;
import java.util.Random;

public class Circle {
    private int x;
    private int y;
    private static final int RADIUS = 100;
    private Color color;
    private static final Random random = new Random();


    public Circle(int x, int y,  Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Circle random(){
        int x = Circle.random.nextInt();
        int y = Circle.random.nextInt();

        return new Circle(x,y, Circle.getRandomColor());
    }

    public static Color getRandomColor(){
        int r = Circle.random.nextInt(255);
        int g = Circle.random.nextInt(255);
        int b = Circle.random.nextInt(255);

        return new Color(r,g,b);
    }

    public boolean containsPoint(int i, int j){
        return (this.x - i) * (this.x - i) + (this.y - j) * (this.y - j) <= (Circle.RADIUS * Circle.RADIUS);
    }

}
