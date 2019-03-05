package com.divercity.android.features.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.core.ui.AnimateHorizontalProgressBar;

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

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}