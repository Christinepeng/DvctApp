package com.divercity.app.features.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by lucas on 17/05/2018.
 */

public class CustomTwoBtnDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "argTitle";
    private static final String ARG_MESSAGE = "argMessage";
    private static final String ARG_BTN_POS_TEXT = "argBtnPosText";
    private static final String ARG_BTN_NEG_TEXT = "argBtnNegText";

    private OnBtnListener mListener;
    private String mTitle;
    private String mMessage;
    private String mBtnPosText;
    private String mBtnNegText;

    public interface OnBtnListener {
        void onPositiveBtnClick();

        void onNegativeBtnClick();
    }

    public static CustomTwoBtnDialogFragment newInstance(String title, String message, String btnPosTxt, String btnNegTxt) {
        CustomTwoBtnDialogFragment fragment = new CustomTwoBtnDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_BTN_POS_TEXT, btnPosTxt);
        args.putString(ARG_BTN_NEG_TEXT, btnNegTxt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mMessage = getArguments().getString(ARG_MESSAGE);
            mBtnPosText = getArguments().getString(ARG_BTN_POS_TEXT);
            mBtnNegText = getArguments().getString(ARG_BTN_NEG_TEXT);
        }
        setCancelable(true);
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
                .setPositiveButton(mBtnPosText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveBtnClick();
                    }
                })
                .setNegativeButton(mBtnNegText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeBtnClick();
                    }
                });
        return builder.create();
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

}