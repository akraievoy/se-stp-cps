package se;

public class V04 {
    public static interface WeightedModifier {
        void apply();

        void rollback();
    }

    public static class WeighedModifierBumpEven implements WeightedModifier {
        Weighted root;

        public WeighedModifierBumpEven(Weighted root) {
            this.root = root;
        }

        private void applyRecursively(Weighted w) {
            if (w.getWeight() % 2 == 0) {
                w.setWeight(w.getWeight() + 2);
            }

            for (Weighted n : w.nested) {
                applyRecursively(n);
            }
        }

        public void apply() {
            applyRecursively(root);
        }

        private void rollbackRecursively(Weighted w) {
            if (w.getWeight() % 2 == 0) {
                w.setWeight(w.getWeight() - 2);
            }

            for (Weighted n : w.nested) {
                rollbackRecursively(n);
            }
        }

        public void rollback() {
            rollbackRecursively(root);
        }
    }

    public static class Weighted {
        double weight;
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

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double computeWeight() {
            double total = getWeight();

            for (Weighted w : nested) {
                total += w.computeWeight();
            }

            return total;
        }
    }

    public static void main(String[] args) {

        final Weighted wTree = new Weighted(
                1,
                new Weighted[]{
                        new Weighted(2),
                        new Weighted(
                                3,
                                new Weighted[]{
                                        new Weighted(4)
                                }
                        )
                }
        );

        final WeighedModifierBumpEven modifier = new WeighedModifierBumpEven(wTree);

        System.out.println("total weight before bumping: " + wTree.computeWeight());
        modifier.apply();
        System.out.println("total weight after bumping: " + wTree.computeWeight());
        modifier.rollback();
        System.out.println("total weight after rolling back: " + wTree.computeWeight());
    }
}
