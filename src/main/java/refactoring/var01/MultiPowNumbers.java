package refactoring.var01;

import refactoring.var00.Util;

interface Powers {
    long at(int pos);
}

public class MultiPowNumbers {
    public static boolean eq(
            int[] digitsA, long[] powersA,
            int[] digitsB, long[] powersB
    ) {
        long resultA = 0;
        for (int pos = 0; pos < digitsA.length; pos++) {
            resultA += digitsA[pos] * powersA[pos];
        }

        long resultB = 0;
        for (int pos = 0; pos < digitsB.length; pos++) {
            resultB += digitsB[pos] * powersB[pos];
        }

        return resultA == resultB;
    }

    public static boolean eq(
            int[] digitsA, long[] powersA,
            int[] digitsB, Powers powersB
    ) {
        long resultA = 0;
        for (int pos = 0; pos < digitsA.length; pos++) {
            resultA += digitsA[pos] * powersA[pos];
        }

        long resultB = 0;
        for (int pos = 0; pos < digitsB.length; pos++) {
            resultB += digitsB[pos] * powersB.at(pos);
        }

        return resultA == resultB;
    }

    public static boolean eq(
            int[] digitsA, Powers powersA,
            int[] digitsB, Powers powersB
    ) {
        long resultA = 0;
        for (int pos = 0; pos < digitsA.length; pos++) {
            resultA += digitsA[pos] * powersA.at(pos);
        }

        long resultB = 0;
        for (int pos = 0; pos < digitsB.length; pos++) {
            resultB += digitsB[pos] * powersB.at(pos);
        }

        return resultA == resultB;
    }

    public static void main(String[] args) {
        //  encode 123 dec (7B hex)
        final int[] digitsA = {3,2,1};
        final long[] powersA = {1,10,100};

        //  encode 7B hex (123 dec)
        final int[] digitsB = {0xB,7,0};
        final long[] powersB = {1,16,256};

        //  encode 173 in oct (123 dec)
        final int[] digitsC = {3,7,1};
        final Powers powersC = new Powers() {
            public long at(int pos) {
                return (long) Math.pow(8, pos);
            }
        };

        //  encode 123 dec as fibbonachi number
        final int[] digitsD = {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1};
        final Powers powersD = new Powers() {
            public long at(int pos) {
                return Util.fibbonachi(pos);
            }
        };

        System.out.println("var01: extract method + introduce parameter object");
        System.out.println("123 dec == 78 hex: " + eq(digitsA, powersA, digitsB, powersB));
        System.out.println("123 dec == 173 oct: " + eq(digitsA, powersA, digitsC, powersC));
        System.out.println("173 oct == 10100000000 fib: " + eq(digitsC, powersC, digitsD, powersD));
    }
}
