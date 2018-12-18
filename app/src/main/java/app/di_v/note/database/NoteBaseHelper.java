package app.di_v.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.di_v.note.database.NoteDb.NoteTable;

/**
 * Class for working with the database.
 * @author Dmitry Vaganov
 * @version 1.1.0
 */
public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "noteBase.db";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NoteTable.Cols.UUID + ", " +
                NoteTable.Cols.TITLE + ", " +
                NoteTable.Cols.COLOR + ", " +
                NoteTable.Cols.DATE + ", " +
                NoteTable.Cols.IMPORTANT +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}