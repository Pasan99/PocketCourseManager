package com.triangle.pocketcoursemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.db.dao.CourseDao;
import com.triangle.pocketcoursemanager.models.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class NewCourseActivity extends AppCompatActivity {
    private TextInputEditText mName;
    private TextInputEditText mCode;
    private TextInputEditText mDescription;
    private TextInputEditText mDuration;
    private EditText mStartDate;
    private EditText mEndDate;
    private TextInputEditText mStartTime;
    private TextInputEditText mEndTime;
    private TextInputEditText mFee;

    private ImageButton mStartDateBtn;
    private ImageButton mEndDateBtn;
    private Calendar mStartDateCalendar;
    private Calendar mEndDateCalendar;
    private TextView mTitle;

    private MaterialDayPicker mDaysPicker;
    private Button mCreateBtn;
    private long mCourseId;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);

        initViews();
        setupCalendars();
    }

    @SuppressLint("SetTextI18n")
    private void initViews(){
        mTitle = findViewById(R.id.text_title);
        mName = findViewById(R.id.text_name);
        mCode = findViewById(R.id.text_code);
        mDescription = findViewById(R.id.text_description);
        mDuration = findViewById(R.id.text_duration);
        mStartDate = findViewById(R.id.text_start_date);
        mEndDate = findViewById(R.id.text_end_date);
        mStartTime = findViewById(R.id.text_day_start_time);
        mEndTime = findViewById(R.id.text_day_end_time);
        mStartDateBtn = findViewById(R.id.btn_start_date);
        mEndDateBtn = findViewById(R.id.btn_end_date);
        mDaysPicker = findViewById(R.id.day_picker);
        mCreateBtn = findViewById(R.id.btn_create);
        mFee = findViewById(R.id.text_fee);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

        final Course course = (Course) getIntent().getSerializableExtra("course");
        if (course != null){
            isEditing = true;
            mCourseId = course.getId();
            mName.setText(course.getName());
            mDescription.setText(course.getDescription());
            mCode.setText(course.getCode());
            mFee.setText(String.valueOf(course.getFee()));
            mDuration.setText(course.getDuration());
            mStartDate.setText(course.getStartDate());
            mEndDate.setText(course.getEndDate());
            mStartTime.setText(course.getDayStartTime());
            mEndTime.setText(course.getDayEndTime());
            mTitle.setText("Update Course Details");
            setSelectedDays(course);
            mCreateBtn.setText("Update Details");
        }
    }

    private void setSelectedDays(Course course){
        String[] days = course.getDays().split(",");
        ArrayList<MaterialDayPicker.Weekday> weekdays = new ArrayList<>();
        for (String day : days) {
            if (day.length() > 2) {
                if (day.equals("MONDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.MONDAY);
                }
                if (day.equals("TUESDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.TUESDAY);
                }
                if (day.equals("WEDNESDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.WEDNESDAY);
                }
                if (day.equals("THURSDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.THURSDAY);
                }
                if (day.equals("FRIDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.FRIDAY);
                }
                if (day.equals("SATURDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.SATURDAY);
                }
                if (day.equals("SUNDAY")) {
                    weekdays.add(MaterialDayPicker.Weekday.SUNDAY);
                }
            }
        }
        mDaysPicker.setSelectedDays(weekdays);
    }

    private void saveDetails(){

        if (TextUtils.isEmpty(Objects.requireNonNull(mName.getText()).toString())){
            mName.setError("Name cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mCode.getText()).toString())){
            mCode.setError("Code cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mDescription.getText()).toString())){
            mDescription.setError("Description cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mDuration.getText()).toString())){
            mDuration.setError("Duration cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mStartDate.getText()).toString())){
            mStartDate.setError("Start Date cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mEndDate.getText()).toString())){
            mEndDate.setError("End Date cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mStartTime.getText()).toString())){
            mStartTime.setError("Start Time cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mEndTime.getText()).toString())){
            mEndTime.setError("End Time cannot be empty");
        }
        else if (TextUtils.isEmpty(Objects.requireNonNull(mFee.getText()).toString())){
            mFee.setError("Fee cannot be empty");
        }
        else if (mDaysPicker.getSelectedDays().size() == 0){
            Toast.makeText(this, "Please select at least 1 day", Toast.LENGTH_LONG).show();
        }
        else {
            StringBuilder builder = new StringBuilder();
            for (MaterialDayPicker.Weekday a : mDaysPicker.getSelectedDays()) {
                builder.append(a.name()).append(",");
            }
            Course course = new Course(
                    mCourseId,
                    mName.getText().toString(),
                    mCode.getText().toString(),
                    mDescription.getText().toString(),
                    mDuration.getText().toString(),
                    Float.parseFloat(mFee.getText().toString()),
                    mStartDate.getText().toString(),
                    mEndDate.getText().toString(),
                    builder.toString(),
                    mStartTime.getText().toString(),
                    mEndTime.getText().toString()
            );
            try {
                CourseDao dao = new CourseDao(this);
                if (isEditing){
                    dao.update(course);
                    Toast.makeText(NewCourseActivity.this, "Course Successfully Updated" , Toast.LENGTH_LONG).show();
                }
                else{
                    dao.insert(course);
                    Toast.makeText(NewCourseActivity.this, "New Course Successfully Created" , Toast.LENGTH_LONG).show();
                }
                finish();
            }
            catch (Exception ex){
                Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupCalendars() {
        mStartDateCalendar = Calendar.getInstance();
        mEndDateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mStartDateCalendar.set(Calendar.YEAR, year);
                mStartDateCalendar.set(Calendar.MONTH, monthOfYear);
                mStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mEndDateCalendar.set(Calendar.YEAR, year);
                mEndDateCalendar.set(Calendar.MONTH, monthOfYear);
                mEndDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        mStartDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewCourseActivity.this, startDateListener, mStartDateCalendar
                        .get(Calendar.YEAR), mStartDateCalendar.get(Calendar.MONTH),
                        mStartDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mEndDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewCourseActivity.this, endDateListener, mEndDateCalendar
                        .get(Calendar.YEAR), mEndDateCalendar.get(Calendar.MONTH),
                        mEndDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mStartDate.setText(sdf.format(mStartDateCalendar.getTime()));
        mEndDate.setText(sdf.format(mEndDateCalendar.getTime()));
    }
}