package app.di_v.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.di_v.note.database.NoteBaseHelper;
import app.di_v.note.database.NoteCursorWrapper;
import app.di_v.note.database.NoteDb.NoteTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dmitry Vaganov
 * @version 1.0.1
 */
public class NoteList {
    private static NoteList sNoteList;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteList get(Context context) {
        if (sNoteList == null) {
            sNoteList = new NoteList(context);
        }
        return sNoteList;
    }

    private NoteList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();
    }

    // Adding a new item.
    public void addNote(Note n) {
        ContentValues values = getContentValues(n);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    // Delete an item
    public void deleteNote(Note note) {
        String uuidString = note.getId().toString();
        mDatabase.delete(NoteTable.NAME,
                NoteTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }

    // Updating rows in the database
    public void updateNote(Note note) {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME, values,
                NoteTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NoteCursorWrapper(cursor);
    }

    // Recording and updating databases
    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.COLOR, note.getColor());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteTable.Cols.IMPORTANT, note.isImportant() ? 1 : 0);
        return values;
    }
}
