package org.example;

import java.util.HashMap;

public class DataFrame {
    private Object index;
    private HashMap<String, Object[]> content = new HashMap<>();

    DataFrame(HashMap<String, Object[]> content, Object index) {
        this.setContent(content);
        this.setIndex(index);
    }

    DataFrame(HashMap<String, Object[]> content) {
        this.setContent(content);
    }

    public Object getIndex() {
        return index;
    }

    public void setIndex(Object index) {
        this.index = index;
    }

    public HashMap<String, Object[]> getContent() {
        return content;
    }

    public void setContent(HashMap<String, Object[]> content) {
        this.content = content;
    }
}
