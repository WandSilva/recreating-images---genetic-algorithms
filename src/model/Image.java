package model;

import utils.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Image {
    private static final int NUM_CIRCLES = 10000;
    private Circle[] circles;
    private int height;
    private int width;
    private int[][] evolutiveImage;

    private Image(int height, int width, Circle[] randomCircles) {
        this.height = height;
        this.width = width;
        this.evolutiveImage = new int[height][width];
        this.circles = randomCircles;
    }


    private Color getPointColor(int x, int y) {
        List<Circle> pointCircles = new ArrayList<>();

        for (Circle c : circles) {
            if (c.containsPoint(x, y)) {
                pointCircles.add(c);
            }
        }

        List<Color> colors = pointCircles.stream()
                .map(Circle::getColor)
                .collect(Collectors.toList());

        return Colors.average(colors);
    }


    public int[][] getEvolutiveMatrix() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.evolutiveImage[i][j] = getPointColor(i, j).getRGB();
            }
        }

        return this.evolutiveImage;
    }


    public static Image random(int height, int width) {
        Circle[] randomCircles = new Circle[Image.NUM_CIRCLES];

        for (int i = 0; i < NUM_CIRCLES; i++) {
            randomCircles[i] = Circle.random();
        }

        return new Image(height, width, randomCircles);
    }
}
