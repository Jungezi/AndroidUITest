package com.www233.uitest.flowtest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TextDataItem")
public class TextDataItem{
    public String data_content;
    public int data_size;

    @PrimaryKey(autoGenerate = true)
    long key = 0;

    public TextDataItem(String data_content, int data_size) {
        this.data_content = data_content;
        this.data_size = data_size;
    }
}