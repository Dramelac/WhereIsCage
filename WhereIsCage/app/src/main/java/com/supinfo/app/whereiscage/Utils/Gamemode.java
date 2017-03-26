package com.supinfo.app.whereiscage.Utils;


public enum Gamemode {
    Normal(0),
    Chrono(1),
    Chrono_two(2);

    private final int value;
    Gamemode(int value) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
