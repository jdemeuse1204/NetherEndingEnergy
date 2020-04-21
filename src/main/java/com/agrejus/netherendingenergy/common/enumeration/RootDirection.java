package com.agrejus.netherendingenergy.common.enumeration;

public enum RootDirection {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),

    NORTHEAST("northeast"),
    NORTHWEST("northwest"),

    SOUTHEAST("southeast"),
    SOUTHWEST("southwest");

    private String name;
    private RootDirection(String name) {
        this.name = name;
    }
}
