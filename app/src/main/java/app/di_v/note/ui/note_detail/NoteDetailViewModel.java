package app.di_v.note.ui.note_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.UUID;

import app.di_v.note.data.database.AppDatabase;
import app.di_v.note.data.database.NoteDao;
import app.di_v.note.data.entity.Note;
import app.di_v.note.repository.NoteDetailRepository;

public class NoteDetailViewModel extends AndroidViewModel {
    private LiveData<Note> mNoteLiveData;

    private NoteDetailRepository mRepository;

    public NoteDetailViewModel(@NonNull Application application, UUID id) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        NoteDao dao = database.noteDao();
        mRepository = new NoteDetailRepository(dao, id);
        mNoteLiveData = mRepository.getNote();
    }

    public LiveData<Note> getNoteLiveData() {
        return mNoteLiveData;
    }

    public void setDate(Date date) {
        Note note = mNoteLiveData.getValue();
        note.setDate(date);
        mRepository.updateNote(note);
    }

    public void setColor(int color) {
        Note note = mNoteLiveData.getValue();
        note.setColor(color);
        mRepository.updateNote(note);
    }

    public void setImportant(boolean isChecked) {
        Note note = mNoteLiveData.getValue();
        note.setImportant(isChecked);
        mRepository.updateNote(note);
    }

    public void setTitle(String string) {
        if (string != null) {
            Note note = mNoteLiveData.getValue();
            note.setTitle(string);
            mRepository.updateNote(note);
        }
    }

    public void deleteNote() {
        mRepository.deleteNote(mNoteLiveData.getValue());
    }
}