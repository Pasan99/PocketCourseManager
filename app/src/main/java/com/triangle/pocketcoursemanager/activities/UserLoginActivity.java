package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.db.dao.UserDao;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.utilities.UserAuth;
import java.util.ArrayList;
import java.util.Objects;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLoginBtn;
    private TextView mGotoRegisterLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.label_goto_register_page:
                startActivity(new Intent(UserLoginActivity.this, StudentRegisterActivity.class));
                break;
        }
    }

    private void initViews() {
        mEmail = findViewById(R.id.text_email);
        mPassword = findViewById(R.id.text_password);
        mLoginBtn = findViewById(R.id.btn_login);
        mGotoRegisterLabel = findViewById(R.id.label_goto_register_page);

        // Click Listeners
        mLoginBtn.setOnClickListener(this);
        mGotoRegisterLabel.setOnClickListener(this);
    }
    private void login(){
        // validate fields
        if (!isValidEmail(mEmail.getText())){
            mEmail.setError("Please enter valid email");
        }
        else if (TextUtils.isEmpty(mPassword.getText())){
            mPassword.setError("Password cannot be empty");
        }
        else {
            // login user
            UserAuth auth = UserAuth.getInstance();
            try {
                User user = auth.loginUser(this, mEmail.getText().toString().trim(), mPassword.getText().toString().trim());
                if (user == null) {
                    Snackbar.make(mEmail, "Email & Password is not matching", Snackbar.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(UserLoginActivity.this, DashboardActivity.class));
                    finish();
                }
            } catch (Exception ex) {
                Snackbar.make(mEmail, Objects.requireNonNull(ex.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        }

        // redirect to dashboard on user role
    }
    // email validation
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}