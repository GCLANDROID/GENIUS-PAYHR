package com.genius.hrms.activity.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.genius.hrms.R;
import com.genius.hrms.activity.chat.adapter.UserAdapter;
import com.genius.hrms.activity.chat.adapter.UserFragmentAdapter;
import com.genius.hrms.activity.chat.data.ChatList;
import com.genius.hrms.activity.chat.data.ChatListOfItem;
import com.genius.hrms.activity.chat.data.User;
import com.genius.hrms.activity.chat.data.Users;
import com.genius.hrms.activity.chat.viewmodel.DatabaseViewModel;
import com.genius.hrms.activity.chat.viewmodel.LogInViewModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualChatFragment extends Fragment {
    private Context context;
    private UserAdapter userAdapter;
    private ArrayList<User> mUsers;
    private String currentUserId;
    private ArrayList<ChatListOfItem> userList;  //list of all other users with chat record
    private DatabaseViewModel databaseViewModel;
    private LogInViewModel logInViewModel;
    private RecyclerView rvItem;
    RelativeLayout relative_layout_chat_fragment;
    DatabaseReference reference;
    Pref pref;

    //Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IndividualChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndividualChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndividualChatFragment newInstance(String param1, String param2) {
        IndividualChatFragment fragment = new IndividualChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public IndividualChatFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_individual_chat, container, false);

        rvItem=view.findViewById(R.id.rvItem);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        pref=new Pref(getContext());
        userList=new ArrayList<>();
        readUsers();
        getTokens();

        return view;
    }
    public void getTokens() {
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String mToken = instanceIdResult.getToken();
//                updateToken(mToken); //updating token in firebase database
//
//            }
//        });
    }
    private void updateToken(String token) {
        //logInViewModel.updateToken(token);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        databaseReference.child(pref.getempcode()).setValue(token1);

    }
    private void readUsers() {
        mUsers=new ArrayList<>();
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users-"+pref.getSecurityCode());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mUsers.clear();
                    for (DataSnapshot datasnapshot:snapshot.getChildren()){
                        User user=datasnapshot.getValue(User.class);
//                        assert user!=null;
//                        assert firebaseUser!=null;
//
                            if (!user.getId().equals(pref.getempcode())){
                                mUsers.add(user);
                            }





                    }
                    String uid=pref.getempcode();
                    userAdapter=new UserAdapter(getContext(),mUsers, false,uid);
                    rvItem.setAdapter(userAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}