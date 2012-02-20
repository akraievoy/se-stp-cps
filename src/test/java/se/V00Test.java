package se;

import junit.framework.TestCase;

import static se.V00.*;

/**
 * Тести для компонент нульового варіанту.
 */
public class V00Test extends TestCase {
    private WeightedAudit audit;
    private WeightedImpl wTree;

    /** Початкове встановлення стану теста. */
    public void setUp() throws Exception {
        //  створення тестової ієрархії об'єктів
        wTree = new WeightedImpl(
                1,
                //  один простий об’єкт
                new WeightedImpl(2),
                //  обгортаємо один з об’єктів у реалізацію об’єкта аудитора
                audit = new WeightedAudit(new WeightedImpl(2)),
                //  об’єкт котрий має ще один вкладений
                new WeightedImpl(3, new WeightedImpl(4))
        );
    }

    /** Очищення стану теста. */
    public void tearDown() throws Exception {
        audit = null;
        wTree = null;
    }

    /** Тестовий приклад. */
    public void testMain() throws Exception {
        assertNotNull(wTree);
        assertNotNull(audit);

        assertEquals(12.0, wTree.computeWeight());
        assertEquals(1, audit.getComputeCallCount());

        assertEquals(12.0, wTree.computeWeight());
        assertEquals(2, audit.getComputeCallCount());

        assertEquals(2.0, audit.computeWeight());
        assertEquals(3, audit.getComputeCallCount());
    }
}