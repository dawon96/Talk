package com.example.talk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talk.R;
import com.example.talk.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadingFragment extends Fragment {


    TextView tv_title ;
    ImageView user_image;
    TextView userName;
    TextView userEmail;
    ImageView img;
    TextView tv_content;
    Button bt_money;
    Button bt_chatting;
    UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);

        LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.readingfragment_linearlayout);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        user_image = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        img = (ImageView) view.findViewById(R.id.img);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        bt_money = (Button) view.findViewById(R.id.bt_money);
        bt_chatting = (Button) view.findViewById(R.id.bt_chatting);

        SharedPreferences pref = getActivity().getSharedPreferences("adapter", getActivity().MODE_PRIVATE);

        tv_title.setText(pref.getString("title", ""));
        bt_money.setText(pref.getString("money", ""));
        tv_content.setText(pref.getString("content", ""));

        Glide.with(img).load(pref.getString("imageUrl", "")).into(img);

        //Toast.makeText(getActivity(), pref.getString("useruid",""), Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(pref.getString("useruid", "")+"####").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
         userModel = dataSnapshot.getValue(UserModel.class);
         }
         @Override
         public void onCancelled(DatabaseError databaseError) {
         }
         });

        userName.setText(userModel.userName);
        userEmail.setText(userModel.userEmail);

        removeAllPreferences(getActivity());

        return view;
    }

    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }


}
