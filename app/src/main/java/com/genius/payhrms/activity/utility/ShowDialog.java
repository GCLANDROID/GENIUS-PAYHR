package com.genius.payhrms.activity.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.payhrms.R;

public class ShowDialog {
    private static AlertDialog errorAlertDialog,successaAlertDialog;
    public static void showSuccessDialog(Context context, String text,ResultListener resultListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultListener.onSuccess();

            }
        });

        successaAlertDialog = dialogBuilder.create();
        successaAlertDialog.setCancelable(false);
        Window window = successaAlertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        successaAlertDialog.show();
    }
    public static void showErrorDialog(Context context,String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.error_ayput, null);
        dialogBuilder.setView(dialogView);
        TextView tvError = (TextView) dialogView.findViewById(R.id.tvError);
        tvError.setText(text);
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorAlertDialog.dismiss();
            }
        });

        errorAlertDialog = dialogBuilder.create();
        errorAlertDialog.setCancelable(false);
        Window window = errorAlertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        errorAlertDialog.show();
    }

    public static void onDismiss(){
        if (successaAlertDialog != null  || successaAlertDialog.isShowing()){
            successaAlertDialog.dismiss();
        }
    }

    public interface ResultListener{
        void onSuccess();
    }
}
