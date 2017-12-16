package com.example.talk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.talk.fragment.CategoryFragment;
import com.example.talk.fragment.ChatFragment;
import com.example.talk.fragment.HomeFragment;
import com.example.talk.fragment.WriteFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);

        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        FragmentClear();
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new HomeFragment()).commit();
                        return true;
                    case R.id.action_category:
                        FragmentClear();
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new CategoryFragment()).commit();
                        return true;
                    case R.id.action_write:
                        FragmentClear();
                        //Intent intent = new Intent(MainActivity.this,WriteFragment.class);
                        //startActivity(intent);
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new WriteFragment()).commit();
                        return true;
                    case R.id.action_people_chat:
                        FragmentClear();
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new ChatFragment()).commit();
                        return true;
                    case R.id.action_mypage:
                        //getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new MypageFragment()).commit();
                        return true;
                }
                return false;
            }
        });

    }

    private long backPressedTime = 0;
    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long interval = tempTime-backPressedTime;

        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        else {
            if (0 <= interval && 2000 >= interval) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "'뒤로' 버튼을 한번 더 누르시면 종료합니다. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void FragmentClear(){
        for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
            getFragmentManager().popBackStack();
        }
    }
    @Override
    protected void onDestroy() {
        getApplicationContext().getSharedPreferences("category", 0).edit().clear().commit();
        super.onDestroy();
    }
}