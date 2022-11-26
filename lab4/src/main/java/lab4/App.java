package lab4;

import java.util.Arrays;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        try {
            Group g = new Group(5);
            String realSender = g.getRealSender();
            String fakeSender = g.getFakeSender();
            String[] victims = g.getVictims();

            System.out.println("Real sender = " + realSender);
            System.out.println("Fake sender = " + fakeSender);
            System.out.println("Victims = " + Arrays.toString(victims));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
