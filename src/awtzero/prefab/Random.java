package awtzero.prefab;

public class Random {
    public static Random randominstance = new Random();

    public static int randInt(int min, int max) {
        return (int) (randominstance. * (max - min)) + min;
    }

    public static double randDouble(double min, double max) {
        return (Math.random() * (max - min));
    }


}
