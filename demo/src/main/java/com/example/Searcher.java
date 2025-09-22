package com.example;

import java.util.ArrayList;
import java.util.List;

public class Searcher {

    // checks if the phrase exists in the list
    public boolean searchExactPhrase(String phrase, List<String> list) {
        if (phrase == null || list == null) {
            return false;
        }
        for (String item : list) {
            if (phrase.equals(item)) {
                return true;
            }
        }
        return false;
    }

    // Simple contains check
    public boolean searchWord(String word, List<String> list) {
        return list.contains(word);
    }

    // Get element by index safely
    public String getWordByIndex(List<String> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null; // Avoid IndexOutOfBounds
    }

    // New: find elements starting with a given prefix
    public List<String> searchByPrefix(String prefix, List<String> list) {
        List<String> results = new ArrayList<>();
        for (String element : list) {
            if (element.startsWith(prefix)) {
                results.add(element);
            }
        }
        return results;
    }

    // New: filter all elements that contain a given keyword
    public List<String> filterByKeyword(String keyword, List<String> list) {
        List<String> results = new ArrayList<>();
        for (String element : list) {
            if (element.contains(keyword)) {
                results.add(element);
            }
        }
        return results;
    }
}