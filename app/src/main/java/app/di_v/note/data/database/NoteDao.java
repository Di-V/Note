package app.di_v.note.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.di_v.note.data.entity.Note;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * FROM note_table WHERE note_id LIKE :id")
    LiveData<Note> getNote(String id);

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * from note_table ORDER BY note_id ASC")
    LiveData<List<Note>> getSortedNoteById();
}
