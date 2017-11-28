package com.example.talk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.talk.fragment.ChatFragment;
import com.example.talk.fragment.HomeFragment;
import com.example.talk.fragment.WriteFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new HomeFragment()).commit();
                        return true;
                    case R.id.action_category:
                        //getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new CategoryFragment()).commit();
                        return true;
                    case R.id.action_write:
                        //Intent intent = new Intent(MainActivity.this,WriteFragment.class);
                        //startActivity(intent);
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new WriteFragment()).commit();
                        return true;
                    case R.id.action_people_chat:
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
}
