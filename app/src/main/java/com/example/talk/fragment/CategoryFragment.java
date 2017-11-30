package com.example.talk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.talk.R;

public class CategoryFragment extends Fragment {

    TextView ct1;
    TextView ct2;
    TextView ct3;
    TextView ct4;
    TextView ct5;
    TextView ct6;
    TextView ct7;
    TextView ct8;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ct1 = (TextView) view.findViewById(R.id.ct1);
        ct2 = (TextView) view.findViewById(R.id.ct2);
        ct3 = (TextView) view.findViewById(R.id.ct3);
        ct4 = (TextView) view.findViewById(R.id.ct4);
        ct5 = (TextView) view.findViewById(R.id.ct5);
        ct6 = (TextView) view.findViewById(R.id.ct6);
        ct7 = (TextView) view.findViewById(R.id.ct7);
        ct8 = (TextView) view.findViewById(R.id.ct8);

        ct1.setOnClickListener(ocl);
        ct2.setOnClickListener(ocl);
        ct3.setOnClickListener(ocl);
        ct4.setOnClickListener(ocl);
        ct5.setOnClickListener(ocl);
        ct6.setOnClickListener(ocl);
        ct7.setOnClickListener(ocl);
        ct8.setOnClickListener(ocl);

        return view;

    }

    TextView.OnClickListener ocl = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            Fragment fragment = new CategoryFragment();
            Bundle bundle = new Bundle();

            switch(v.getId()){
                case R.id.ct1:
                    bundle.putString("category", ct1.getText().toString());
                    break;
                case R.id.ct2:
                    bundle.putString("category", ct2.getText().toString());
                    break;
                case R.id.ct3:
                    bundle.putString("category", ct3.getText().toString());
                    break;
                case R.id.ct4:
                    bundle.putString("category", ct4.getText().toString());
                    break;
                case R.id.ct5:
                    bundle.putString("category", ct5.getText().toString());
                    break;
                case R.id.ct6:
                    bundle.putString("category", ct6.getText().toString());
                    break;
                case R.id.ct7:
                    bundle.putString("category", ct7.getText().toString());
                    break;
                case R.id.ct8:
                    bundle.putString("category", ct8.getText().toString());
                    break;
            }
            //찾아보니 fragment 간 데이터전달에는 intent말고 bundle 써야한대서 해봤는데 안되네요..
            fragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new Reading_category()).commit();


        }
    };

}