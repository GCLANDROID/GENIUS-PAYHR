package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.leaveapplication.ApplicationFragment;
import com.genius.hrms.activity.leaveapplication.SpecialHolidayApplicationFragment;
import com.genius.hrms.activity.model.DayBreakUpModel;
import com.genius.hrms.activity.model.SpecialHolidayItemModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SpecialHolidayAppItemAdapter extends RecyclerView.Adapter<SpecialHolidayAppItemAdapter.MyViewHolder> {
    ArrayList<SpecialHolidayItemModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spholidayappitem_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final SpecialHolidayItemModel dayModel = itemList.get(i);
        final Pref pref=new Pref(context1);
        final ProgressDialog pd=new ProgressDialog(context1);
        pd.setMessage("loading..");
        pd.setCancelable(false);

        if (pref.getLanguage().equals("hi")) {

            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getDayName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvDayName.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.show();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        }else {
            myViewHolder.tvDayName.setText(itemList.get(i).getDayName());


        }
        myViewHolder.tvDate.setText(itemList.get(i).getDate());


      if (dayModel.isSelected()){
          myViewHolder.imgLike.setVisibility(View.VISIBLE);
      }else {
          myViewHolder.imgLike.setVisibility(View.GONE);
      }

        myViewHolder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);

                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((SpecialHolidayApplicationFragment) context).updateStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);

                    ((SpecialHolidayApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDayName;
        LinearLayout llClick;
        ImageView imgLike;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDayName = (TextView) itemView.findViewById(R.id.tvDayName);



            llClick = (LinearLayout) itemView.findViewById(R.id.llClick);

            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);

        }
    }

    public SpecialHolidayAppItemAdapter(ArrayList<SpecialHolidayItemModel> itemList, Fragment context, Context context1) {
        this.itemList = itemList;
        this.context = context;
        this.context1=context1;
    }
}
