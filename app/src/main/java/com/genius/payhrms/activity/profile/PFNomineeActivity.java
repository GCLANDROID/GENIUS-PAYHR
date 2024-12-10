package com.genius.payhrms.activity.profile;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.ValidUtils;
import com.genius.payhrms.databinding.ActivityPfnomineeBinding;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PFNomineeActivity extends AppCompatActivity {
    Pref pref;
    ActivityPfnomineeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_pfnominee);
        initView();
    }

    private void initView(){
        pref=new Pref(PFNomineeActivity.this);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("SecurityCode","1186");
            jsonObject.put("Employeeid",pref.getEmpId());
            getNominee(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getNominee(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog(PFNomineeActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sGetPFNominationAPi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("responseNominee", "@@@@@@" + job1);
                        pd.dismiss();

                        String Response_Code = job1.optString("Response_Code");
                        if (Response_Code.equals("101")){
                            String Response_Data=job1.optString("Response_Data");
                            try {
                                JSONObject responseOBj=new JSONObject(Response_Data);
                                JSONArray Table=responseOBj.optJSONArray("Table");
                                for ( int i=0;i<Table.length();i++){
                                    JSONObject tableobj=Table.optJSONObject(i);
                                    String PFNomination_Filled=tableobj.optString("PFNomination_Filled");
                                    binding.tvPFNominee.setText(PFNomination_Filled);

                                    String Name_of_the_PFNominee=tableobj.optString("Name_of_the_PFNominee");
                                    binding.tvPFNomineeName.setText(Name_of_the_PFNominee);

                                    String Relationship_with_PFNominee=tableobj.optString("Relationship_with_PFNominee");
                                    binding.tvPFNomineeNameReltn.setText(Relationship_with_PFNominee);



                                    String Gratuity_Nomination=tableobj.optString("Gratuity_Nomination");
                                    binding.tvGratuityNominee.setText(Gratuity_Nomination);

                                    String Name_of_the_GratuityNominee=tableobj.optString("Name_of_the_GratuityNominee");
                                    binding.tvGratuityNomineeName.setText(Name_of_the_GratuityNominee);

                                    String Relationship_with_GratuityNominee=tableobj.optString("Relationship_with_GratuityNominee");
                                    binding.tvGratuityNomineeNameReltn.setText(Relationship_with_GratuityNominee);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();

                    }
                });
    }
}