package app.di_v.note.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import app.di_v.note.ui.note_detail.NoteDetailViewModel;

public class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final UUID mId;
    private final Application mApplication;

    public ViewModelFactory(Application application, UUID id) {
        super(application);
        mApplication = application;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteDetailViewModel.class) {
            return (T) new NoteDetailViewModel(mApplication, mId);
        }
        return null;
    }
}
