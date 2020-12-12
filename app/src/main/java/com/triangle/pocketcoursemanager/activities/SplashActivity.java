package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.db.dao.UserDao;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserAuth auth = UserAuth.getInstance();
        if (auth != null && auth.getCurrentUser(this) != null){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    finish();
                }
            }, 2000);
        }
        else{
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    finish();
                }
            }, 2000);
        }
        createFirstAdmin();
    }

    private void createFirstAdmin(){
        UserAuth auth = UserAuth.getInstance();
        UserDao dao = new UserDao(this);
        ArrayList<User> users = dao.getAllUsers();

        if (auth != null && auth.getCurrentUser(this) == null){
            if (users == null || users.size() == 0) {
                auth.registerAdmin(this, "admin@gmail.com", "admin", "1234");
            }
        }
    }
}