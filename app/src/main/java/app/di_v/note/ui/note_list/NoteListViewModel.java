package app.di_v.note.ui.note_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.di_v.note.data.database.AppDatabase;
import app.di_v.note.data.database.NoteDao;
import app.di_v.note.data.entity.Note;
import app.di_v.note.repository.NoteListRepository;

public class NoteListViewModel extends AndroidViewModel {
    private LiveData<List<Note>> mNoteListLiveData;

    private NoteListRepository mRepository;

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        NoteDao dao = database.noteDao();
        mRepository = new NoteListRepository(dao);
        mNoteListLiveData = mRepository.getNotes();
    }

    public LiveData<List<Note>> getNoteListLiveData() {
        return mNoteListLiveData;
    }

    public void createNote(Note note) {
        mRepository.insertNote(note);
    }

    public void swipeToAction(int position) {
        Note note = mNoteListLiveData.getValue().get(position);
        mRepository.deleteNote(note);
    }
}
