package model;

import java.awt.*;
import java.util.Random;

public class Circle {
    private int x;
    private int y;
    private int position;
    private int radius;
    private Color color;
    private static final Random random = new Random();


    public Circle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
    }

    public Circle(int x, int y, int radius, int colorRGB) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = new Color(colorRGB);
    }

    public Circle(int x, int y, int radius, int r, int g, int b, int position) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = new Color(r,g,b);
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }

    public boolean containsPoint(int i, int j) {
        return (this.x - i) * (this.x - i) + (this.y - j) * (this.y - j) <= (this.radius * this.radius);
    }

}
