package com.www233.uitest;

import java.util.ArrayList;

public class TableItem {
    public String title;
    public ArrayList<String> tag;
    public double number, number_sub;

    public TableItem(String title, ArrayList<String> tag, double number, double numberSub) {
        this.title = title;
        this.tag = tag;
        this.number = number;
        number_sub = numberSub;
    }
}
