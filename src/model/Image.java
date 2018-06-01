package model;

import utils.ColorUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Image {
    private static final int NUM_CIRCLES = 10000;
    private Circle[] circles;
    private int height;
    private int width;
    private int[][] evolutiveImage;
    private static int[][] originalImage;

    private Image(Circle[] randomCircles) {
        this.circles = randomCircles;
    }

    private void CandidateImage(int width, int height){
        this.height = height;
        this.width = width;
        this.evolutiveImage = new int[height][width];
    }

    public static void setOriginalImage(String path) throws IOException {
        BufferedImage read = ImageIO.read(Files.newInputStream(Paths.get(path)));
        Image.originalImage = Image.getOriginalMatrix(read);
    }

    private Color getPointColor(int x, int y){
        List<Circle> pointCircles = new ArrayList<>();

        for(Circle c: circles){
            if(c.containsPoint(x, y)){
                pointCircles.add(c);
            }
        }

        List<Color> colors = pointCircles.stream()
                .map(c -> c.getColor())
                .collect(Collectors.toList());

        return ColorUtils.average(colors);
    }

    private static int[][] getOriginalMatrix(BufferedImage img){
        int height = img.getHeight();
        int width = img.getWidth();
        int[][] matrix = new int[height][width];

        for (int i = 0; i < height ; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = img.getRGB(i, j);
            }
        }

        return matrix;
    }

    //Compose image w/ circles
    public int[][] getEvolutiveMatrix(){
        for (int i = 0; i < this.height ; i++) {
            for (int j = 0; j < this.width; j++) {
               this.evolutiveImage[i][j] = getPointColor(i, j).getRGB();
            }
        }

        return this.evolutiveImage;
    }

    public float fitness(){
        float dist = 0;

        for (int i = 0; i < this.height ; i++) {
            for (int j = 0; j < this.width; j++) {
                Color original = new Color(this.originalImage[i][j]);
                Color evolutive = new Color(this.evolutiveImage[i][j]);
                dist += ColorUtils.distance(original, evolutive);
            }
        }

        return dist;
    }


    private static Image random(){
        Circle[] randomCircles = new Circle[Image.NUM_CIRCLES];

        for (int i = 0; i < NUM_CIRCLES; i++) {
            randomCircles[i] = Circle.random();
        }

        return new Image(randomCircles);
    }
}
