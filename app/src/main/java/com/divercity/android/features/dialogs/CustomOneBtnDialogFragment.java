package com.divercity.android.features.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by lucas on 17/05/2018.
 */

public class CustomOneBtnDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "argTitle";
    private static final String ARG_MESSAGE = "argMessage";
    private static final String ARG_BTN_TEXT = "argBtnText";

    private OnBtnListener mListener;
    private String mTitle;
    private String mMessage;
    private String mBtnText;

    public interface OnBtnListener {
        void onBtnClick();
    }

    public static CustomOneBtnDialogFragment newInstance(String title, String message, String btnText) {
        CustomOneBtnDialogFragment fragment = new CustomOneBtnDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_BTN_TEXT, btnText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mMessage = getArguments().getString(ARG_MESSAGE);
            mBtnText = getArguments().getString(ARG_BTN_TEXT);
        }
    }

    public void setListener(OnBtnListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton(mBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onBtnClick();
                    }
                });
        return builder.create();
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

}