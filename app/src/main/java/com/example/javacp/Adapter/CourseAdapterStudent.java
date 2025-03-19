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
import com.example.javacp.model.CourseModelStudent;
import java.util.List;

public class CourseAdapterStudent extends RecyclerView.Adapter<CourseAdapterStudent.CourseViewHolder> {

    private Context context;
    private List<CourseModelStudent> courseList;

    public CourseAdapterStudent(Context context, List<CourseModelStudent> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cards_student, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModelStudent course = courseList.get(position);

        // Load thumbnail using Glide (ensure Glide dependency is added)
        String secureUrl = course.getThumbnailUrl().replace("http://", "https://");
        Glide.with(context)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)  // Fallback image
                .into(holder.courseThumbnail);

        // Set course title, description, and price
        holder.courseTitle.setText(course.getTitle());
        holder.courseDescription.setText(course.getDescription());
        holder.coursePrice.setText("₹" + course.getPrice());  // Display price with currency symbol
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseThumbnail;
        TextView courseTitle, courseDescription, coursePrice;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseThumbnail = itemView.findViewById(R.id.ivCourseThumbnail);
            courseTitle = itemView.findViewById(R.id.tvCourseTitle);
            courseDescription = itemView.findViewById(R.id.tvCourseDescription);
            coursePrice = itemView.findViewById(R.id.tvCoursePrice);
        }
    }
}
