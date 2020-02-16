package com.agrejus.netherendingenergy.common;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class Ratio implements INBTSerializable<CompoundNBT> {

    private int antecedent;
    private int consequent;

    public Ratio(int antecedent, int consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
    }

    public int getAntecedent() {
        return antecedent;
    }

    public float asPercent() {
        return (float)antecedent / (float)consequent;
    }

    public int getConsequent() {
        return consequent;
    }

    public int get(int index) {
        int[] array = new int[]{this.antecedent, this.consequent};
        return array[index];
    }

    public static Ratio add(Ratio valueOne, Ratio valueTwo) {
        /*
         * a/b + c/d = ad + bc / bd
         * */
        int ad = valueOne.antecedent * valueTwo.consequent;
        int bc = valueOne.consequent * valueTwo.antecedent;
        int bd = valueOne.consequent * valueTwo.consequent;
        return new Ratio(ad + bc, bd);
    }

    public static Ratio addMany(Ratio... values) {
        Ratio result = values[0];

        if (values.length == 1) {
            return result;
        }

        for (int i = 0; i < values.length; i++) {

            if (i == 0) {
                continue;
            }

            result = Ratio.add(result, values[i]);
            i++;
        }

        return result;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Antecedent", this.antecedent);
        nbt.putInt("Consequent", this.consequent);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.antecedent = nbt.getInt("Antecedent");
        this.antecedent = nbt.getInt("Consequent");
    }
}
