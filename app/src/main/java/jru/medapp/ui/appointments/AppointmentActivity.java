package jru.medapp.ui.appointments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import jru.medapp.R;
import jru.medapp.app.Constants;
import jru.medapp.databinding.ActivityAppointmentBinding;
import jru.medapp.model.data.Appointment;
import jru.medapp.model.data.Clinic;
import jru.medapp.ui.appointments.detail.AppointmentDetailActivity;

public class AppointmentActivity extends MvpActivity<AppointmentView, AppointmentPresenter> implements AppointmentView {

    ActivityAppointmentBinding binding;
    private AppointmentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getAppointments();
            }
        });

    }


    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setList(List<Appointment> list) {
        adapter.setList(list);
    }


    @Override
    public void OnItemClicked(Appointment item) {
        Intent intent = new Intent(AppointmentActivity.this, AppointmentDetailActivity.class);
        intent.putExtra(Constants.ID, item.getTransId());
        intent.putExtra("from", "list");
        startActivity(intent);
    }

    @Override
    public void OnClinicClicked
            (Clinic clinic) {
       /* Intent intent = new Intent(AppointmentActivity.this, ClinicActivity.class);
        intent.putExtra("id", clinic.getClinicId());
        startActivity(intent);*/
    }

    @Override
    public AppointmentPresenter createPresenter() {
        return new AppointmentPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }
}
