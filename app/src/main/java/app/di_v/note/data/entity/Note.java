package app.di_v.note.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

import app.di_v.note.data.DateConverter;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "note_id")
    private String mId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @NonNull
    @ColumnInfo(name = "color")
    private int mColor;
    @NonNull
    @ColumnInfo(name = "date")
    private Date mDate;
    @NonNull
    @ColumnInfo(name = "important")
    private boolean mImportant;

    public Note(){
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mId = id.toString();
        mColor = 0xFFFFFFFF;
        mDate = new Date();
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean important) {
        mImportant = important;
    }
}
