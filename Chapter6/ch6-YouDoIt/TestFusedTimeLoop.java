//Victor Delgado
//p.227

import java.time.*;

public class TestFusedTimeLoop {
    public static void main(String[] args) {
        int startTime, endTime;
        int x;
        final int repeat = 5_000_000;
        final int factor = 1_000_000;

        LocalDateTime now;
        now = LocalDateTime.now();
        startTime = now.getNano();
        for (x = 0; x < repeat; ++x)
            method1();
        for (x = 0; x < repeat; ++x)
            method2();
        now = LocalDateTime.now();
        endTime = now.getNano();
        System.out.println("Time for loops executed separately: " +
            ((endTime - startTime) / factor) + " milliseconds");

        now = LocalDateTime.now();
        startTime = now.getNano();
        for (x = 0; x < repeat; ++x) {
            method1();
            method2();
        }
        now = LocalDateTime.now();
        endTime = now.getNano();
        System.out.println("Time for loops executed in a block: " +
            ((endTime - startTime) / factor) + " milliseconds");
    }

    public static void method1() {
    }

    public static void method2() {
    }
}
