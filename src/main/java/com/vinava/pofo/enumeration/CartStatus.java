package com.vinava.pofo.enumeration;

public enum CartStatus {
    OPEN(1),
    LOCKED(2),
    CLOSED(3);

    int value;

    public int getValue() {
        return this.value;
    }

    CartStatus(int value) {
        this.value = value;
    }

}
