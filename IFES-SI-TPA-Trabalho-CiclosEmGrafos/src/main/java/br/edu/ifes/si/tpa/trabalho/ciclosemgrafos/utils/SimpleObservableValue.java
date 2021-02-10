package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.utils;

import javafx.beans.value.ObservableValueBase;

public class SimpleObservableValue<T> extends ObservableValueBase<T> {

    private T value;

    public SimpleObservableValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
