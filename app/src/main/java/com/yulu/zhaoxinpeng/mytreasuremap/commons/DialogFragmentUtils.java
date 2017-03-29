package com.yulu.zhaoxinpeng.mytreasuremap.commons;

import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.yulu.zhaoxinpeng.mytreasuremap.R;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by Administrator on 2017/3/28.
 */

public class DialogFragmentUtils extends DialogFragment{

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    public static DialogFragmentUtils getInstance(String title, String message) {
        DialogFragmentUtils dialogFragmentUtils = new DialogFragmentUtils();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_MESSAGE, message);
        dialogFragmentUtils.setArguments(bundle);

        return dialogFragmentUtils;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);


        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }
}
