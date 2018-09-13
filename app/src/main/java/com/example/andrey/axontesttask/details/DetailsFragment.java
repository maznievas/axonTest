package com.example.andrey.axontesttask.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.example.andrey.axontesttask.R;
import com.example.andrey.axontesttask.api.entity.Employee;
import com.example.andrey.axontesttask.util.Const;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andrey on 12.09.18.
 */

public class DetailsFragment extends MvpAppCompatFragment implements DetailsView {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.fullNameTextView)
    TextView fullName;

    @BindView(R.id.avatarImageView)
    CircleImageView avatarImageView;

    @BindView(R.id.ageTextView)
    TextView age;

    private Unbinder unbinder;

    public static DetailsFragment newInstance() {

        Bundle args = new Bundle();

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        String fullName = getArguments().getString(Const.Employee.FIRST_NAME, "")
                + " " + getArguments().getString(Const.Employee.SECOND_NAME, "");
        int age = getArguments().getInt(Const.Employee.AGE, 0);

        this.fullName.setText(fullName);
        if (age != 0)
            this.age.setText(String.valueOf(age));
        Picasso.get()
                .load(getArguments().getString(Const.Employee.AVATAR_URL, "error"))
                .error(R.drawable.no_image)
                .into(this.avatarImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
