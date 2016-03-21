package com.xd.reviewsbj.bean;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/4.
 */
public class PictureData {
    public int retcode;
    public PictureInerData data;

    public class PictureInerData {
        public String title;
        public ArrayList<NewsData> news;
    }

    public class NewsData {
        public String listimage;
        public String title;
    }
}
