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
    private final Article articleGetDiscountedAmountZeroDiscount = new Article("ArticleForTest", 3, 10.0, 0.0);

    @Test
    void getDiscountedAmount_zeroDiscount() {
        assertEquals(30.0, articleGetDiscountedAmountZeroDiscount.getDiscountedAmount(), EPS);
    }

    private final Article getArticleGetDiscountedAmountNormalCase = new Article("ArticleForTest", 2, 50.0, 15.00);

    @Test
    void getDiscountedAmount_normalCase() {
        assertEquals(85.0, getArticleGetDiscountedAmountNormalCase.getDiscountedAmount(), EPS);
    }

    private final Article getArticleGetDiscountedAmountFullDiscount = new Article("ArticleForTest", 5, 12.0, 100.00);

    @Test
    void getDiscountedAmount_fullDiscount() {
        assertEquals(0.0, getArticleGetDiscountedAmountFullDiscount.getDiscountedAmount(), EPS);
    }

    private final Article getArticleGetDiscountedAmountDecimalPrice = new Article("ArticleForTest", 7, 5.97, 3.0);

    @Test
    void getDiscountedAmount_decimalPrice() {
        assertEquals(40.5363, getArticleGetDiscountedAmountDecimalPrice.getDiscountedAmount(), EPS);
    }

    //P.S. Aquí he probado nueva metodología del testing para mí
    private final Article getArticleGetDiscountedAmountMonotoncityLOW = new Article("ArticleForTest", 2, 50.0, 10.0);
    private final Article getArticleGetDiscountedAmountMonotoncityHIGH = new Article("ArticleForTest", 2, 50.0, 20.0);

    @Test
    void getDiscountedAmount_monotonicity() {
        double low = getArticleGetDiscountedAmountMonotoncityLOW.getDiscountedAmount();
        double high = getArticleGetDiscountedAmountMonotoncityHIGH.getDiscountedAmount();
        assertEquals(true, low > high);
    }

    private final Article getArticleGetDiscountedAmountEdgesMIN = new Article("ArticleForTest", 1, 100.0, 0.0001);
    private final Article getArticleGetDiscountedAmountEdgesMAX = new Article("ArticleForTest", 1, 100.0, 99.9999);

    @Test
    void getDiscountedAmount_edges() {
        assertEquals(99.9999, getArticleGetDiscountedAmountEdgesMIN.getDiscountedAmount(), EPS);
        assertEquals(0.0001, getArticleGetDiscountedAmountEdgesMAX.getDiscountedAmount(), EPS);
    }

    private final Article getArticleGetDiscountedAmountNegativeDiscount = new Article("ArticleForTest", 1, 10.0, -1.0);

    @Test
    void getDiscountedAmount_negatveDiscount() {
        assertThrows(IllegalArgumentException.class, getArticleGetDiscountedAmountNegativeDiscount::getDiscountedAmount);
    }

    private final Article getArticleGetDiscountedAmountTooHighDiscount = new Article("ArticleForTest", 1, 10.0, 100.0001);

    @Test
    void getDiscountedAmount_invalidHigh() {
        assertThrows(IllegalArgumentException.class, getArticleGetDiscountedAmountTooHighDiscount::getDiscountedAmount);
    }

    //P.S. Aquí he probado nueva metodología del testing para mí
    private final Article getArticleGetDiscountedAndGetGrossAmountInvariant = new Article("ArticleForTest", 3, 10.0, 15.0);
    @Test
    void getDiscountedAmount_invariant() {
        double gross = getArticleGetDiscountedAndGetGrossAmountInvariant.getGrossAmount();
        double discounted = getArticleGetDiscountedAndGetGrossAmountInvariant.getDiscountedAmount();
        assertEquals(false, discounted > gross);
    }
}
