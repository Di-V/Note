package app.di_v.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

/**
 * @author Dmitry Vaganov
 * @version 1.3.5
 */
public class NoteFragment extends Fragment{
    private static final String TAG = "NoteFragment";
    private static final String ARG_NOTE_ID = "note_id";
    private static final String DIALOG_COLOR = "DialogColor";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_COLOR = 1;
    private static final int REQUEST_DATE = 0;

    private Note mNote;
    private EditText mTitleField;
    private CheckBox mImportantCheckbox;
    private ImageButton mButtonColor;

    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteList.get(getActivity()).getNote(noteId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        /*if (mTitleField.getText().toString().equals(""))
        {
            NoteList.get(getActivity()).deleteNote(mNote);
        } else NoteList.get(getActivity()).updateNote(mNote);*/
        NoteList.get(getActivity()).updateNote(mNote);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    // Populating the menu resource.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
        // Update title date in menu
        MenuItem dateItem = menu.findItem(R.id.date);
        dateItem.setTitle(DateFormat.format("dd.MM.yyyy", mNote.getDate()).toString());
    }

    // Response to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager manager = getFragmentManager();
        switch (item.getItemId()) {
            /*case R.id.color:
                DialogFragment newFragment = new ColorPickerFragment().newInstance(mNote.getColor());
                newFragment.setTargetFragment(NoteFragment.this, REQUEST_COLOR);
                newFragment.show(manager, DIALOG_COLOR);
                return true;*/
            case R.id.date:
                DatePickerFragment dialog = DatePickerFragment.newInstance(mNote.getDate());
                dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                return true;
            case R.id.delete_note:
                NoteList.get(getActivity()).deleteNote(mNote);
                Intent intent = new Intent(getActivity(), NoteListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This does work
        MenuItem dateItem = menu.findItem(R.id.date);
        dateItem.setTitle(DateFormat.format("dd.MM.yyyy", mNote.getDate()).toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mTitleField = view.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle());
        mTitleField.setBackgroundColor(mNote.getColor());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                FloatingActionButton fab = getActivity().findViewById(R.id.note_fab);
                if (s.toString().equals("")) {
                    fab.setImageResource(R.drawable.ic_action_close);
                } else fab.setImageResource(R.drawable.ic_action_ok);
            }
        });

        // Checkbox
        mImportantCheckbox = view.findViewById(R.id.note_important);
        mImportantCheckbox.setChecked(mNote.isImportant());
        mImportantCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNote.setImportant(isChecked);
            }
        });

        //Button color
        mButtonColor = view.findViewById(R.id.button_color);
        //mButtonColor.setColorFilter(mNote.getColor());
        mButtonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new ColorPickerFragment().newInstance(mNote.getColor());
                newFragment.setTargetFragment(NoteFragment.this, REQUEST_COLOR);
                newFragment.show(getFragmentManager(), DIALOG_COLOR);
            }
        });

        // floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.note_fab);

        fab.setImageResource(R.drawable.ic_action_close);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mNote.setDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_COLOR) {
            int color = (int) data.getSerializableExtra(ColorPickerFragment.EXTRA_COLOR);
            mNote.setColor(color);
            updateColor();
        }
    }

    private void updateColor() {
        int color = mNote.getColor();
        mTitleField.setBackgroundColor(color);
        //mButtonColor.setColorFilter(color);
    }

    private void updateDate() {
        invalidateOptionsMenu(getActivity());
    }
}
