package com.agrejus.netherendingenergy.common.rendering;

public class Rect {

    public Rect(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    private int top;
    private int left;
    private int right;
    private int bottom;

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getHeight() {
        return this.bottom - this.top;
    }

    public int getWidth() {
        return this.right - this.left;
    }

    public int getComputedFillHeight(int capacity, int amountFilled) {

        int amount = (int) (this.getHeight() * (amountFilled / (float) capacity));

        return this.top + (this.getHeight() - amount);
    }

    public int getComputedFillWidth(int capacity, int amountFilled) {

        int amount = (int) (this.getWidth() * (amountFilled / (float) capacity));

        return this.left + (this.getWidth() - amount);
    }
}
