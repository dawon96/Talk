package com.example.talk.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talk.R;
import com.example.talk.chat.MessageActivity;
import com.example.talk.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadingFragment extends Fragment {


    TextView tv_title ;
    ImageView user_image;
    TextView userNameEmail;
    TextView userMajor;
    ImageView img;
    TextView tv_content;
    TextView bt_money;
    Button bt_chatting;
    UserModel userModel;
    TextView tv_category;
    ImageView bt_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        user_image = (ImageView) view.findViewById(R.id.user_image);
        userNameEmail = (TextView) view.findViewById(R.id.userNameEmail);
        userMajor = (TextView) view.findViewById(R.id.userMajor);
        img = (ImageView) view.findViewById(R.id.img);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        bt_money = (TextView) view.findViewById(R.id.bt_money);
        bt_chatting = (Button) view.findViewById(R.id.bt_chatting);
        tv_category = (TextView)view.findViewById(R.id.tv_category);
        bt_back = (ImageView)view.findViewById(R.id.bt_back);

        SharedPreferences pref = getActivity().getSharedPreferences("adapter", getActivity().MODE_PRIVATE);

        tv_title.setText(pref.getString("title", ""));
        bt_money.setText(pref.getString("money", "") + "  won");
        tv_content.setText(pref.getString("content", ""));
        tv_category.setText(pref.getString("category",""));

        Glide.with(img).load(pref.getString("imageUrl", "")).into(img);

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(pref.getString("useruid", "")).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    userModel = snapshot.getValue(UserModel.class);

                    userNameEmail.setText(userModel.userName.toString() + "   " + userModel.userEmail.toString());
                    userMajor.setText(userModel.major.toString());

                    Glide.with(getActivity()).load(userModel.profileImageUrl.toString()).into(user_image);
                }
                //Toast.makeText(getActivity(),userModel.userName.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getActivity(),"실패", Toast.LENGTH_SHORT).show();
            }
        });

        removeAllPreferences(getActivity());


        bt_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    return;
                }
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid",userModel.uid);
                ActivityOptions activityOptions = null;
                startActivity(intent);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }


}
