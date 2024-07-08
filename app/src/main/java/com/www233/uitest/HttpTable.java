package com.www233.uitest;

import java.util.List;
import java.util.Map;

public class HttpTable {
    public static class Bean1{
        public String chapterName, title, niceDate;
        public double publishTime;
        public double id;
    }
    public static class Bean0{
        public List<Bean1> datas;
    }

    public Bean0 data;

}
