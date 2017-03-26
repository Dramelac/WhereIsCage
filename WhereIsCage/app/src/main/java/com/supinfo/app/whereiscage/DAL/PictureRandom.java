package com.supinfo.app.whereiscage.DAL;


import android.graphics.Point;

import com.supinfo.app.whereiscage.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PictureRandom {

    private List<Integer> ListPictures = new ArrayList<Integer>();
    private Random rand = new Random();

    public PictureRandom() {
        ListPictures.add(R.drawable.w_cage1);
        ListPictures.add(R.drawable.w_cage2);
        ListPictures.add(R.drawable.w_cage3);
        ListPictures.add(R.drawable.w_cage4);
        ListPictures.add(R.drawable.w_cage5);

    }

    public int pop(){
        if (ListPictures.size() == 0) return -1;
        int i = rand.nextInt(ListPictures.size());
        int result = ListPictures.get(i);
        ListPictures.remove(i);
        return result;
    }

    public int get(){
        if (ListPictures.size() == 0) return -1;
        return ListPictures.get(rand.nextInt(ListPictures.size()));
    }
}
    /*public int[] pop(){
        if (ListPictures.size() == 0) return null;
        int pos = rand.nextInt(ListPictures.size());
        int[] result = new int[5];
        result[0] = ListPictures.get(pos);
        result[1] = ListPos.get(pos*4);
        result[2] = ListPos.get(pos*4 + 1);
        result[3] = ListPos.get(pos*4 + 2);
        result[4] = ListPos.get(pos*4 + 3);
        ListPictures.remove(pos);
        ListPos.remove(pos*4);
        ListPos.remove(pos*4 + 1);
        ListPos.remove(pos*4 + 2);
        ListPos.remove(pos*4 + 3);
        return result;
    }

    public int[] get(){
        if (ListPictures.size() == 0) return null;
        int[] result = new int[5];
        int pos = rand.nextInt(ListPictures.size());
        result[0] = ListPictures.get(pos);
        result[1] = ListPos.get(pos*4);
        result[2] = ListPos.get(pos*4 + 1);
        result[3] = ListPos.get(pos*4 + 2);
        result[4] = ListPos.get(pos*4 + 3);
        return result;

    }*/