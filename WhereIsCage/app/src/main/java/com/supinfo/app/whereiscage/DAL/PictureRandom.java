package com.supinfo.app.whereiscage.DAL;


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
