package com.example.javacp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.model.CourseModelAdmin;
import java.util.List;

public class CourseAdapterAdmin extends RecyclerView.Adapter<CourseAdapterAdmin.CourseViewHolder> {

    private Context context;
    private List<CourseModelAdmin> courseList;

    public CourseAdapterAdmin(Context context, List<CourseModelAdmin> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item_admin, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModelAdmin course = courseList.get(position);

        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getCourseDescription());
        holder.coursePrice.setText("Price: ₹" + course.getPrice());
        String secureUrl = course.getThumbnailUrl().replace("http://", "https://");
        Glide.with(context)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.courseThumbnail);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDescription, coursePrice;
        ImageView courseThumbnail;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.tvCourseTitleAdmin);
            courseDescription = itemView.findViewById(R.id.tvCourseDescriptionAdmin);
            coursePrice = itemView.findViewById(R.id.tvCoursePriceAdmin);
            courseThumbnail = itemView.findViewById(R.id.ivCourseThumbnailAdmin);
        }
    }
}
