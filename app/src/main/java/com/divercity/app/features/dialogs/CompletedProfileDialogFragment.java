package com.divercity.app.features.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.divercity.app.R;
import com.divercity.app.core.ui.AnimateHorizontalProgressBar;

/**
 * Created by lucas on 18/03/2018.
 */

public class CompletedProfileDialogFragment extends DialogFragment {

    public static CompletedProfileDialogFragment newInstance() {
        return new CompletedProfileDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_complete_profile, null);

        AnimateHorizontalProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setProgressWithAnim(100);

        TextView progressText = dialogView.findViewById(R.id.txt_progress);
        progressText.setText("100%");

        dialogView.findViewById(R.id.dlg_cmpl_prf_btn_thanks).setOnClickListener(view -> {
            dismiss();
        });
        builder.setView(dialogView);
        return builder.create();
    }
}