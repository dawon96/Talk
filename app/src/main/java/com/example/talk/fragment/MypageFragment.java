package com.example.talk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.talk.R;
import com.example.talk.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageFragment extends Fragment{

    ImageView mypage_userimage;
    TextView tv_useremail;
    TextView tv_userinformation;
    TextView tv_selling;
    TextView tv_programinformation;
    TextView tv_logout;
    TextView tv_exit;
    UserModel userModel;
    public RequestManager mGlide;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);


        mypage_userimage = (ImageView)view.findViewById(R.id.mypage_userimage);
        tv_useremail = (TextView)view.findViewById(R.id.tv_useremail);
        tv_userinformation = (TextView)view.findViewById(R.id.tv_userimformation);
        tv_selling =(TextView)view.findViewById(R.id.tv_selling);
        tv_programinformation = (TextView)view.findViewById(R.id.tv_programinformation);
        tv_logout = (TextView)view.findViewById(R.id.tv_logout);
        tv_exit = (TextView)view.findViewById(R.id.tv_exit);

        mGlide = Glide.with(getActivity());

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    userModel = snapshot.getValue(UserModel.class);

                    tv_useremail.setText(userModel.userEmail.toString());
                    tv_userinformation.setText(userModel.userName.toString());

                    mGlide.load(userModel.profileImageUrl.toString()).into(mypage_userimage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tv_selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainactivity_framelayout,new Mypage_selling()).commit();
            }
        });


        return  view;
    }
}
