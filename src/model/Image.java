package model;

import utils.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Image {
    private static final int NUM_CIRCLES = 10000;
    private List<Circle> circles;
    private int[][] evolutiveImage;

    public Image(List<Circle> randomCircles) {
        this.evolutiveImage = new int[Images.width][Images.height];
        this.circles = randomCircles;
        Collections.reverse(this.circles);
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
        for (int i = 0; i < Images.width; i++) {
            for (int j = 0; j < Images.height; j++) {
                this.evolutiveImage[i][j] = getPointColor(i, j).getRGB();
            }
        }

        return this.evolutiveImage;
    }
}
