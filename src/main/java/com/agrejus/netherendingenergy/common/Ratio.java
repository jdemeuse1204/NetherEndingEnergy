package com.agrejus.netherendingenergy.common;

public class Ratio {

    public final int Antecedent;
    public final int Consequent;

    public Ratio(int antecedent, int consequent) {
        this.Antecedent = antecedent;
        this.Consequent = consequent;
    }

    public int get(int index) {
        int[] array = new int[]{this.Antecedent, this.Consequent};
        return array[index];
    }

    public static Ratio add(Ratio valueOne, Ratio valueTwo) {
        /*
         * a/b + c/d = ad + bc / bd
         * */
        int ad = valueOne.Antecedent * valueTwo.Consequent;
        int bc = valueOne.Consequent * valueTwo.Antecedent;
        int bd = valueOne.Consequent * valueTwo.Consequent;
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

            result = Ratio.addMany(result, values[i]);
            i++;
        }

        return result;
    }
}
