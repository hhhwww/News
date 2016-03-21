package com.xd.reviewsbj.bean;

import java.util.ArrayList;

/**
 * Created by hhhhwei on 16/3/3.
 */
public class RealContentsData {

    public int retcode;
    public RealDetailData data;

    public class RealDetailData {
        public String more;
        public String title;
        public ArrayList<RealNewsData> news;
        public ArrayList<RealTopNewsData> topnews;
    }

    public class RealNewsData {
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    public class RealTopNewsData {
        public String id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
