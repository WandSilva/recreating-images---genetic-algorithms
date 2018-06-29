package model;

import java.awt.*;
import java.util.Random;

public class Circle {
    private  boolean display;
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

    public Circle(int x, int y, int radius, int r, int g, int b, int position, boolean display) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = new Color(r, g, b);
        this.position = position;
        this.display = display;
    }

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }

    public boolean isActive(){
        return display;
    }

    public boolean containsPoint(int i, int j) {
        return (this.x - i) * (this.x - i) + (this.y - j) * (this.y - j) <= (this.radius * this.radius);
    }

    public double areaOfIntersection(Circle c) {
        int x0 = this.x;
        int y0 = this.y;
        int r0 = this.radius;

        int x1 = c.x;
        int y1 = c.y;
        int r1 = c.radius;

        int rr0 = r0 * r0;
        int rr1 = r1 * r1;
        double d = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));

        // Circles do not overlap
        if (d > r1 + r0) {
            return 0;
        }

        // Circle1 is completely inside circle0
        else if (d <= Math.abs(r0 - r1) && r0 >= r1) {
            // Return area of circle1
            return Math.PI * rr1;
        }

        // Circle0 is completely inside circle1
        else if (d <= Math.abs(r0 - r1) && r0 < r1) {
            // Return area of circle0
            return Math.PI * rr0;
        }

        // Circles partially overlap
        else {
            double phi = (Math.acos((rr0 + (d * d) - rr1) / (2 * r0 * d))) * 2;
            double theta = (Math.acos((rr1 + (d * d) - rr0) / (2 * r1 * d))) * 2;
            double area1 = 0.5 * theta * rr1 - 0.5 * rr1 * Math.sin(theta);
            double area2 = 0.5 * phi * rr0 - 0.5 * rr0 * Math.sin(phi);

            // Return area of intersection
            return area1 + area2;
        }
    }

}
