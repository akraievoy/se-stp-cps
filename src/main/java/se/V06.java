package se;

public class V06 {
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

    public static class WeightedCreator {
        public Weighted createSimple(double weight) {
            return new WeightedImpl(weight);
        }

        public Weighted createWithNested(double weight, Weighted[] nested) {
            return new WeightedImpl(weight, nested);
        }

        public Weighted[] createSimpleRepeated(double weight, int count) {
            final Weighted[] res = new Weighted[count];
            for (int i = 0; i < res.length; i++) {
                res[i] = createSimple(weight);
            }
            return res;
        }
    }

    public static void main(String[] args) {
        final WeightedCreator creator = new WeightedCreator();
        final Weighted wTree = creator.createWithNested(
                1,
                new Weighted[]{
                        creator.createSimple(2),
                        creator.createWithNested(
                                3,
                                creator.createSimpleRepeated(1, 4)
                        )
                }
        );

        System.out.println("total weight: " + wTree.computeWeight());
    }
}
