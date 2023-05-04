package com.github.bawey;


public class SparseBooleanMegaArray {
    private final int capacity;
    private final int startingKey;

    private final char[] data;

    public SparseBooleanMegaArray(int capacity, int startingKey) {
        this.capacity = capacity;
        this.startingKey = startingKey;
        data = new char[capacity / 8 + 1];
    }

    private int getCellNo(int key) {
        return (key - startingKey) >> 3;
    }

    private char getCellBitMask(int key) {
        return (char) (1 << (key % 8));
    }

    public void setValue(int key, boolean value) {
        int cellNo = getCellNo(key);
        char bitMask = getCellBitMask(key);
        if (value) {
            data[cellNo] = (char) (data[cellNo] | bitMask);
        } else {
            bitMask = (char) (0xff ^ bitMask);
            data[cellNo] = (char) (data[cellNo] & bitMask);
        }
    }

    public boolean readValue(int key) {
        int cellNo = getCellNo(key);
        char bitMask = getCellBitMask(key);

        return (data[cellNo] & bitMask) != 0;
    }
}
