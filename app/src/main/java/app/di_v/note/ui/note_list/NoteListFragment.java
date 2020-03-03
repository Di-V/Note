package app.di_v.note.ui.note_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import app.di_v.note.R;
import app.di_v.note.adapters.NoteListAdapter;
import app.di_v.note.data.entity.Note;
import app.di_v.note.ui.note_detail.NoteDetailActivity;

public class NoteListFragment extends Fragment {
    private RecyclerView mNoteRecyclerView;
    private NoteListAdapter mAdapter;
    private NoteListViewModel viewModel;

    private static final String APP_PREFERENCES = "notes_settings";
    private static final String APP_PREFERENCES_SPAN_COUNT = "span_counter";
    private SharedPreferences mSettings;

    private int spanCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        initUi(view);
        observeNotes();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_SPAN_COUNT, spanCount);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePrefUI();
    }

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

    private void initUi(View view) {
        mAdapter = new NoteListAdapter();
        mNoteRecyclerView = view.findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoteRecyclerView.setAdapter(mAdapter);

        viewModel = ViewModelProviders.of(this).get(NoteListViewModel.class);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    viewModel.swipeToAction(viewHolder.getAdapterPosition());
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(mNoteRecyclerView);
        updatePrefUI();

        // floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.note_fab);

        fab.setImageResource(R.drawable.ic_action_add);
        fab.setOnClickListener(v -> {
            Note note = new Note();
            viewModel.createNote(note);
            Intent intent = NoteDetailActivity.newIntent(getActivity(), UUID.fromString(note.getId()));
            startActivity(intent);
        });
    }

    private void updatePrefUI() {
        if (mSettings.contains(APP_PREFERENCES_SPAN_COUNT)) {
            spanCount = mSettings.getInt(APP_PREFERENCES_SPAN_COUNT, 0);
            mNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                    StaggeredGridLayoutManager.VERTICAL));
        }   else  mNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                    StaggeredGridLayoutManager.VERTICAL));
    }

    private void observeNotes() {
        viewModel.getNoteListLiveData().observe(getViewLifecycleOwner(), notes -> {
            mAdapter.setData(notes);
        });
    }
}
