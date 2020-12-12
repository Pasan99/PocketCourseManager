package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.adapters.CourseItemAdapter;
import com.triangle.pocketcoursemanager.adapters.SelectCourseItemAdapter;
import com.triangle.pocketcoursemanager.db.dao.CourseDao;
import com.triangle.pocketcoursemanager.models.Course;
import com.triangle.pocketcoursemanager.models.UserTypes;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

import java.util.ArrayList;

public class CourseSelectionActivity extends AppCompatActivity {
    UserAuth mAuth;
    private ArrayList<Course> mCourses;
    private SelectCourseItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);

        mAuth = UserAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recycle_view_course_selection);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_course_selection);

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

        if (mCourses != null) {
            mAdapter = new SelectCourseItemAdapter(this, mCourses);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            CourseDao dao = new CourseDao(this);
            courses = dao.getNotAttendedCoursesByUserId(mAuth.getCurrentUser(CourseSelectionActivity.this).getId());
        }
        catch (Exception ex){
            Toast.makeText(this, "No Courses to display", Toast.LENGTH_LONG).show();
        }

        return courses;
    }
}