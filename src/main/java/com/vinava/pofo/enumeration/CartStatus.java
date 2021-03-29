package com.vinava.pofo.enumeration;

public enum CartStatus {
    OPEN(1),
    LOCKED(2), //vendor or admin can edit this
    CLOSED(3);

    int value;

    public int getValue() {
        return this.value;
    }

    CartStatus(int value) {
        this.value = value;
    }

}
