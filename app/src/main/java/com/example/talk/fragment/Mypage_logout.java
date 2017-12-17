package com.example.talk.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talk.LoginActivity;
import com.example.talk.R;
import com.google.firebase.auth.FirebaseAuth;

public class Mypage_logout extends Fragment {

    TextView tv_logout;
    ImageView bt_back;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_mypage, container, false);

        tv_logout = (TextView)view.findViewById(R.id.tv_logout);
        bt_back = (ImageView) view.findViewById(R.id.bt_back);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                FragmentClear();
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

    public void FragmentClear(){
        for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
            getFragmentManager().popBackStack();
        }
    }
}
