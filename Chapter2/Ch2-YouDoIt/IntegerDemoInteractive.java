// Victor Delgado
//P. 62

import java.util.Scanner;

public class IntegerDemoInteractive {
    public static void main(String[] args) {
        int anInt;
        byte aByte;
        short aShort;
        long aLong;
        Scanner input = new Scanner(System.in);
        try {
            System.out.print("Enter an integer: ");
            anInt = input.nextInt();
            System.out.print("Enter a byte: ");
            aByte = input.nextByte();
            System.out.print("Enter a short: ");
            aShort = input.nextShort();
            System.out.print("Enter a long: ");
            aLong = input.nextLong();
            System.out.println(" The Integer is: " + anInt);
            System.out.println(" The Byte is: " + aByte);
            System.out.println(" The Short is: " + aShort);
            System.out.println(" The Long is: " + aLong);
        } finally {
            input.close();
        }
    }
}
