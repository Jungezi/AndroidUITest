package com.www233.uitest.flowtest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TextDataDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTextDataItem(TextDataItem td);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTextDataItems(List<TextDataItem> tds);
    @Update
    void updateTextDataItem(TextDataItem item);
    @Delete
    void deleteTextDataItem(TextDataItem item);
    @Query("delete from TextDataItem")
    void deleteTextDataItem();
    @Query("select * from TextDataItem")
    List<TextDataItem> getTextDataItem();

}