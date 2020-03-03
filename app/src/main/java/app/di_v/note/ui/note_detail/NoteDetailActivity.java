package app.di_v.note.ui.note_detail;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.UUID;

import app.di_v.note.base.BaseActivity;

public class NoteDetailActivity extends BaseActivity {

    private static final String EXTRA_NOTE_ID = "app.di_v.note.note_id";

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NoteDetailActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);
        return NoteDetailFragment.newInstance(noteId);
    }
}
