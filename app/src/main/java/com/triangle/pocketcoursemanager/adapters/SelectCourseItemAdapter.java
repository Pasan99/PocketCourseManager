package com.triangle.pocketcoursemanager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.triangle.pocketcoursemanager.R;
import com.triangle.pocketcoursemanager.activities.CourseDetailsActivity;
import com.triangle.pocketcoursemanager.db.dao.CourseDao;
import com.triangle.pocketcoursemanager.db.dao.UserDao;
import com.triangle.pocketcoursemanager.models.Course;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.utilities.UserAuth;

import java.util.ArrayList;

public class SelectCourseItemAdapter extends RecyclerView.Adapter<SelectCourseItemAdapter.CourseItemViewHolder> {
    private ArrayList<Course> mCourses;
    private Context mContext;

    public SelectCourseItemAdapter(Context context, ArrayList<Course> courses){
        mContext = context;
        mCourses = courses;
    }

    @NonNull
    @Override
    public CourseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_course, parent, false);
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

            if(course.isAttended()){
                holder.attendBtn.setVisibility(View.INVISIBLE);
                holder.successMessage.setVisibility(View.VISIBLE);
            }
            else{
                holder.attendBtn.setVisibility(View.VISIBLE);
                holder.successMessage.setVisibility(View.INVISIBLE);
            }
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
        holder.attendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course != null){
                    User user = UserAuth.getInstance().getCurrentUser(mContext);
                    if (user != null) {
                        try {
                            CourseDao dao = new CourseDao(mContext);
                            dao.assignCourseToUser(course.getId(), user.getId());
                            course.setAttended(true);
                            notifyDataSetChanged();
                        }
                        catch (Exception ex){
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
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
        private final Button attendBtn;
        private final TextView successMessage;

        public CourseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_course_name_txt);
            description = itemView.findViewById(R.id.item_course_description_txt);
            price = itemView.findViewById(R.id.item_course_price_txt);
            container = itemView.findViewById(R.id.item_course_container);
            attendBtn = itemView.findViewById(R.id.item_course_attend_button);
            successMessage = itemView.findViewById(R.id.item_course_successfull_message);
        }
    }
}
