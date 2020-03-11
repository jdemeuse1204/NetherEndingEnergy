package com.agrejus.netherendingenergy.common.rendering;

public class RectProgression extends Rect {
    private int progression;

    public RectProgression(int top, int left, int right, int bottom) {
        super(top, left, right, bottom);
        this.progression = 0;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public int getProgression() {
        return this.progression;
    }
}
