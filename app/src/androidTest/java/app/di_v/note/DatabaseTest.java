package app.di_v.note;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import app.di_v.note.data.database.AppDatabase;
import app.di_v.note.data.database.NoteDao;
import app.di_v.note.data.entity.Note;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private NoteDao mNoteDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mNoteDao = mDb.noteDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetWord() throws Exception {
        Note note = new Note(UUID.randomUUID());
        mNoteDao.insertNote(note);
        List<Note> allWords = LiveDataTestUtil.getValue(mNoteDao.getSortedNoteById());
        assertEquals(allWords.get(0).getId(), note.getId());
    }

    @Test
    public void getAllNotes() throws Exception {
        Note note = new Note(UUID.randomUUID());
        mNoteDao.insertNote(note);
        Note note2 = new Note(UUID.randomUUID());
        mNoteDao.insertNote(note2);
        List<Note> allWords = LiveDataTestUtil.getValue(mNoteDao.getSortedNoteById());
        assertEquals(allWords.get(0).getId(), note.getId());
        assertEquals(allWords.get(1).getId(), note2.getId());
    }

    @Test
    public void deleteAll() throws Exception {
        Note note = new Note(UUID.randomUUID());
        mNoteDao.insertNote(note);
        Note note2 = new Note(UUID.randomUUID());
        mNoteDao.insertNote(note2);
        mNoteDao.deleteAll();
        List<Note> allWords = LiveDataTestUtil.getValue(mNoteDao.getSortedNoteById());
        assertTrue(allWords.isEmpty());
    }
}