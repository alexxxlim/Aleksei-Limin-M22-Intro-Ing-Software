import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test; 

import com.example.Calculator; 

public class CalculatorTestCase {

    private final Calculator calc = new Calculator();
    private static final double EPS = 1e-9;

    // multiply
    @Test
    void multiply_normalCase() {
        assertEquals(6, calc.multiply(2, 3));
    }

    @Test
    void multiply_withZero() {
        assertEquals(0, calc.multiply(5, 0));
        assertEquals(0, calc.multiply(0, 7));
    }

    @Test
    void multiply_withNegativeNumbers() {
        assertEquals(-15, calc.multiply(-3, 5));
        assertEquals(15,  calc.multiply(-3, -5));
    }

    // concat
    @Test
    void concat_twoNormalStrings() {
        assertEquals("HelloWorld", calc.concat("Hello", "World"));
    }

    @Test
    void concat_returnsEmptyWhenAnyArgIsNull() {
        assertEquals(Calculator.EMPTY, calc.concat(null, "World"));
        assertEquals(Calculator.EMPTY, calc.concat("Hello", null));
        assertEquals(Calculator.EMPTY, calc.concat(null, null));
    }

    // sum
    @Test
    void sum_normal() {
        assertEquals(7.5, calc.sum(3.0, 4.5), EPS);
    }

    @Test
    void sum_withNegatives() {
        assertEquals(1.0, calc.sum(5.0, -4.0), EPS);
        assertEquals(-9.0, calc.sum(-5.0, -4.0), EPS);
    }

    // discount
    @Test
    void discount_validPercent() {
        assertEquals(80.0, calc.discount(100.0, 20.0), EPS);
        assertEquals(95.0, calc.discount(100.0, 5.0), EPS);
    }

    @Test
    void discount_zeroAndHundredPercent() {
        assertEquals(100.0, calc.discount(100.0, 0.0), EPS);
        assertEquals(0.0,   calc.discount(100.0, 100.0), EPS);
    }

    @Test
    void discount_invalidPercentThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.discount(100.0, -0.01));
        assertThrows(IllegalArgumentException.class, () -> calc.discount(100.0, 100.01));
    }

    // calculateTotal
    @Test
    void calculateTotal_sumsAll() {
        List<Double> amounts = Arrays.asList(10.0, 20.5, 30.25);
        assertEquals(60.75, calc.calculateTotal(amounts), EPS);
    }

    @Test
    void calculateTotal_emptyListReturnsZero() {
        assertEquals(0.0, calc.calculateTotal(Arrays.asList()), EPS);
    }
}