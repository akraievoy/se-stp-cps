package var04;

import var00.Util;

interface Powers {
    long at(int pos);
}

interface Digits {
    int length();
    int at(int pos);
}

class MultiPowNumber {
    private Digits digits;
    private Powers powers;

    MultiPowNumber(Digits digits, Powers powers) {
        this.digits = digits;
        this.powers = powers;
    }

    public long value() {
        long result = 0;
        for (int pos = 0; pos < digits.length(); pos++) {
            result += digits.at(pos) * powers.at(pos);
        }
        return result;
    }
}

public class MultiPowNumbers {
    public static Powers powers(final long[] powers) {
        return new Powers() {
            public long at(int pos) {
                return powers[pos];
            }
        };
    }

    public static Digits digits(final int[] digits) {
        return new Digits() {
            public int at(int pos) {
                return digits[pos];
            }

            public int length() {
                return digits.length;
            }
        };
    }

    public static boolean eq(
            MultiPowNumber a,
            MultiPowNumber b
    ) {
        return a.value() == b.value();
    }

    public static void main(String[] args) {
        //  encode 123 dec (7B hex)
        final Digits digitsA = digits(new int[]{3, 2, 1});
        final Powers powersA = powers(new long[]{1, 10, 100});
        final MultiPowNumber a = new MultiPowNumber(digitsA, powersA);

        //  encode 7B hex (123 dec)
        final Digits digitsB = digits(new int[]{0xB, 7, 0});
        final Powers powersB = powers(new long[]{1, 16, 256});
        final MultiPowNumber b = new MultiPowNumber(digitsB, powersB);

        //  encode 173 in oct (123 dec)
        final Digits digitsC = digits(new int[]{3, 7, 1});
        final Powers powersC = new Powers() {
            public long at(int pos) {
                return (long) Math.pow(8, pos);
            }
        };
        final MultiPowNumber c = new MultiPowNumber(digitsC, powersC);

        //  encode 123 dec as fibbonachi number
        final Digits digitsD = digits(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1});
        final Powers powersD = new Powers() {
            public long at(int pos) {
                return Util.fibbonachi(pos);
            }
        };
        final MultiPowNumber d = new MultiPowNumber(digitsD, powersD);

        System.out.println("var04: replace method duplicates + extract class (builder)");
        System.out.println("123 dec == 78 hex: " + eq(a, b));
        System.out.println("123 dec == 173 oct: " + eq(a, c));
        System.out.println("173 oct == 10100000000 fib: " + eq(c, d));
    }
}
