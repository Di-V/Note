package app.di_v.note.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import app.di_v.note.R;

public class ColorPickerFragment extends DialogFragment {
    public static final String EXTRA_COLOR = "app.di_v.android.note.color";
    private static final String ARG_COLOR = "color";

    public static DialogFragment newInstance(int color) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLOR, color);

        DialogFragment fragment = new ColorPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_color)
                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int color;
                        switch (which) {
                            case 0:
                                color = 0xFF03A9F4;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 1:
                                color = 0xFF9E9E9E;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case  2:
                                color = 0xFF4CAF50;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 3:
                                color = 0xFFFF9800;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 4:
                                color = 0xFF9C27B0;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 5:
                                color = 0xFFF44336;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 6:
                                color = 0xFFE91E63;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 7:
                                color = 0xFFFFFFFF;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 8:
                                color = 0xFFFFEB3B;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            case 9:
                                color = 0xFFCDDC39;
                                sendResult(Activity.RESULT_OK, color);
                                break;
                            default:
                                break;
                        }
                    }
                });

        return builder.create();
    }

    private void sendResult(int resultCode, int color) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_COLOR, color);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
