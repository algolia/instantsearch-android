package com.algolia.instantsearch.model;

public class Facet {
    final private String name;
    private int count;
    private boolean isEnabled;

    public Facet(String name, int count, boolean isEnabled) {
        this.name = name;
        this.count = count;
        this.isEnabled = isEnabled;
    }

    public Facet(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "Facet{" + "name='" + name + ", count=" + count + ", enabled=" + isEnabled + '}';
    }
}
