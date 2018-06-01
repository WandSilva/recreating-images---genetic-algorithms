package utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Colors {
    public static int MAX_RGB_VALUE = 16777215;

    public static Color average(List<Color> colors) {
        if(colors.size() == 0){
            return new Color(0);
        }

        long r = 0;
        long g = 0;
        long b = 0;

        for (Color color : colors) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }

        r /= colors.size();
        g /= colors.size();
        b /= colors.size();

        return new Color((int) r, (int) g, (int) b);
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
