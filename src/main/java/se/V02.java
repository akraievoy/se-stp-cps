package se;

public class V02 {
    /** Деякий об’єкт, який можна зважувати */
    public static class Weighted {
        double weight;
        Weighted[] nested;

        /**
         * Створення екземляра класу без вкладених об’єктів
         * @param weight вага об’єкту
         * @param nested масив вкладених об’єктів
         */
        public Weighted(double weight, Weighted... nested) {
            this.weight = weight;
            this.nested = nested;
        }

        /** @return вага об’єкту */
        public double getWeight() {
            return weight;
        }

        /** Обхід дерева об’єктів з викликом
         *  заданої Callback-функції для кожного */
        public void analyzeAll(WeightedAnalyzer wa) {
            wa.analyze(this);
            for (Weighted w : nested) {
                w.analyzeAll(wa);
            }
        }
    }

    /** Callback-інтерфейс, який можна реалізувати
     * обробкою одного елемента Weighted */
    public static interface WeightedAnalyzer {
        void analyze(Weighted w);
    }

    public static void main(String[] args) {
        //  створюємо деяке деревце об’єктів
        final Weighted wTree = new Weighted(
                1,
                new Weighted(2),
                new Weighted(3,
                        new Weighted(4)
                )
        );

        final double[] totalWeight = {0};
        final int[] count = {0};
        //  виклик методу обходу дерева
        wTree.analyzeAll(
                //  з реалізацією Callback-функції, яка обчислює
                //    сумарну вагу та кількість об'єктів
                new WeightedAnalyzer() {
                    public void analyze(Weighted w) {
                        count[0]++;
                        totalWeight[0] += w.getWeight();
                    }
                }
        );

        System.out.println("total weight: " + totalWeight[0]);
        System.out.println("count of elements: " + count[0]);
    }
}