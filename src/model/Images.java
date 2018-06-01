package model;

import utils.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Images {

    private int[][] originalImage;
    private int height;
    private int width;

    public Images(String pathToImage) throws IOException {
        this.originalImage = this.setOriginalImage(pathToImage);
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
}
