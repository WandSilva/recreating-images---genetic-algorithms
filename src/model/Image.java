package model;

import utils.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Image {
    private static final int NUM_CIRCLES = 10000;
    private List<Circle> circles;
    private int[][] evolutiveImage;

    public Image(List<Circle> randomCircles) {
        this.evolutiveImage = new int[Images.width][Images.height];
        this.circles = randomCircles.stream()
                .filter(c -> c.getPosition() > 0)
                .sorted(Comparator.comparingInt(Circle::getPosition))
                .collect(Collectors.toList());
    }

    private Color getPointColor(int x, int y) {
        for (Circle c : circles) {
            if (c.containsPoint(x, y)) {
                return c.getColor();
            }
        }

        return Color.black;
    }

    public double getDoubt(){
        int doubt = 0;

        for (int i = 0; i < this.circles.size(); i++) {
            for (int j = 0; j < this.circles.size(); j++) {
                doubt += circles.get(i).areaOfIntersection(circles.get(j));
            }
        }

        return Math.sqrt(doubt)/(Images.height * Images.width);
    }


    public int[][] getEvolutiveMatrix() {
        for (int i = 0; i < Images.width; i++) {
            for (int j = 0; j < Images.height; j++) {
                this.evolutiveImage[i][j] = getPointColor(i, j).getRGB();
            }
        }

        return this.evolutiveImage;
    }
}
