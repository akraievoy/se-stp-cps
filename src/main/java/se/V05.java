package se;

public class V05 {
    public static class Solid {
        double mass;
        double volume;

        public Solid(double mass, double volume) {
            this.mass = mass;
            this.volume = volume;
        }

        public double getPureWeight(double gravityAccel, double envDensity) {
            return (mass - volume * envDensity) * gravityAccel;
        }
    }

    public static interface Weighted {
        /**
         * @return force, newtons
         */
        double getWeight();

        double computeWeight();
    }

    public static class WeightedImpl implements Weighted {
        double weight;
        Weighted[] nested;

        public WeightedImpl(double weight) {
            this(weight, new Weighted[0]);
        }

        public WeightedImpl(double weight, Weighted[] nested) {
            this.weight = weight;
            this.nested = nested;
        }

        public double getWeight() {
            return weight;
        }

        public double computeWeight() {
            double total = getWeight();

            for (Weighted w : nested) {
                total += w.computeWeight();
            }

            return total;
        }
    }

    public static class SolidWeightedOnEarth implements Weighted {
        Solid solid;

        public SolidWeightedOnEarth(Solid solid) {
            this.solid = solid;
        }

        public double computeWeight() {
            return getWeight();
        }

        public double getWeight() {
            return solid.getPureWeight(9.80665, 1.2041);
        }
    }

    public static void main(String[] args) {
        final WeightedImpl wTree = new WeightedImpl(
                1,
                new Weighted[]{
                        new WeightedImpl(2),
                        new WeightedImpl(
                                3,
                                new Weighted[]{
                                        new WeightedImpl(4),
                                        //	inject some other library with other weight computations
                                        new SolidWeightedOnEarth(new Solid(5, 0.025))
                                }
                        )
                }
        );

        System.out.println("total weight with a solid on Earth surface: " + wTree.computeWeight());
    }
}
