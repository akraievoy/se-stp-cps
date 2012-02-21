package se;

public class V03 {
    /**
     * Алгоритм, який описує зміни ваги об’єктів у залежності від місця на орбіті
     */
    public static interface WeightAlgo {
        /**
         * Обчислення ваги об’єкта в залежності від прогресу руху по орбіті
         * та початкової ваги
         *
         * @param orbitProgression прогрес руху по орбіті,
         *                         від 0 (початок) до 1(завершення)
         * @param startWeight початкова вага
         * @return обчислена вага
         */
        double computeWeight(double orbitProgression, double startWeight);
    }

    /**
     * Алгоритм, що описує відсутність змін у вазі об’єкта
     */
    public static class WeightAlgoConstant implements WeightAlgo {
        /**
         * Обчислення ваги об’єкта в залежності від прогресу руху по орбіті
         * та початкової ваги
         *
         * @param orbitProgression прогрес руху по орбіті,
         *                         від 0 (початок) до 1(завершення)
         * @param startWeight початкова вага
         * @return обчислена вага
         */
        public double computeWeight(double orbitProgression, double startWeight) {
            return startWeight;
        }
    }

    /**
     * Алгоритм, що описує зміни у вазі об’єкта, що буде відділено
     * на деякій фазі просування по орбіті
     */
    public static class WeightAlgoDisposable implements WeightAlgo {
        /**
         * Фаза відділення об’єкта, від 0 до 1
         */
        double disposeProgression;

        /**
         * Створення примірника алгоритма
         *
         * @param disposeProgression фаза відділення об’єкта, від 0 до 1
         */
        public WeightAlgoDisposable(double disposeProgression) {
            this.disposeProgression = disposeProgression;
        }

        /**
         * Обчислення ваги об’єкта в залежності від прогресу руху по орбіті
         * та початкової ваги
         *
         * @param orbitProgression прогрес руху по орбіті,
         *                         від 0 (початок) до 1(завершення)
         * @param startWeight початкова вага
         * @return обчислена вага
         */
        public double computeWeight(double orbitProgression, double startWeight) {
            return orbitProgression > disposeProgression ? 0 : startWeight;
        }
    }

    /**
     * Алгоритм, що описує зміни у вазі об’єкта,
     * що починає постійно та рівномірно використовуватись
     * на початку та повністю закінчується на заданому
     * єтапі руху по орбіті
     */
    public static class WeightAlgoExhaustable implements WeightAlgo {
        /**
         * Фаза орбіти (від 0 до 1), на якій об’єкт буде повністю використано
         */
        double exhaustProgression;

        /**
         * Створення примірника алгоритма
         *
         * @param exhaustProgression фаза повного використання об’єкта,
         *                           від 0 до 1
         */
        public WeightAlgoExhaustable(double exhaustProgression) {
            this.exhaustProgression = exhaustProgression;
        }

        /**
         * Обчислення ваги об’єкта в залежності від прогресу руху по орбіті
         * та початкової ваги
         *
         * @param orbitProgression прогрес руху по орбіті,
         *                         від 0 (початок) до 1(завершення)
         * @param startWeight початкова вага
         * @return обчислена вага
         */
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
