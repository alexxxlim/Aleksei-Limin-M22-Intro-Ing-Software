
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.Searcher;

public class SearcherTestCase {

    private final Searcher searcher = new Searcher();
    private final List<String> words = Arrays.asList("apple", "banana", "cherry", "apricot", "pineapple");

    // searchWord
    @Test
    void searchWord_returnsTrueWhenPresent() {
        assertTrue(searcher.searchWord("banana", words));
    }

    @Test
    void searchWord_returnsFalseWhenAbsent() {
        assertFalse(searcher.searchWord("orange", words));
    }

    @Test
    void getWordByIndex_validIndexReturnsWord() {
        assertEquals("apple", searcher.getWordByIndex(words, 0));
        assertEquals("cherry", searcher.getWordByIndex(words, 2));
    }

    @Test
    void getWordByIndex_invalidNegativeReturnsNull() {
        assertNull(searcher.getWordByIndex(words, -1));
    }

    @Test
    void getWordByIndex_invalidLargeReturnsNull() {
        assertNull(searcher.getWordByIndex(words, 999));
    }

    @Test
    void searchByPrefix_returnsOnlyWordsWithPrefix() {
        List<String> result = searcher.searchByPrefix("ap", words);
        assertEquals(Arrays.asList("apple", "apricot"), result);
    }

    @Test
    void searchByPrefix_returnsEmptyWhenNoMatches() {
        assertTrue(searcher.searchByPrefix("zz", words).isEmpty());
    }

    @Test
    void filterByKeyword_returnsAllContainingKeyword() {
        List<String> result = searcher.filterByKeyword("pp", words);
        assertEquals(Arrays.asList("apple", "pineapple"), result);
    }

    @Test
    void filterByKeyword_returnsEmptyWhenNoElementContainsKeyword() {
        assertTrue(searcher.filterByKeyword("zzz", words).isEmpty());
    }

    @Test
    void searchExactPhrase_trueWhenFirstElementMatches() {
        assertTrue(searcher.searchExactPhrase("apple", words));
    }

    @Test
    void searchExactPhrase_trueWhenLaterElementMatches() {
        assertTrue(searcher.searchExactPhrase("cherry", words));
    }

    @Test
    void searchExactPhrase_falseWhenPhraseNotPresent() {
        assertFalse(searcher.searchExactPhrase("mango", words));
    }
}