package refactoring.var05;

import refactoring.var00.Util;

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

class NumberBuilder {
    private Digits digits;
    private Powers powers;

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

    public NumberBuilder withPowers(final long[] powersArr) {
        powers = powers(powersArr);
        return this;
    }

    public NumberBuilder withDigits(final int[] digitsArr) {
        digits = digits(digitsArr);
        return this;
    }

    public NumberBuilder withPowers(final Powers newPowers) {
        powers = newPowers;
        return this;
    }

    public NumberBuilder withDigits(final Digits newDigits) {
        digits = newDigits;
        return this;
    }

    public MultiPowNumber build() {
        return new MultiPowNumber(digits, powers);
    }
}

public class MultiPowNumbers {

    public static boolean eq(
            MultiPowNumber a,
            MultiPowNumber b
    ) {
        return a.value() == b.value();
    }

    public static MultiPowNumber valueOf(Powers powers, final long value) {
        int digitCount = 0;
        while (digitCount == 0 || powers.at(digitCount) < value) {
            digitCount ++;
        }

        int[] digits = new int[digitCount+1];
        long valueRemainder = value;
        while (digitCount >= 0) {
            digits[digitCount] = (int) (valueRemainder / powers.at(digitCount));
            valueRemainder %= powers.at(digitCount);
            digitCount--;
        }

        return new NumberBuilder().withDigits(digits).withPowers(powers).build();
    }

    public static void main(String[] args) {
        //  encode 123 dec (7B hex)
        final MultiPowNumber a = valueOf(NumberBuilder.powers(new long[]{1, 10, 100, 1000}), 123);

        //  encode 7B hex (123 dec)
        final MultiPowNumber b =
                new NumberBuilder()
                        .withDigits(new int[]{0xB, 7, 0})
                        .withPowers(new long[]{1, 16, 256})
                        .build();

        //  encode 173 in oct (123 dec)
        final MultiPowNumber c =
                new NumberBuilder()
                        .withDigits(new int[]{3, 7, 1})
                        .withPowers(new Powers() {
                            public long at(int pos) {
                                return (long) Math.pow(8, pos);
                            }
                        })
                        .build();

        //  encode 123 dec as fibbonachi number
        final MultiPowNumber d =
                new NumberBuilder()
                        .withDigits(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1})
                        .withPowers(new Powers() {
                            public long at(int pos) {
                                return Util.fibbonachi(pos);
                            }
                        })
                        .build();

        System.out.println("var05: move valueOf to builder");
        System.out.println("123 dec == 78 hex: " + eq(a, b));
        System.out.println("123 dec == 173 oct: " + eq(a, c));
        System.out.println("173 oct == 10100000000 fib: " + eq(c, d));
    }
}
