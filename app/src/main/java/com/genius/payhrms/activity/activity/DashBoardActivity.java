package com.genius.payhrms.activity.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.payhrms.R;

import com.genius.payhrms.activity.utility.NetworkConnectionCheck;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class DashBoardActivity extends AppCompatActivity {
    LinearLayout llSignIn;
    //Pref pref;
    LinearLayout llAbout, llServices, llContact,llBrochure;
    NetworkConnectionCheck connectionCheck;
    String playversion;
    String version;
    AlertDialog alertDialog;
    AlertDialog alert1;
    AlertDialog alert2;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    boolean responseStatus;
    ProgressDialog progressDialog;
    TextView tvNumber;
    Locale myLocale;
    ArrayList<String>languageList=new ArrayList<>();
    Spinner spLan;
    Pref pref;
    TextView tvNumberName,tvAbout,tvContactUs,tvServices,tvBrochure,tvLogin,tvLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        initialize();
       // shoeDialog();

        //checkBersion();
        onClick();
    }

    private void initialize() {
        pref=new Pref(DashBoardActivity.this);

        if (pref.getAccessFlag().equals("1")){
            Intent intent=new Intent(DashBoardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {

        }
        String security = "0000";
        String d = security.replace("\"", "");
        Log.d("de", security);

        llSignIn = (LinearLayout) findViewById(R.id.llLogin);
        //pref=new Pref(DashBoardActivity.this);
        llAbout = (LinearLayout) findViewById(R.id.llAbout);
        llServices = (LinearLayout) findViewById(R.id.llServices);
        llContact = (LinearLayout) findViewById(R.id.llContact);
        connectionCheck = new NetworkConnectionCheck(DashBoardActivity.this);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        llBrochure=(LinearLayout)findViewById(R.id.llBrochure);
        tvNumber=(TextView)findViewById(R.id.tvNumber);
        spLan=(Spinner)findViewById(R.id.spLan);
        languageList.add("English(India)");
        languageList.add("हिंदी");
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,languageList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spLan.setAdapter(aa);

        tvNumberName=(TextView)findViewById(R.id.tvNumberName);
        final Handler textViewHandler = new Handler();

        /*new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate("Hello World",
                                Translate.TranslateOption.targetLanguage("de"));
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {

                            Log.d("sssh",translation.getTranslatedText());

                    }
                });
                return null;
            }
        }.execute();*/
        tvAbout=(TextView)findViewById(R.id.tvAbout);
        tvContactUs=(TextView)findViewById(R.id.tvContactUs);
        tvServices=(TextView)findViewById(R.id.tvServices);
        tvBrochure=(TextView)findViewById(R.id.tvBrochure);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tvLanguage=(TextView)findViewById(R.id.tvLanguage);



    }

    private void onClick() {

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DashBoardActivity.this, AboutUsActivity.class);
                    startActivity(intent);
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                    Intent intent = new Intent(DashBoardActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashBoardActivity.this, ServicesActivity.class);
                startActivity(intent);
            }
        });
        llBrochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    operBrowser();
            }
        });
        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18002582585"));
                startActivity(intent);
            }
        });

        spLan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){


                }else {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void upDateAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_alert, null);
        dialogBuilder.setView(dialogView);
        TextView tvAttenDate = (TextView) dialogView.findViewById(R.id.tvAttenDate);
        tvAttenDate.setText("New update available");
        Button btnSkip=(Button)dialogView.findViewById(R.id.btnSkip);
        if (responseStatus){
            btnSkip.setVisibility(View.GONE);
        }else {
            btnSkip.setVisibility(View.VISIBLE); 
        }
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                alertDialog.dismiss();


            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(DashBoardActivity.this,LoginActivity.class);
               startActivity(intent);
               finish();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }


    private void errorshowing() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_error, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkBersion();
                alert1.dismiss();
            }
        });

        alert1 = dialogBuilder.create();
        alert1.setCancelable(true);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }

    private void operBrowser() {
        Uri uri = Uri.parse("https://geniusconsultant.com/PDF-doc/GCL_BROCHURE.pdf"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);

        Resources res = getResources();

        DisplayMetrics dm = res.getDisplayMetrics();

        Configuration conf = res.getConfiguration();

        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, DashBoardActivity.class);
        startActivity(refresh);

    }

    public static void main() throws Exception {
        // Instantiates a client
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // The text to translate
        String text = "Hello, world!";

        // Translates some text into Russian
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("ru"));


        System.out.printf("Text: %s%n", text);
        Log.d("Translation: %s%n", translation.getTranslatedText());
    }
















}
