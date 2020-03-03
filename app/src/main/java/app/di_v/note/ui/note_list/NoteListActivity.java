package app.di_v.note.ui.note_list;

import androidx.fragment.app.Fragment;

import app.di_v.note.base.BaseActivity;

public class NoteListActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
