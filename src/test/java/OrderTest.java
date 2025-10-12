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

    //P.S. Aquí he probado nueva metodología del testing para mí
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
}



