package app.di_v.note.adapters;

import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import app.di_v.note.R;
import app.di_v.note.data.entity.Note;
import app.di_v.note.ui.note_detail.NoteDetailActivity;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteHolder> {
    private List<Note> mData;

    public void setData(List<Note> notes) {
        mData = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else return 0;
    }

    @Override
    public NoteHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);
        return new NoteHolder(cardView);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, final int position) {
        if (mData != null) {
            Note note = mData.get(position);
            holder.mTitleTextView.setText(note.getTitle());
            holder.mView.setBackgroundColor(note.getColor());
            holder.mDateTextView.setText(DateFormat.format("dd.MM", note.getDate()).toString());
            holder.mImportantImageView.setVisibility(note.isImportant() ? View.VISIBLE : View.GONE);

            holder.mView.setOnClickListener(v -> {
                Intent intent = NoteDetailActivity.newIntent(v.getContext(), UUID.fromString(note.getId()));
                v.getContext().startActivity(intent);
            });
        } else {
            holder.mTitleTextView.setText("No Note");
        }
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mImportantImageView;

        public NoteHolder(View v) {
            super(v);
            mView = v;
            mTitleTextView = v.findViewById(R.id.note_title);
            mDateTextView = v.findViewById(R.id.note_date);
            mImportantImageView = itemView.findViewById(R.id.note_important);
        }
    }
}
