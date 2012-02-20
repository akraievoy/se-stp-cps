package se;

public class V03 {
    public static interface WeightAlgo {
        double computeWeight(double orbitProgression, double startWeight);
    }

    public static class WeightAlgoConstant implements WeightAlgo {
        public double computeWeight(double orbitProgression, double startWeight) {
            return startWeight;
        }
    }

    public static class WeightAlgoDisposable implements WeightAlgo {
        double disposeProgression;

        public WeightAlgoDisposable(double disposeProgression) {
            this.disposeProgression = disposeProgression;
        }

        public double computeWeight(double orbitProgression, double startWeight) {
            return orbitProgression > disposeProgression ? 0 : startWeight;
        }
    }

    public static class WeightAlgoExhaustable implements WeightAlgo {
        double exhaustProgression;

        public WeightAlgoExhaustable(double exhaustProgression) {
            this.exhaustProgression = exhaustProgression;
        }

        public double computeWeight(double orbitProgression, double startWeight) {
            final double exhaustRatio = orbitProgression / exhaustProgression;
            final double leftRatio = 1 - exhaustRatio;

            return orbitProgression > exhaustProgression ? 0 : startWeight * leftRatio;
        }
    }

    public static class Weighted {
        double weight;
        WeightAlgo algo = new WeightAlgoConstant();
        Weighted[] nested;

        public Weighted(double weight) {
            this(weight, new Weighted[0]);
        }

        public Weighted(double weight, Weighted[] nested) {
            this.weight = weight;
            this.nested = nested;
        }

        public double getWeight() {
            return weight;
        }

        public double computeWeight(final double orbitProgression) {
            double total = algo.computeWeight(orbitProgression, getWeight());

            for (Weighted w : nested) {
                total += w.computeWeight(orbitProgression);
            }

            return total;
        }

        public Weighted algo(WeightAlgo algo) {
            this.algo = algo;
            return this;
        }
    }

    public static void main(String[] args) {

        final Weighted wTree = new Weighted(
                1,
                new Weighted[]{
                        new Weighted(2).algo(new WeightAlgoDisposable(0.5)),    //	moon landing module
                        new Weighted(
                                3,
                                new Weighted[]{
                                        new Weighted(4).algo(new WeightAlgoExhaustable(0.8))    //	fuel
                                }
                        )
                }
        );

        System.out.println("total weight on start: " + wTree.computeWeight(0));
        System.out.println("total weight near moon landing: " + wTree.computeWeight(0.4));
        System.out.println("total weight near landing on earth: " + wTree.computeWeight(0.9));
    }
}
