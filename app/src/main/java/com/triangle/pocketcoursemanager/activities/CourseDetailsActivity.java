package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.db.dao.CourseDao;
import com.triangle.pocketcoursemanager.models.Course;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.models.UserTypes;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        final Course course = (Course) getIntent().getSerializableExtra("course");

        TextView name = findViewById(R.id.text_name);
        TextView description = findViewById(R.id.text_description);
        TextView code = findViewById(R.id.text_course_code);
        TextView subject = findViewById(R.id.text_subject);
        TextView fee = findViewById(R.id.text_fee);
        TextView duration = findViewById(R.id.text_duration);
        TextView startDate = findViewById(R.id.text_start_date);
        TextView endDate = findViewById(R.id.text_end_date);
        TextView startTime = findViewById(R.id.text_start_time);
        TextView endTime = findViewById(R.id.text_end_time);
        Button updateBtn = findViewById(R.id.btn_update);
        final Button deleteBtn = findViewById(R.id.btn_delete);

        if (course != null){
            name.setText(course.getName());
            description.setText(course.getDescription());
            subject.setText(course.getName());
            code.setText(course.getCode());
            fee.setText("LKR " + course.getFee());
            duration.setText(course.getDuration());
            startDate.setText(course.getStartDate());
            endDate.setText(course.getEndDate());
            startTime.setText(course.getDayStartTime());
            endTime.setText(course.getDayEndTime());

            // weekday
            setWeekdays(course);

            User currentUser = UserAuth.getInstance().getCurrentUser(this);
            if (currentUser != null && currentUser.getUserType().equals(UserTypes.ADMIN)){
                updateBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseDetailsActivity.this, NewCourseActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("course",course);
                    b.putString("type", "edit");
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseDao dao = new CourseDao(CourseDetailsActivity.this);
                    try {
                        dao.delete(course.getId());
                        Toast.makeText(CourseDetailsActivity.this, "Successfully Deleted" , Toast.LENGTH_LONG).show();
                        finish();
                    }
                    catch (Exception ex){
                        Snackbar.make(deleteBtn, "Something went wrong." , Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Snackbar.make(name, "Something went wrong", Snackbar.LENGTH_LONG).show();
        }

    }

    private void setWeekdays(Course course){
        TextView saturday = findViewById(R.id.text_saturday);
        TextView sunday = findViewById(R.id.text_sunday);
        TextView monday = findViewById(R.id.text_monday);
        TextView tuesday = findViewById(R.id.text_tuesday);
        TextView wednesday = findViewById(R.id.text_wednesday);
        TextView thursday = findViewById(R.id.text_thursday);
        TextView friday = findViewById(R.id.text_friday);

        String[] days = course.getDays().split(",");
        for(int i = 0; i < days.length; i++){
            if (days[i].length() > 2){
                if (days[i].equals("MONDAY")){
                    monday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("TUESDAY")){
                    tuesday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("WEDNESDAY")){
                    wednesday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("THURSDAY")){
                    thursday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("FRIDAY")){
                    friday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("SATURDAY")){
                    saturday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                if (days[i].equals("SUNDAY")){
                    sunday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }
    }
}