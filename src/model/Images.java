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
    private int height;
    private int width;

    public Images(String pathToImage) throws IOException {
        this.originalImage = this.setOriginalImage(pathToImage);
        System.out.println(pathToImage);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Image fromGenotype(double[] genotype) {
        int numGenes = 4;
        Double x, y, radius, color;
        List<Circle> circles = new ArrayList<>();

        for (int i = 0; i < genotype.length; i += numGenes) {
            x = genotype[i] * this.getHeight();
            y = genotype[i + 1] * this.getWidth();
            radius = genotype[i + 2] * Math.max(this.getHeight(), this.getWidth());
            color = genotype[i + 3] * Colors.MAX_RGB_VALUE;

            circles.add(new Circle(x.intValue(), y.intValue(), radius.intValue(), color.intValue()));
        }

        return this.fromCircles(circles);
    }

    public double getFitness(double[] genotype) {
        return this.getImageFitness(this.fromGenotype(genotype));
    }

    private Image fromCircles(List<Circle> circles) {
        return new Image(this.height, this.width, circles);
    }

    public Image randomImage() {
        return Image.random(this.height, this.width);
    }

    public float getImageFitness(Image img) {
        int[][] evolutiveImage = img.getEvolutiveMatrix();
        float dist = 0;

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Color original = new Color(this.originalImage[i][j]);
                Color evolutive = new Color(evolutiveImage[i][j]);
                dist += Colors.distance(original, evolutive);
            }
        }

        return dist;
    }

    private int[][] setOriginalImage(String path) throws IOException {
        BufferedImage read = ImageIO.read(Files.newInputStream(Paths.get(path)));
        this.height = read.getHeight();
        this.width = read.getWidth();
        return this.getOriginalMatrix(read);
    }


    private int[][] getOriginalMatrix(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        int[][] matrix = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = img.getRGB(i, j);
            }
        }

        return matrix;
    }

    public static void render(int[][] color) throws IOException {

        BufferedImage image = new BufferedImage(color[0].length, color.length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < color.length; i++)
            for (int j = 0; j < color[0].length; j++)
                image.setRGB(j, i, color[i][j]);

        File ImageFile = new File("result");

        ImageIO.write(image, "jpg", ImageFile);

    }

}
