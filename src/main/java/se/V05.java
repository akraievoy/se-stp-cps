package se;

public class V05 {
    /**
     * Деяка "стороння" бібліотека, що моделює об’єкти з урахуванням
     * прискорення вільного падіння та щільності оточуючого середовища.
     */
    public static class Solid {
        /**
         * Властивість, що зберігає массу об’єкту
         */
        double mass;
        /**
         * Об’єм простору, зайнятого об’єктом
         */
        double volume;

        /**
         * Створення об’єкту об’єкту заданої маси та об’єму
         * @param mass маса об’єкту
         * @param volume об’єм об’єкту
         */
        public Solid(double mass, double volume) {
            this.mass = mass;
            this.volume = volume;
        }

        /**
         * Обчислення ваги об’єкту з урахуванням прискорення вільного
         * падіння та щільності оточуючого середовища
         * @param gravityAccel прискорення вільного падіння
         * @param envDensity щільність оточуючого середовища
         * @return чиста вага об’єкта
         */
        public double getPureWeight(double gravityAccel, double envDensity) {
            return (mass - volume * envDensity) * gravityAccel;
        }
    }

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
     * Класс, що здійснює інтеграцію сторонньої бібліотеки з інтерфейсом
     * Weighted
     */
    public static class SolidWeightedOnEarth implements Weighted {
        /** Посилання на інтегрований об’єкт зі стороньої бібліотеки */
        Solid solid;

        /**
         * Створення об’єкту-інтегратора
         * @param solid посилання на об’єкт зі стороньої бібліотеки
         */
        public SolidWeightedOnEarth(Solid solid) {
            this.solid = solid;
        }

        /**
         * Повертає значення ваги з урахуванням ваги вкладених об’єктів
         * @return вага, сила у Ньютонах
         */
        public double computeWeight() {
            //  адаптовані об’єкти не підтримують вкладеність,
            //      тому просто повертаємо значення getWeight()
            return getWeight();
        }

        /**
         * Повертає значення ваги з урахуванням ваги вкладених об’єктів
         * @return вага, сила у Ньютонах
         */
        public double getWeight() {
            //  обчислюємо вагу об’єкта на поверхні Землі
            //      з урахуванням щільності повітря
            return solid.getPureWeight(9.80665, 1.2041);
        }
    }

    public static void main(String[] args) {
        //  створення тестового набору вкладених об’єктів
        //  кореневий об’єкт з вагою 1 та двома безпосередньо вкладеними
        final WeightedImpl wTree = new WeightedImpl(
                1,
                //  перший вкладений об’єкт
                new WeightedImpl(2),
                //  другий вкладений об’єкт, ще з двома вкладеними
                new WeightedImpl(
                        3,
                        new WeightedImpl(4),
                        //  один з яких є інтеграцією зі стороньою бібліотекою
                        new SolidWeightedOnEarth(new Solid(5, 0.025))
                )
        );

        //  виводимо повну вагу всього дерева об’єктів
        //  --> 58.738045318375
        System.out.println(
                "total weight with a solid on Earth surface: " +
                        wTree.computeWeight()
        );
    }
}
