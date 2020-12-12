package com.triangle.pocketcoursemanager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.activities.CourseDetailsActivity;
import com.triangle.pocketcoursemanager.models.Course;
import java.util.ArrayList;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.CourseItemViewHolder> {
    private ArrayList<Course> mCourses;
    private Context mContext;

    public CourseItemAdapter(Context context, ArrayList<Course> courses){
        mContext = context;
        mCourses = courses;
    }

    @NonNull
    @Override
    public CourseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourseItemViewHolder holder, int position) {
        final Course course = mCourses.get(position);
        if (course != null){
            holder.price.setText("LKR " + course.getFee());
            if (course.getDescription() != null && course.getDescription().length() > 100) {
                holder.description.setText(course.getDescription().substring(0, 100));
            }
            else{
                holder.description.setText(course.getDescription());
            }
            holder.name.setText(course.getName());
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("course",course);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        });

        assert course != null;
        if (course.isAttended()){
            holder.price.setVisibility(View.GONE);
            holder.dayPicker.setVisibility(View.VISIBLE);
            holder.dayPicker.setEnabled(false);
            setSelectedDays(course, holder.dayPicker);
        }
        else{
            holder.price.setVisibility(View.VISIBLE);
            holder.dayPicker.setVisibility(View.GONE);
        }
    }

    private void setSelectedDays(Course course, MaterialDayPicker picker){
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
        picker.setSelectedDays(weekdays);
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public void setCourses(ArrayList<Course> courses){
        mCourses = courses;
    }

    public static class CourseItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView description;
        private final TextView price;
        private final ConstraintLayout container;
        private final MaterialDayPicker dayPicker;

        public CourseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_course_name_txt);
            description = itemView.findViewById(R.id.item_course_description_txt);
            price = itemView.findViewById(R.id.item_course_price_txt);
            container = itemView.findViewById(R.id.item_course_container);
            dayPicker = itemView.findViewById(R.id.day_picker);
        }
    }
}
