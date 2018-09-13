package com.example.andrey.axontesttask.employees;

import com.arellomobile.mvp.MvpView;
import com.example.andrey.axontesttask.api.entity.Employee;

import java.util.List;

/**
 * Created by andrey on 12.09.18.
 */

public interface EmployeesView extends MvpView {
    void showLoadingState();
    void hideLoadingState();
    void setEmployees(List<Employee> employeesList);
}
