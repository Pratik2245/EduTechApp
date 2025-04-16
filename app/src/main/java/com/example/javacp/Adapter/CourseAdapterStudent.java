package com.example.javacp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.javacp.R;
import com.example.javacp.Student.HomeActivityStudents;
import com.example.javacp.model.CourseModelStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import org.json.JSONObject;

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

        String secureUrl = course.getThumbnailUrl().replace("http://", "https://");
        Glide.with(context)
                .load(secureUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.courseThumbnail);

        holder.courseTitle.setText(course.getTitle());
        holder.courseDescription.setText(course.getDescription());
        holder.coursePrice.setText("₹" + course.getPrice());
        holder.tvTeacherName.setText("By "+course.getTeacherName());

        holder.BuyCourse.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

            if (currentUserId == null) {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if course is already subscribed
            db.collection("subscribed_courses")
                    .whereEqualTo("userId", currentUserId)
                    .whereEqualTo("courseId", course.getCourseId())  // Use your course ID field here
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Already subscribed
                            Toast.makeText(context, "You have already subscribed to this course", Toast.LENGTH_SHORT).show();
                        } else {
                            // Not subscribed, proceed with payment

                            // Save course info before payment
                            HomeActivityStudents.setLastPaymentDetails(
                                    course.getTitle(),
                                    course.getPrice(),
                                    course.getCourseId(),
                                    course.getThumbnailUrl(),
                                    course.getVideoUrl(),
                                    course.getTeacherId(),
                                    course.getTeacherName()
                            );

                            // Start Razorpay payment
                            initiatePayment(course); // Replace with your Razorpay code
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error checking subscription: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    private void initiatePayment(CourseModelStudent course) {
        try {
            int price = Integer.parseInt(course.getPrice());
            int finalAmount = price * 100;

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_tpyHJaccSpSIuW");

            JSONObject options = new JSONObject();
            options.put("name", "EduTechApp");
            options.put("description", course.getTitle());
            options.put("currency", "INR");
            options.put("amount", finalAmount);
            options.put("theme.color", "#3399cc");
            options.put("prefill.email", "user@example.com");
            options.put("prefill.contact", "9876543210");

            if (context instanceof Activity) {
                checkout.open((Activity) context, options);
            } else {
                Toast.makeText(context, "Payment initiation failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("RAZORPAY_ERROR", "Payment error", e);
            Toast.makeText(context, "Payment Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseThumbnail;
        Button BuyCourse;
        TextView courseTitle, courseDescription, coursePrice,tvTeacherName;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseThumbnail = itemView.findViewById(R.id.ivCourseThumbnail);
            courseTitle = itemView.findViewById(R.id.tvCourseTitle);
            courseDescription = itemView.findViewById(R.id.tvCourseDescription);
            coursePrice = itemView.findViewById(R.id.tvCoursePrice);
            BuyCourse = itemView.findViewById(R.id.BuyCourse);
            tvTeacherName=itemView.findViewById(R.id.tvTeacherName);
        }
    }
}
