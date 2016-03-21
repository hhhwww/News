package com.xd.reviewsbj.bean;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/2.
 */
public class NewsData {

    public int retcode;
    public ArrayList<NewsLeftData> data;

    public class NewsLeftData {
        public String id;
        public String title;
        public String type;
        public String url;
        public ArrayList<NewsTabData> children;
    }

    public class NewsTabData {
        public String id;
        public String title;
        public int type;
        public String url;
    }
}
