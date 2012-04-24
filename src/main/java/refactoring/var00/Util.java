package var00;

/**
 * LATER add javadocs for a class created by anton
 */
public class Util {
    public static long fibbonachi(int pos) {
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
