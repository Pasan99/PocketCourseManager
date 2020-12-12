package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

public class StudentRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextInputEditText mName;
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        initViews();
    }

    private void initViews() {
        mEmail = findViewById(R.id.text_email);
        mPassword = findViewById(R.id.text_password);
        mName = findViewById(R.id.text_name);
        mRegisterBtn = findViewById(R.id.btn_register);

        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            register();
        }
    }

    private void register() {
        if (TextUtils.isEmpty(mEmail.getText())){
            mEmail.setError("Email cannot be empty");
        }
        else if (TextUtils.isEmpty(mPassword.getText())){
            mPassword.setError("Password cannot be empty");
        }
        else if (TextUtils.isEmpty(mName.getText())){
            mName.setError("Name cannot be empty");
        }
        else{
            // register user
            UserAuth auth = UserAuth.getInstance();
            try {
                User newUser = auth.registerStudent(
                        StudentRegisterActivity.this,
                        mEmail.getText().toString().trim(),
                        mName.getText().toString(),
                        mPassword.getText().toString().trim()
                );
                if (newUser != null){
                    User loggedUser = auth.loginUser(StudentRegisterActivity.this, mEmail.getText().toString(), mPassword.getText().toString());
                    if (loggedUser != null){
                        finish();
                        startActivity(new Intent(StudentRegisterActivity.this, DashboardActivity.class));
                        finish();
                    }
                }
            }
            catch (Exception ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}