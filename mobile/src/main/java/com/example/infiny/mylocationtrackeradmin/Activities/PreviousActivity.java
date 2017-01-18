package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.infiny.mylocationtrackeradmin.Adapter.PreviousActivityAdapter;
import com.example.infiny.mylocationtrackeradmin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class PreviousActivity extends AppCompatActivity implements View.OnClickListener {

    final Calendar myCalendar = Calendar.getInstance();
    TextView tv_no_records_prev_detail,tv_go,tv_to_date,tv_from_date;
    RecyclerView recycler_view_previous_detal;
    PreviousActivityAdapter previousActivityAdapter;
    LinearLayout ll_filter_view;
    private MenuItem filter_item;
    private int flag=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_no_records_prev_detail= (TextView) findViewById(R.id.tv_no_records_prev_detail);
        recycler_view_previous_detal= (RecyclerView) findViewById(R.id.recycler_view_previous_detal);
        tv_from_date= (TextView) findViewById(R.id.tv_from_date);
        tv_go= (TextView) findViewById(R.id.tv_go);
        tv_to_date= (TextView) findViewById(R.id.tv_to_date);
        ll_filter_view= (LinearLayout) findViewById(R.id.ll_filter_view);

        tv_from_date.setOnClickListener(this);
        tv_go.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter,menu);
        filter_item=menu.findItem(R.id.filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.filter:
                if (flag==1) {
                    flag=0;
                    ll_filter_view.setVisibility(View.VISIBLE);
                }
                else {
                    flag=1;
                    ll_filter_view.setVisibility(View.GONE);
                }
                break;
        }
        return true;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_from_date:
                getDatePicker(tv_from_date);
                break;
            case R.id.tv_go:

                break;
            case R.id.tv_to_date:
                getDatePicker(tv_to_date);
                break;
        }

    }


    private void getDatePicker(final TextView textView)
    {


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textView.setText(sdf.format(myCalendar.getTime()));
//                        setData();
            }


        }, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
