package se;

public class V06 {
    /** Об’єкт, що має деяку вагу */
    public static interface Weighted {
        /**
         * Повертає значення ваги об’єкта без урахування вкладених
         * @return вага, сила у Ньютонах
         */
        double getWeight();

        /**
         * Повертає значення ваги з урахуванням ваги вкладених об’єктів
         * @return вага, сила у Ньютонах
         */
        double computeWeight();
    }

    /**
     * Реалізація інтерфейсу {@link Weighted}
     */
    public static class WeightedImpl implements Weighted {
        /**
         * Поле, яке зберігає вагу об’єкта
         */
        double weight;
        /**
         * Масив посилань на вкладені об’єкти
         */
        Weighted[] nested;

        /**
         * Створення об’єкту з деякими вкладеними об’єктами
         * @param weight вага об’єкту
         * @param nested масив посилань на вкладені об’єкти
         */
        public WeightedImpl(double weight, Weighted... nested) {
            this.weight = weight;
            this.nested = nested;
        }

        /**
         * Повертає значення ваги об’єкта без урахування вкладених
         * @return вага, сила у Ньютонах
         */
        public double getWeight() {
            return weight;
        }

        /**
         * Повертає значення ваги з урахуванням ваги вкладених об’єктів
         * @return вага, сила у Ньютонах
         */
        public double computeWeight() {
            double total = getWeight();

            for (Weighted w : nested) {
                total += w.computeWeight();
            }

            return total;
        }
    }

    /**
     * Об’єкт, що створює реалізації інтерфейсу {@link Weighted}
     */
    public static class WeightedCreator {
        /**
         * Створення об’єкту з пустим масивом посилань на вкладені об’єкти
         * @param weight вага об’єкту
         * @return створений об’єкт
         */
        public Weighted createSimple(double weight) {
            return new WeightedImpl(weight);
        }

        /**
         * Створення об’єкту з деякими вкладеними об’єктами
         * @param weight вага об’єкту
         * @param nested масив посилань на вкладені об’єкти
         * @return створений об’єкт
         */
        public Weighted createWithNested(double weight, Weighted... nested) {
            return new WeightedImpl(weight, nested);
        }

        /**
         * Створення масиву однакових об’єктів з однаковою вагою
         * @param weight вага об’єкту
         * @param count кількість об’єктів
         * @return масив створених об’єктів
         */
        public Weighted[] createSimpleRepeated(double weight, int count) {
            final Weighted[] res = new Weighted[count];
            for (int i = 0; i < res.length; i++) {
                res[i] = createSimple(weight);
            }
            return res;
        }
    }

    public static void main(String[] args) {
        //  створення об’єкту для створення об’єктів Weighted ;)
        final WeightedCreator creator = new WeightedCreator();

        //  тестова ієрархія об’єктів, кореневий з вагою 1 і двома вкладеними
        final Weighted wTree = creator.createWithNested(
                1,
                //  перший зі вкладених з вагою 2 без вкладених
                creator.createSimple(2),
                //  другий зі вкладених з вагою 3 з одним вкладеним з вагою 4
                creator.createWithNested(
                        3,
                        creator.createSimpleRepeated(1, 4)
                ));

        //  виводимо загальну вагу кореневого об’єкту з урахуванням вкладених
        //  --> 10
        System.out.println("total weight: " + wTree.computeWeight());
    }
}
