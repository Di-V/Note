package app.di_v.note.repository;

import androidx.lifecycle.LiveData;

import java.util.UUID;

import app.di_v.note.data.database.AppDatabase;
import app.di_v.note.data.database.NoteDao;
import app.di_v.note.data.entity.Note;

public class NoteDetailRepository {
    private NoteDao mNoteDao;
    private LiveData<Note> mNote;

    public NoteDetailRepository(NoteDao dao, UUID uuid) {
        mNoteDao = dao;
        mNote = dao.getNote(String.valueOf(uuid));
    }

    public LiveData<Note> getNote() {
        return mNote;
    }

    public void updateNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.updateNote(note);
        });
    }

    public void deleteNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteNote(note);
        });
    }
}