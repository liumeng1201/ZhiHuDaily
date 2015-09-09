package com.android.liumeng.zhihudaily.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.android.liumeng.zhihudaily.utils.StringUtils;

/**
 * Created by liumeng on 2015/9/9.
 */
public class CustomDialog extends DialogFragment {
    public static final String TITLE = "dialog_title";
    public static final String MESSAGE = "dialog_message";
    public static final String POSITIVE = "dialog_positive";
    public static final String NEGATIVE = "dialog_negative";
    private CustomDialogClickListener clickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        clickListener = (CustomDialogClickListener) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialog_title = getArguments().getString(TITLE);
        String dialog_message = getArguments().getString(MESSAGE);
        String dialog_positive = getArguments().getString(POSITIVE);
        String dialog_negative = getArguments().getString(NEGATIVE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialog_title);
        builder.setMessage(dialog_message);
        if (!StringUtils.isEmpty(dialog_positive)) {
            builder.setPositiveButton(dialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clickListener.positiveClick(dialog);
                }
            });
        }
        if (!StringUtils.isEmpty(dialog_negative)) {
            builder.setNegativeButton(dialog_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clickListener.negativeClick(dialog);
                }
            });
        }
        return builder.create();
    }

    public interface CustomDialogClickListener {
        void positiveClick(DialogInterface dialog);
        void negativeClick(DialogInterface dialog);
    }
}
