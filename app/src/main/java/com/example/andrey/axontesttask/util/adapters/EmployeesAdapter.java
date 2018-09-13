package com.example.andrey.axontesttask.util.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.andrey.axontesttask.R;
import com.example.andrey.axontesttask.api.entity.Employee;
import com.example.andrey.axontesttask.util.ItemSelectedListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andrey on 12.09.18.
 */

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {

    private List<Employee> employeesList;
    private Context context;
    ItemSelectedListener itemSelectedListener;

    public EmployeesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeesList.get(position);

        Picasso.get()
                .load(employee.getAvatar())
                .error(R.drawable.no_image)
                .into(holder.avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        String fullName = employee.getFirstName() + " " + employee.getSecondName();
        holder.fullName.setText(fullName);

        holder.content.setOnClickListener(v -> {
            itemSelectedListener.selectedEmployee(employee);
        });
    }

    @Override
    public int getItemCount() {
        if (employeesList != null)
            return employeesList.size();
        else return 0;
    }

    public void setEmployeesList(List<Employee> employeesList) {
        this.employeesList = employeesList;
        notifyDataSetChanged();
    }

    public void setItemSelectedListener(ItemSelectedListener itemSelectedListener){
        this.itemSelectedListener = itemSelectedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatarImageView)
        CircleImageView avatar;

        @BindView(R.id.fullNameTextView)
        TextView fullName;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.itemContentLayout)
        ViewGroup content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
