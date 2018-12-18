package app.di_v.note;

import android.support.v4.app.Fragment;

/**
 * @author Dmitry Vaganov
 * @version 1.3.1
 */
public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
