package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.adapters.CourseItemAdapter;
import com.triangle.pocketcoursemanager.db.dao.CourseDao;
import com.triangle.pocketcoursemanager.models.Course;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.models.UserTypes;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    UserAuth mAuth;
    private ArrayList<Course> mCourses;
    private CourseItemAdapter mAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = UserAuth.getInstance();
        final User currentUser = mAuth.getCurrentUser(this);

        RecyclerView recyclerView = findViewById(R.id.courses_recycle_view);
        FloatingActionButton courseFab = findViewById(R.id.new_course_fab);
        FloatingActionButton adminFab = findViewById(R.id.new_admin_fab);
        FloatingActionMenu floatingActionMenu = findViewById(R.id.floatingMenu);
        ImageButton logoutBtn = findViewById(R.id.btn_logout);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_dashboard);
        TextView userName = findViewById(R.id.text_user_name);
        TextView dashTitle = findViewById(R.id.text_dashboard_title);

        if (mAuth.getCurrentUser(this) != null){
            userName.setText(currentUser.getName());

            if (currentUser.getUserType().equals(UserTypes.ADMIN)){
                dashTitle.setText("All Courses");
            }
        }

        if (currentUser != null && currentUser.getUserType().equals(UserTypes.STUDENT)){
            courseFab.setLabelText("Attend to course");
            floatingActionMenu.removeMenuButton(adminFab);
        }

        if (courseFab != null) {
            courseFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser != null && currentUser.getUserType().equals(UserTypes.STUDENT)){
                        startActivity(new Intent(DashboardActivity.this, CourseSelectionActivity.class));
                    }
                    else{
                        startActivity(new Intent(DashboardActivity.this, NewCourseActivity.class));
                    }
                }
            });
        }
        if (adminFab != null){
            adminFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DashboardActivity.this, AdminRegistrationsActivity.class));
                }
            });
        }

        mCourses = getCourses();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCourses = getCourses();
                mAdapter.setCourses(mCourses);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter = new CourseItemAdapter(this, mCourses);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mCourses == null || mCourses.size() == 0) {
            Snackbar.make(recyclerView, "No attended courses to display. Please attend by pressing the plus button", Snackbar.LENGTH_LONG).show();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Logout")
                        .setMessage("Do you really want to logout?")
                        .setIcon(android.R.drawable.ic_input_delete)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                UserAuth.getInstance().logout(DashboardActivity.this);
                                startActivity(new Intent(DashboardActivity.this, UserLoginActivity.class));
                                finish();
                            }})
                        .setNegativeButton("Cancel", null).show();
            }
        });
    }

    private ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            CourseDao dao = new CourseDao(this);

            if (mAuth != null && mAuth.getCurrentUser(this) != null) {
                if (mAuth.getCurrentUser(this).getUserType().equals(UserTypes.ADMIN)) {
                    courses = dao.getAllCourses();
                } else {
                    courses = dao.getCoursesByUserId(mAuth.getCurrentUser(this).getId());
                }

                if (mAuth.getCurrentUser(this).getUserType().equals(UserTypes.STUDENT)) {
                    for (Course course : courses) {
                        course.setAttended(true);
                    }
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(this, "No Courses to display", Toast.LENGTH_LONG).show();
        }

        return courses;
    }
}