package app.di_v.note;

import java.util.Date;
import java.util.UUID;

/**
 * @author Dmitry Vaganov
 * @version 1.1.0
 */
public class Note {
    private UUID mId;
    private String mTitle;
    private int mColor;
    private Date mDate;
    private boolean mImportant;

    public Note(){
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mId = id;
        mColor = 0xFFFFFFFF;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isImportant() {
        return mImportant;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setImportant(boolean important) {
        mImportant = important;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
