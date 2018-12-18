package app.di_v.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @author Dmitry Vaganov
 * @version 1.3.1
 */
public class NoteListFragment extends Fragment {
    private RecyclerView mNoteRecyclerView;
    private NoteAdapter mAdapter;

    private static final String TAG = "NoteListFragment";
    private static final String APP_PREFERENCES = "notes_settings";
    private static final String APP_PREFERENCES_SPAN_COUNT = "span_counter";
    private SharedPreferences mSettings;

    private int spanCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setHasOptionsMenu(true);
        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mNoteRecyclerView = view.findViewById(R.id.note_recycler_view);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    mAdapter.swipeToDelete(viewHolder.getAdapterPosition());

                    NoteList noteList = NoteList.get(getActivity());
                    List<Note> notes = noteList.getNotes();

                    if (mAdapter == null) {
                        mAdapter = new NoteAdapter(notes);
                        mNoteRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setNotes(notes);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(mNoteRecyclerView);
        updateUI();

        // floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.note_fab);

        fab.setImageResource(R.drawable.ic_action_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                NoteList.get(getActivity()).addNote(note);
                Intent intent = NoteActivity.newIntent(getActivity(), note.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_SPAN_COUNT, spanCount);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        updateUI();
    }

    /**
     * Populating the menu resource.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.list_type);
        if (spanCount == 1) {
            menuItem.setIcon(R.drawable.ic_action_grid);
        } else {
            menuItem.setIcon(R.drawable.ic_action_list);
        }
    }

    /**
     * Response to menu selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_type:
                if (spanCount == 1) {
                    spanCount = 2;
                    item.setIcon(R.drawable.ic_action_list);
                } else {
                    spanCount = 1;
                    item.setIcon(R.drawable.ic_action_grid);
                }
                mNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                        StaggeredGridLayoutManager.VERTICAL));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        NoteList noteList = NoteList.get(getActivity());
        List<Note> notes = noteList.getNotes();

        if (mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(notes);
            mAdapter.notifyDataSetChanged();
        }

        if (mSettings.contains(APP_PREFERENCES_SPAN_COUNT)) {
            spanCount = mSettings.getInt(APP_PREFERENCES_SPAN_COUNT, 0);
            mNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                    StaggeredGridLayoutManager.VERTICAL));
        }   else  mNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                    StaggeredGridLayoutManager.VERTICAL));
    }

    private class NoteHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Note mNote;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mImportantImageView;

        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_note, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.note_title);
            mDateTextView = itemView.findViewById(R.id.note_date);
            mImportantImageView = itemView.findViewById(R.id.note_important);
        }

        public void bind(Note note) {
            mNote = note;
            mTitleTextView.setText(mNote.getTitle());
            mDateTextView.setText(DateFormat.format("dd.MM", mNote.getDate()).toString());
            mImportantImageView.setVisibility(note.isImportant() ? View.VISIBLE : View.GONE);
            itemView.setBackgroundColor(mNote.getColor());
        }

        @Override
        public void onClick(View view) {
            Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new NoteHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bind(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }

        public void swipeToDelete(int position) {
            NoteList noteList = NoteList.get(getActivity());
            Note note = mNotes.get(position);
            noteList.deleteNote(note);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, noteList.getNotes().size());
            Toast.makeText(getContext(), R.string.toast_delete_note, Toast.LENGTH_SHORT).show();
        }
    }
}
