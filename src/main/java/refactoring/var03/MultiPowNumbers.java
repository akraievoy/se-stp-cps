package var03;

interface Powers {
    long at(int pos);
}

interface MultiPowNumber {
    long value();
    long powerAt(int pos);
    int digitAt(int pos);
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
            result += digitAt(pos) * powerAt(pos);
        }
        return result;
    }

    public long powerAt(int pos) {
        return powers[pos];
    }

    public int digitAt(int pos) {
        return digits[pos];
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
            result += digitAt(pos) * powers.at(pos);
        }
        return result;
    }

    public int digitAt(int pos) {
        return digits[pos];
    }

    public long powerAt(int pos) {
        return powers.at(pos);
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
                        if (pos < 2) return 1;

                        long powPrev = 1;
                        long pow = 2;
                        long curPos = 2;

                        while (curPos < pos) {
                            final long nextPow = pow + powPrev;
                            powPrev = pow;
                            pow = nextPow;
                            curPos++;
                        }

                        return pow;
                    }
                }
        );

        System.out.println("var03: replace method duplicates + pullup");
        System.out.println("123 dec == 78 hex: " + eq(a, b));
        System.out.println("123 dec == 173 oct: " + eq(a, c));
        System.out.println("173 oct == 10100000000 fib: " + eq(c, d));
    }
}
