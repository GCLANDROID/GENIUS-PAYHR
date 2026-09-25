package com.genius.payhrms.activity.utility;



import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.payhrms.R;


public class MockLocationDialog extends Dialog {
    private static final String TAG = "MockLocationDialog";

    private Context context;
    public MockLocationDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_mock_location);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        LinearLayout btnOpenSettings = findViewById(R.id.btnOpenSettings);
        TextView btnCloseApp = findViewById(R.id.btnCloseApp);
        btnOpenSettings.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finishAffinity();
            }
            dismiss();
        });

        btnCloseApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.finishAffinity();
                }
            }
        });

        Window window = getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }
}