package app.di_v.note;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID = "app.di_v.note.note_id";            //identifier

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);
        return NoteFragment.newInstance(noteId);
    }
}
