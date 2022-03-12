package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }

    public ObjectForMessage copy() {
        ObjectForMessage result = new ObjectForMessage();
        result.setData(List.copyOf(data));
        return result;
    }
}
