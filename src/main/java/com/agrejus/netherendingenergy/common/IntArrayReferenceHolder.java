package com.agrejus.netherendingenergy.common;

import net.minecraft.util.IIntArray;

import java.util.ArrayList;

public class IntArrayReferenceHolder implements IIntArray {

    public IntArrayReferenceHolder(Integer... values) {
        array = new ArrayList<Integer>();

        for (int i = 0; i < values.length; i++) {
            array.add(values[i]);
        }
    }

    private ArrayList<Integer> array;

    @Override
    public int get(int index) {
        return array.get(index);
    }

    @Override
    public void set(int index, int value) {
        // Not needed
    }

    @Override
    public int size() {
        return array.size();
    }
}