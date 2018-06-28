package utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Colors {
    public static int MAX_RGB_VALUE = 16777215;

    public static Color average(List<Color> colors) {
        if (colors.isEmpty()) {
            return new Color(0);
        }
        Color result = colors.get(0);
        for (int i = 1; i < colors.size(); i++) {
            result = Colors.blend(result, colors.get(i), 0.5f);
        }

        return result;
    }

    private static Color blend(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }

    public static double distance(Color a, Color b) {
        List<Integer> colorsA = Arrays.asList(a.getRed(), a.getGreen(), a.getBlue());
        List<Integer> colorsB = Arrays.asList(b.getRed(), b.getGreen(), b.getBlue());

        return Colors.euclideanDistance(colorsA, colorsB);
    }

    private static double euclideanDistance(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Different sizes");
        }

        double sum = IntStream.range(0, a.size())
                .map(i -> a.get(i) - b.get(i))
                .mapToDouble(i -> Math.pow(i, 2))
                .sum();

        return Math.sqrt(sum);
    }
}
