package com.custom.serialization.test;

import com.custom.serialization.CustomSerializable;

public class TestClass implements CustomSerializable {
    private int id;
    private String name;
    private boolean isActive;

    public TestClass(int id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    @Override
    public int getVersion() {
        return 1; // Version 1
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
