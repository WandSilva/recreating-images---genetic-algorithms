package model;

import utils.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Image {
    private static final int NUM_CIRCLES = 10000;
    private List<Circle> circles;
    private int height;
    private int width;
    private int[][] evolutiveImage;

    public Image(int height, int width, List<Circle> randomCircles) {
        this.height = height;
        this.width = width;
        this.evolutiveImage = new int[height][width];
        this.circles = randomCircles;
        Collections.reverse(this.circles);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private Color getPointColor(int x, int y) {
        for (Circle c : circles) {
            if (c.containsPoint(x, y)) {
                return c.getColor();
            }
        }

            return Color.black;
    }


    public int[][] getEvolutiveMatrix() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.evolutiveImage[i][j] = getPointColor(i, j).getRGB();
            }
        }

        return this.evolutiveImage;
    }

    public List<Integer> getGenotype() {
        return this.circles.stream()
                .map(c -> Arrays.asList(c.getX(), c.getY(), c.getRadius(), c.getColor().getRGB()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    public static Image random(int height, int width) {
        List<Circle> randomCircles = IntStream.range(0, Image.NUM_CIRCLES)
                .mapToObj(i -> Circle.random())
                .collect(Collectors.toList());

        return new Image(height, width, randomCircles);
    }
}
