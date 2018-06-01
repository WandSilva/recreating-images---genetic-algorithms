package utils;

import model.Circle;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;

public class ColorUtils {

    public static Color average(List<Color> colors){
        long r = 0;
        long g = 0;
        long b = 0;

        for(Color color : colors){
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }

        r/= colors.size();
        g/= colors.size();
        b/= colors.size();

        return new Color(r, g, b);
    }

    public static double distance(Color a, Color b){
        List<Integer> colorsA = List.of(a.getRed(), a.getGreen(), a.getBlue());
        List<Integer> colorsB = List.of(b.getRed(), b.getGreen(), b.getBlue());

        return euclideanDistance(colorsA, colorsB);
    }

    private static double euclideanDistance(List<Integer> a, List<Integer> b){
        if(a.size() != b.size()){
            throw new IllegalArgumentException("Different sizes");
        }

        double sum = IntStream.range(0, a.size())
                .map(i -> a.get(i) - b.get(i))
                .mapToDouble(i -> Math.pow(i, 2))
                .sum();

        return Math.sqrt(sum);
    }
}
