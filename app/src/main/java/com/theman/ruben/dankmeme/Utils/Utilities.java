package com.theman.ruben.dankmeme.Utils;

/**
 * Created by Ruben on 19-10-2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

public class Utilities {

    public static ProgressDialog showProgress(ProgressDialog dialog, Context context, String displayText) {
        dialog = ProgressDialog.show(context, null, displayText);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
}
