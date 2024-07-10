package com.www233.uitest.charttest;


public class PracticeChartInfoItem {
    public String key, value;
    int value_color;

    public PracticeChartInfoItem(String key, String value) {
        this.key = key;
        this.value = value;
        this.value_color = -1;  // 不赋值，则设为默认值
    }
    public PracticeChartInfoItem(String key, String value, int color) {
        this.key = key;
        this.value = value;
        this.value_color = color;
    }
}
