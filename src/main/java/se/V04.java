package se;

public class V04 {
    /** Об’єкт, що має деяку вагу */
    public static class Weighted {
        /**
         * Властивість, що зберігає массу об’єкту
         */
        double weight;
        /**
         * Вкладені об’єкти
         */
        Weighted[] nested;

        /**
         * Створення об’єкту з деякими вкладеними об’єктами
         * @param weight вага об’єкту
         * @param nested масив посилань на вкладені об’єкти
         */
        public Weighted(double weight, Weighted... nested) {
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
         * Модифікація значення ваги об’єкта, без урахування вкладених
         * @param weight нове значення ваги
         */
        public void setWeight(double weight) {
            this.weight = weight;
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
     * Алгоритм зміни ваги об’єктів
     */
    public static interface WeightedModifier {
        /**
         * Застосування змін
         */
        void apply();

        /**
         * Відмінити зміни
         */
        void rollback();
    }

    /**
     * Для усіх об’єктів з парною вагою додати 2 до ваги
     */
    public static class WeighedModifierBumpEven implements WeightedModifier {
        /**
         * Кореневий об’єкт, до якого будуть застосовані зміни
         */
        Weighted root;

        /**
         * Створення операції
         *
         * @param root кореневий об’єкт, до якого будуть застосовані зміни
         */
        public WeighedModifierBumpEven(Weighted root) {
            this.root = root;
        }

        //  проста рекурсивна реалізація методу apply
        private void applyRecursively(Weighted w) {
            if (w.getWeight() % 2 == 0) {
                w.setWeight(w.getWeight() + 2);
            }

            for (Weighted n : w.nested) {
                applyRecursively(n);
            }
        }

        /**
         * Застосування змін
         */
        public void apply() {
            applyRecursively(root);
        }

        //  проста рекурсивна реалізація методу rollback
        private void rollbackRecursively(Weighted w) {
            if (w.getWeight() % 2 == 0) {
                w.setWeight(w.getWeight() - 2);
            }

            for (Weighted n : w.nested) {
                rollbackRecursively(n);
            }
        }

        /**
         * Відмінити зміни
         */
        public void rollback() {
            rollbackRecursively(root);
        }
    }

    public static void main(String[] args) {

        //  створення дерева вкладених об’єктів, зовнішній
        //  має вагу 1 та три вкладені об’єкти
        final Weighted wTree = new Weighted(
                1,
                //  один простий об’єкт
                new Weighted(2),
                //  об’єкт котрий має ще один вкладений
                new Weighted(
                        3,
                        new Weighted(4)
                )
        );

        //  створюємо операцію
        final WeighedModifierBumpEven modifier = new WeighedModifierBumpEven(wTree);

        //  перевіряємо вагу до виконання операції
        //  --> 10.0
        System.out.println("total weight before bumping: " + wTree.computeWeight());
        modifier.apply();
        //  перевіряємо вагу після виконання операції
        //  --> 14.0
        System.out.println("total weight after bumping: " + wTree.computeWeight());
        modifier.rollback();
        //  перевіряємо вагу після відміни операції
        //  --> 10.0
        System.out.println("total weight after rolling back: " + wTree.computeWeight());
    }
}
