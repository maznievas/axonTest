package com.example.andrey.axontesttask;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.andrey.axontesttask.employees.EmployeesFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String EMPLOYEES_FRAGMENT_TAG = "employees_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, EmployeesFragment.newInstance())
                    .addToBackStack(EMPLOYEES_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) instanceof EmployeesFragment)
            finish();
        else
            super.onBackPressed();
    }
}
