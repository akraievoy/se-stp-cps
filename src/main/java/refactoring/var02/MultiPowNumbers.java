package var02;

import var00.Util;

interface Powers {
    long at(int pos);
}

interface MultiPowNumber {
    long value();
}

class MultiPowNumberArray implements MultiPowNumber {
    private int[] digits;
    private long[] powers;

    MultiPowNumberArray(int[] digits, long[] powers) {
        this.digits = digits;
        this.powers = powers;
    }

    public long value() {
        long result = 0;
        for (int pos = 0; pos < digits.length; pos++) {
            result += digits[pos] * powers[pos];
        }
        return result;
    }
}

class MultiPowNumberFunction implements MultiPowNumber {
    private int[] digits;
    private Powers powers;

    MultiPowNumberFunction(int[] digits, Powers powers) {
        this.digits = digits;
        this.powers = powers;
    }

    public long value() {
        long result = 0;
        for (int pos = 0; pos < digits.length; pos++) {
            result += digits[pos] * powers.at(pos);
        }
        return result;
    }
}

public class MultiPowNumbers {
    public static boolean eq(
            MultiPowNumber a,
            MultiPowNumber b
    ) {
        return a.value() == b.value();
    }

    public static void main(String[] args) {
        //  encode 123 dec (7B hex)
        final MultiPowNumberArray a = new MultiPowNumberArray(
                new int[]{3, 2, 1},
                new long[]{1, 10, 100}
        );

        //  encode 7B hex (123 dec)
        final MultiPowNumberArray b = new MultiPowNumberArray(
                new int[]{0xB, 7, 0},
                new long[]{1, 16, 256}
        );

        //  encode 173 in oct (123 dec)
        final MultiPowNumberFunction c = new MultiPowNumberFunction(
                new int[]{3, 7, 1},
                new Powers() {
                    public long at(int pos) {
                        return (long) Math.pow(8, pos);
                    }
                }
        );

        //  encode 123 dec as fibbonachi number
        final MultiPowNumberFunction d = new MultiPowNumberFunction(
                new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                new Powers() {
                    public long at(int pos) {
                        return Util.fibbonachi(pos);
                    }
                }
        );

        System.out.println("var02: extract (template) method + pull-up");
        System.out.println("123 dec == 78 hex: " + eq(a, b));
        System.out.println("123 dec == 173 oct: " + eq(a, c));
        System.out.println("173 oct == 10100000000 fib: " + eq(c, d));
    }
}
