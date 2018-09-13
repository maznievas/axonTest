package com.example.andrey.axontesttask.employees;

import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.andrey.axontesttask.R;
import com.example.andrey.axontesttask.api.entity.Employee;
import com.example.andrey.axontesttask.details.DetailsFragment;
import com.example.andrey.axontesttask.util.Const;
import com.example.andrey.axontesttask.util.ItemSelectedListener;
import com.example.andrey.axontesttask.util.ListItemDecoration;
import com.example.andrey.axontesttask.util.adapters.EmployeesAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by andrey on 12.09.18.
 */

public class EmployeesFragment extends MvpAppCompatFragment implements EmployeesView,
        ItemSelectedListener {

    @BindView(R.id.employeeesRecyclerView)
    RecyclerView employeesRV;

    @InjectPresenter
    EmployeesPresenter employeesPresenter;

    Unbinder unbinder;
    private EmployeesAdapter employeesAdapter;
    private ProgressDialog progressDialog;
    private DetailsFragment detailsFragment;
    private final String DETAILS_FRAGMENT_TAG = "details_fragment";

    public static EmployeesFragment newInstance() {

        Bundle args = new Bundle();

        EmployeesFragment fragment = new EmployeesFragment();
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
        View view = inflater.inflate(R.layout.fragment_employess, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.loading));

        employeesPresenter.fetchEmployees(getContext().getAssets());

        employeesAdapter = new EmployeesAdapter(getContext());
        employeesAdapter.setItemSelectedListener(this);

        employeesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        employeesRV.setAdapter(employeesAdapter);
        employeesRV.addItemDecoration(new ListItemDecoration((int) getResources().getDimension(R.dimen.space_betwen_items)));
    }

    @Override
    public void showLoadingState() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingState() {
        progressDialog.dismiss();
    }

    @Override
    public void setEmployees(List<Employee> employeesList) {
        employeesAdapter.setEmployeesList(employeesList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        employeesPresenter.clear();
    }

    @Override
    public void selectedEmployee(Employee employee) {
        if (detailsFragment == null)
            detailsFragment = DetailsFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(Const.Employee.FIRST_NAME, employee.getFirstName());
        bundle.putString(Const.Employee.SECOND_NAME, employee.getSecondName());
        bundle.putString(Const.Employee.AVATAR_URL, employee.getAvatar());
        bundle.putInt(Const.Employee.AGE, employee.getAge());
        detailsFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailsFragment)
                .addToBackStack(DETAILS_FRAGMENT_TAG)
                .commit();
    }
}
