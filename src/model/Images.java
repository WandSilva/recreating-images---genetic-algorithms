package model;

import utils.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Images {

    private int[][] originalImage;
    public static int height;
    public static int width;
    public static final int numFeatures = 5;

    public Images(String pathToImage) throws IOException {
        BufferedImage read = ImageIO.read(Files.newInputStream(Paths.get(pathToImage)));
        Images.height = read.getHeight();
        Images.width = read.getWidth();
        this.originalImage = this.getOriginalMatrix(read);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Image fromGenotype(int[] genotype) {
        int x, y, radius, grayTone;
        int alpha;
        List<Circle> circles = new ArrayList<>();

        for (int i = 0; i < genotype.length; i += Images.numFeatures) {
            x = genotype[i];
            y = genotype[i + 1];
            radius = genotype[i + 2];
            grayTone = genotype[i + 3];
            alpha = genotype[i + 4];
            circles.add(new Circle(x, y, radius, grayTone, grayTone, grayTone, alpha));
        }

        return this.fromCircles(circles);
    }

    public double getFitness(int[] genotype) {
        return this.getImageFitness(this.fromGenotype(genotype));
    }

    private Image fromCircles(List<Circle> circles) {
        return new Image(circles);
    }

    public float getImageFitness(Image img) {
        int[][] evolutiveImage = img.getEvolutiveMatrix();
        float dist = 0;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Color original = new Color(this.originalImage[i][j]);
                Color evolutive = new Color(evolutiveImage[i][j]);
                dist += Colors.distance(original, evolutive);
            }
        }

        return dist;
    }


    private int[][] getOriginalMatrix(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        int[][] matrix = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = img.getRGB(i, j);
            }
        }

        return matrix;
    }

    public static void render(int[][] color) throws IOException {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < Images.width; i++)
            for (int j = 0; j < Images.height; j++)
                image.setRGB(i, j, color[i][j]);

        File ImageFile = new File("result_" + System.currentTimeMillis() + ".jpg");

        ImageIO.write(image, "jpg", ImageFile);

    }

    public static void main(String[] args) throws IOException {
        Images x = new Images("images/coracao.jpg");
        Images.render(x.originalImage);
    }

}
