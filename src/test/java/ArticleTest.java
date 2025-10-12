import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.model.Article;

public class ArticleTest {

    private static final double EPS = 1e-6;

    //get gross amount
    private final Article articleGetGrossAmountNormalCase = new Article("ArticleForTest", 2, 10.5, 10.00);

    @Test
    void getGrossAmount_normalCase() {
        assertEquals(21.00, articleGetGrossAmountNormalCase.getGrossAmount(), EPS);
    }

    private final Article articleGetGrossAmountZeroQuantity = new Article("ArticleForTest", 0, 10.5, 10.00);

    @Test
    void getGrossAmount_zeroQuantity() {
        assertEquals(0.0, articleGetGrossAmountZeroQuantity.getGrossAmount(), EPS);
    }

    private final Article articleGetGrossAmountZeroPrice = new Article("ArticleForTest", 5, 0.0, 10.00);

    @Test
    void getGrossAmount_zeroPrice() {
        assertEquals(0.0, articleGetGrossAmountZeroPrice.getGrossAmount(), EPS);
    }

    private final Article articleGetGrossAmountDoublePriceIntegerQuantity = new Article("ArticleForTest", 7, 5.97, 10.00);

    @Test
    void getGrossAmount_DoublePriceIntegerQuantity() {
        assertEquals(41.79, articleGetGrossAmountDoublePriceIntegerQuantity.getGrossAmount(), EPS);
    }

    private final Article articleGetGrossAmountLargeValues = new Article("ArticleForTest", 1_000_000, 999.99, 0.0);

    @Test
    void getGrossAmount_largeValues() {
        assertEquals(999990000.0, articleGetGrossAmountLargeValues.getGrossAmount(), EPS);
    }

    //P.S. Profesor, esta prueba fue escrita para aumentar la cobertura del código, porque en las especificaciones
    //técnicas para la lógica de la clase de producto, usted no dijo que la cantidad o el precio no pueden ser
    //iguales a un número negativo
    private final Article articleGetGrossAmountNegativeQuantity = new Article("ArticleForTest", -3, 10.0, 0.0);

    @Test
    void getGrossAmount_negativeQuantity() {
        assertEquals(-30.0, articleGetGrossAmountNegativeQuantity.getGrossAmount(), EPS);
    }

    //P.S. Profesor, esta prueba fue escrita para aumentar la cobertura del código, porque en las especificaciones
    //técnicas para la lógica de la clase de producto, usted no dijo que la cantidad o el precio no pueden ser
    //iguales a un número negativo
    private final Article articleGetGrossAmountNegativePrice = new Article("ArticleForTest", 3, -10.0, 0.0);

    @Test
    void getGrossAmount_negativePrice() {
        assertEquals(-30.0, articleGetGrossAmountNegativePrice.getGrossAmount(), EPS);
    }

    //P.S. Aquí he probado nueva metodología del testing para mí
    private final Article articleGetGrossAmountRepeatability = new Article("ArticleForTest", 2, 10.5, 0.0);

    @Test
    void getGrossAmount_repeatability() {
        double firstCall = articleGetGrossAmountRepeatability.getGrossAmount();
        double secondCall = articleGetGrossAmountRepeatability.getGrossAmount();
        assertEquals(firstCall, secondCall, EPS);
    }
    //get discounted amount

}
