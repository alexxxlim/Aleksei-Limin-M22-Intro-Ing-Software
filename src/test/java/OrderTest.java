import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.model.Order;
import com.example.model.Article;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class OrderTest {

    private static final double EPS = 1e-6;

    //get gross total

    @Test
    void getGrossTotal_normalCase() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.5, 0.0),
                new Article("ArticleForTest2", 3, 4.0, 0.0)
        );
        Order order = new Order("001", articles);
        assertEquals(33.0, order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_emptyList() {
        Order order = new Order("002", Collections.emptyList());
        assertEquals(0.0, order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_nullList() {
        Order order = new Order("003", null);
        assertEquals(0.0, order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_singleArticle() {
        Article a = new Article("ArticleForTest", 5, 2.2, 0.0);
        Order order = new Order("004", Arrays.asList(a));
        assertEquals(a.getGrossAmount(), order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_allZeroQuantities() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 0, 9.99, 0.0),
                new Article("ArticleForTest2", 0, 5.50, 0.0)
        );
        Order order = new Order("005", articles);
        assertEquals(0.0, order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_largeNumbers() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 1000000, 999.99, 0.0),
                new Article("ArticleForTest2", 500000, 1234.56, 0.0)
        );
        Order order = new Order("006", articles);
        assertEquals(1617270000, order.getGrossTotal(), EPS);
    }

    @Test
    void getGrossTotal_negativeValues() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", -2, 10.0, 0.0),
                new Article("ArticleForTest2", 3, -5.0, 0.0)
        );
        Order order = new Order("007", articles);
        assertEquals(-35.0, order.getGrossTotal(), EPS);
    }


    //get discounted total

    @Test
    void getDiscountedTotal_normalCase() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.0, 10.0),
                new Article("ArticleForTest2", 3, 5.0, 20.0)
        );
        Order order = new Order("008", articles);
        assertEquals(30.0, order.getDiscountedTotal(), EPS);
    }

    @Test
    void getDiscountedTotal_allZeroDiscounts() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.0, 0.0),
                new Article("ArticleForTest2", 3, 5.0, 0.0)
        );
        Order order = new Order("009", articles);
        assertEquals(order.getGrossTotal(), order.getDiscountedTotal(), EPS);
    }

    @Test
    void getDiscountedTotal_allTotalDiscounts() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.0, 100.0),
                new Article("ArticleForTest2", 3, 5.0, 100.0)
        );
        Order order = new Order("010", articles);
        assertEquals(0.0, order.getDiscountedTotal(), EPS);
    }

    @Test
    void getDiscountedTotal_mixedDiscounts() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.0, 0.0),
                new Article("ArticleForTest2", 1, 10.0, 10.0),
                new Article("ArticleForTest3", 2, 10.0, 50.0)
        );
        Order order = new Order("011", articles);
        assertEquals(39.0, order.getDiscountedTotal(), EPS);
    }

    @Test
    void getDiscountedTotal_invalidDiscounts() {
        List<Article> articlesHigh = Arrays.asList(
                new Article("ArticleForTest11", 2, 10.0, 150.0)
        );
        Order orderHigh = new Order("0121", articlesHigh);
        assertThrows(IllegalArgumentException.class, orderHigh::getDiscountedTotal);

        List<Article> articlesLow = Arrays.asList(
                new Article("ArticleForTest21", 3, 5.0, -5.0)
        );
        Order orderLow = new Order("0122", articlesLow);
        assertThrows(IllegalArgumentException.class, orderLow::getDiscountedTotal);
    }

    //P.S. Aquí he probado nueva metodología del testing para mí
    @Test
    void discountedTotal_invariant() {
        List<Article> articles = Arrays.asList(
                new Article("ArticleForTest1", 2, 10.0, 0.0),
                new Article("ArticleForTest2", 1, 15.0, 10.0),
                new Article("ArticleForTest3", 3, 5.0, 50.0)
        );
        Order order = new Order("013", articles);

        double gross = order.getGrossTotal();
        double discounted = order.getDiscountedTotal();

        assertEquals(false, discounted > gross);
    }

}