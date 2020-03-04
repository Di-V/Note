package app.di_v.note.ui.note_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

import app.di_v.note.R;
import app.di_v.note.ui.ColorPickerFragment;
import app.di_v.note.ui.DatePickerFragment;
import app.di_v.note.ui.ViewModelFactory;

public class NoteDetailFragment extends Fragment{
    private static final String ARG_NOTE_ID = "note_id";
    private static final String DIALOG_COLOR = "DialogColor";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_COLOR = 1;
    private static final int REQUEST_DATE = 0;
    private Boolean check = true;

    private UUID mNoteId;
    private NoteDetailViewModel mViewModel;
    private EditText mTitleField;
    private FloatingActionButton fab;

    public static NoteDetailFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);

        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mNoteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);

        initUi(view);
        observeNote();

        return view;
    }

    private void observeNote() {
        mViewModel.getNoteLiveData().observe(getViewLifecycleOwner(), note -> {
        try {
            mTitleField.setBackgroundColor(note.getColor());
            check = note.isImportant();
        } catch (Exception e) {}
        });
    }

    private void initUi(View view){
        mViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication(), mNoteId)).get(NoteDetailViewModel.class);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.bottom_app_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        fab = view.findViewById(R.id.fab_back);
        fab.setOnClickListener(v -> getActivity().onBackPressed());

        mTitleField = view.findViewById(R.id.note_title);
        try {
            mTitleField.setText(mViewModel.getNoteLiveData().getValue().getTitle());
        } catch (Exception e) {

        }
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 1) {
                    mViewModel.setTitle(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_important);
        if (check) {
            menuItem.setIcon(R.drawable.ic_important);
        } else {
            menuItem.setIcon(R.drawable.ic_important_false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_important:
                if (check) {
                    item.setIcon(R.drawable.ic_important_false);
                    check = false;
                } else {
                    item.setIcon(R.drawable.ic_important);
                    check = true;
                }
                mViewModel.setImportant(check);
                return true;
            case R.id.menu_color:
                DialogFragment newFragment = new ColorPickerFragment().newInstance(R.color.white);
                newFragment.setTargetFragment(NoteDetailFragment.this, REQUEST_COLOR);
                newFragment.show(getParentFragmentManager(), DIALOG_COLOR);
                return true;
            case R.id.menu_delete:
                getActivity().finish();
                mViewModel.deleteNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mViewModel.setDate(date);
        }
        if (requestCode == REQUEST_COLOR) {
            int color = (int) data.getSerializableExtra(ColorPickerFragment.EXTRA_COLOR);
            mViewModel.setColor(color);
        }
    }
}
