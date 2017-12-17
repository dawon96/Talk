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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.talk.R;
import com.example.talk.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Mypage_selling_reading extends Fragment {

    TextView tv_title ;
    ImageView user_image;
    TextView userNameEmail;
    TextView userMajor;
    ImageView img;
    TextView tv_content;
    TextView bt_money;
    Button bt_reset;
    UserModel userModel;
    TextView tv_category;
    ImageView bt_back;
    public RequestManager mGlide;
    private List<adapter> adapters = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selling_readinglayout, container, false);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        user_image = (ImageView) view.findViewById(R.id.user_image);
        userNameEmail = (TextView) view.findViewById(R.id.userNameEmail);
        userMajor = (TextView) view.findViewById(R.id.userMajor);
        img = (ImageView) view.findViewById(R.id.img);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        bt_money = (TextView) view.findViewById(R.id.bt_money);
        bt_reset = (Button) view.findViewById(R.id.bt_reset);
        tv_category = (TextView)view.findViewById(R.id.tv_category);
        bt_back = (ImageView)view.findViewById(R.id.bt_back);


        final SharedPreferences pref = getActivity().getSharedPreferences("selling", getActivity().MODE_PRIVATE);
        final adapter ad = new adapter(pref.getString("imageUrl", ""), pref.getString("title", ""), pref.getString("money", ""), pref.getString("content", ""), pref.getString("category",""), pref.getString("useruid", ""));

        tv_title.setText(pref.getString("title", ""));
        bt_money.setText(pref.getString("money", "") + "  won");
        tv_content.setText(pref.getString("content", ""));
        tv_category.setText(pref.getString("category",""));

        mGlide = Glide.with(getActivity());

        mGlide.load(pref.getString("imageUrl", "")).into(img);

        database = FirebaseDatabase.getInstance();

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(pref.getString("useruid", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    userModel = snapshot.getValue(UserModel.class);

                    userNameEmail.setText(userModel.userName.toString() + "   " + userModel.userEmail.toString());
                    userMajor.setText(userModel.major.toString());

                    mGlide.load(userModel.profileImageUrl.toString()).into(user_image);
                }
                //Toast.makeText(getActivity(),userModel.userName.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getActivity(),"실패", Toast.LENGTH_SHORT).show();
            }
        });

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                final String imageurl = pref.getString("imageUrl","");

                database.getReference().child("writings").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                            adapter adapter = snapshot.getValue(adapter.class);
                            if(adapter.imageUrl.equals(ad.imageUrl) && adapter.ad_category.equals(ad.ad_category) && adapter.ad_content.equals(ad.ad_content) && adapter.ad_money.equals(ad.ad_money) && adapter.ad_title.equals(ad.ad_title) && adapter.ad_useruid.equals(ad.ad_useruid)) {
                                snapshot.getRef().setValue(null);
                            }
                        }
                        //Toast.makeText(getActivity(),userModel.userName.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Toast.makeText(getActivity(),"실패", Toast.LENGTH_SHORT).show();
                    }
                });
                getFragmentManager().popBackStack();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        removeAllPreferences(getActivity());

        return view;
    }

    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("selling", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
