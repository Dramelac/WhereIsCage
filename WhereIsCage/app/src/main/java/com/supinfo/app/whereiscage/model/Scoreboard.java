package com.supinfo.app.whereiscage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Scoreboard implements Parcelable {
    private long id;
    private String player;
    private long score;
    private long gamemode;

    protected Scoreboard(Parcel in) {
        setId(in.readLong());
        setPlayer(in.readString());
        setScore(in.readLong());
        setGamemode(in.readLong());
    }

    public Scoreboard(){

    }

    public static final Creator<Scoreboard> CREATOR = new Creator<Scoreboard>() {
        @Override
        public Scoreboard createFromParcel(Parcel in) {
            return new Scoreboard(in);
        }

        @Override
        public Scoreboard[] newArray(int size) {
            return new Scoreboard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPlayer());
        dest.writeLong(getScore());
        dest.writeLong(getGamemode());
    }

    @Override
    public String toString(){
        return player + ", score : " + score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getGamemode() {
        return gamemode;
    }

    public void setGamemode(long gamemode) {
        this.gamemode = gamemode;
    }
}
