package app.di_v.note.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import app.di_v.note.Note;
import app.di_v.note.database.NoteDb.NoteTable;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        int isImportant = getInt(getColumnIndex(NoteTable.Cols.IMPORTANT));

        Note note = new Note(UUID.fromString(uuidString));
        note.setTitle(title);
        note.setDate(new Date(date));
        note.setImportant(isImportant != 0);

        return note;
    }
}