package app.di_v.note.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import app.di_v.note.data.database.AppDatabase;
import app.di_v.note.data.database.NoteDao;
import app.di_v.note.data.entity.Note;

public class NoteListRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mNotes;

    public NoteListRepository(NoteDao dao) {
        mNoteDao = dao;
        mNotes = dao.getNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return mNotes;
    }


    public void insertNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insertNote(note);
        });
    }

    public void getNote(String id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.getNote(id);
        });
    }

    public void deleteNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteNote(note);
        });
    }
}
