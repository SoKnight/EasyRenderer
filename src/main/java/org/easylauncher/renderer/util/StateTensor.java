package org.easylauncher.renderer.util;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.function.IntFunction;

@Getter
@Accessors(fluent = true)
public class StateTensor<T> {

    private final int size;
    private final T[] items;

    public StateTensor(int size, IntFunction<T[]> instantiator) {
        this(size, instantiator.apply((int) Math.pow(2, size)));
    }

    protected StateTensor(int size, T[] items) {
        this.size = size;
        this.items = items;
    }

    public T get(boolean... states) {
        validateStates(states);
        return items[index(states)];
    }

    public void set(T item, boolean... states) {
        validateStates(states);
        this.items[index(states)] = item;
    }

    protected final int index(boolean[] states) {
        int index = 0;

        for (int i = 0; i < states.length; i++)
            if (states[i])
                index |= (1 << i);

        return index;
    }

    protected final void validateStates(boolean[] states) {
        if (states.length != size) {
            throw new IllegalArgumentException("States count must be equal to hyper-cube size!");
        }
    }

}
