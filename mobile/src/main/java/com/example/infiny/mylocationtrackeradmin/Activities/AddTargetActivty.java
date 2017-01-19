package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AddTargetActivty extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    ImageView iv_target;
    TextInputLayout tv_name,tv_dob,tv_addr,tv_phone,tv_desp,tv_email;
    TextView tv_tracker_id,tv_details_sucess1;
    Button btn_add_target;
    VolleyUtils volleyUtils;
    Switch switch_type;
    Bundle bundle;
    SessionManager sessionManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_target);
        mContext=this;
        sessionManager=new SessionManager(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add");
        volleyUtils=new VolleyUtils();

        bundle=getIntent().getBundleExtra("details");
        if (bundle!=null)
            if (bundle.isEmpty())
            {
                Toast.makeText(mContext,"bucdle emoty",Toast.LENGTH_SHORT).show();
            }else {

            }

        switch_type= (Switch) findViewById(R.id.switch_type);
        iv_target= (ImageView) findViewById(R.id.iv_target);
        tv_name= (TextInputLayout) findViewById(R.id.tv_name);
        tv_dob= (TextInputLayout) findViewById(R.id.tv_dob);
        tv_phone= (TextInputLayout) findViewById(R.id.tv_phone);

        tv_email= (TextInputLayout) findViewById(R.id.tv_email);
        tv_desp= (TextInputLayout) findViewById(R.id.tv_desp);
        tv_addr= (TextInputLayout) findViewById(R.id.tv_addr);
        tv_tracker_id= (TextView) findViewById(R.id.tv_tracker_id);
        tv_details_sucess1= (TextView) findViewById(R.id.tv_details_sucess1);

        hideSoftKeyboard(tv_dob.getEditText());
        tv_dob.getEditText().setFocusable(false);
        btn_add_target= (Button) findViewById(R.id.btn_add_target);
        generatePIN();


        btn_add_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",tv_name.getEditText().getText().toString());
                params.put("user_type",String.valueOf(switch_type.isChecked()?1:2));
                params.put("address",tv_addr.getEditText().getText().toString());
                params.put("phone",tv_phone.getEditText().getText().toString());
                params.put("dateofbirth",tv_dob.getEditText().getText().toString());
                params.put("description",tv_desp.getEditText().getText().toString());
                params.put("email_id",tv_email.getEditText().getText().toString());

                params.put("company_id",sessionManager.getCompanyID());
                params.put("track_id_reg",tv_tracker_id.getText().toString());

                try {
                    final ProgressDialog progressDialog=new ProgressDialog(mContext);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    volleyUtils.add_user(params,new NetworkResponse() {
                        @Override
                        public void receiveResult(Object result) {

                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject=new JSONObject(result.toString());
                                Toast.makeText(mContext,jsonObject.toString(),Toast.LENGTH_SHORT).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },new ErrorVolleyUtils(mContext,progressDialog));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        tv_dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        tv_dob.getEditText().setText(sdf.format(myCalendar.getTime()));
                        tv_dob.setError(null);
//                        setData();
                    }


                }, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_desp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tv_desp.getEditText(), InputMethodManager.SHOW_IMPLICIT);
            }
        });
        tv_desp.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(tv_desp.getEditText(), InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv_desp.getEditText().getWindowToken(), 0);
                }
            }
        });

    }


    public void hideSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return true;

    }

    public void generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        tv_tracker_id.setText(""+randomPIN);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
