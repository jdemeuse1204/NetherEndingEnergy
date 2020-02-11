package com.agrejus.netherendingenergy.common;

import net.minecraft.util.IIntArray;

import java.util.ArrayList;
import java.util.function.Supplier;

public class IntArraySupplierReferenceHolder implements IIntArray {

    public IntArraySupplierReferenceHolder(Supplier<Integer>... values) {
        array = new ArrayList<Supplier<Integer>>();

        for (int i = 0; i < values.length; i++) {
            array.add(values[i]);
        }
    }

    private ArrayList<Supplier<Integer>> array;

    @Override
    public int get(int index) {
        Supplier supplier = array.get(index);
        return (int)supplier.get();
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