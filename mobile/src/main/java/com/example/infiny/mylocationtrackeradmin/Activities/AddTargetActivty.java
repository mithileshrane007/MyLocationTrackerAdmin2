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
import com.example.infiny.mylocationtrackeradmin.Models.User_list;
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
    TextView tv_tracker_id,tv_generate_tracker_id;
    Button btn_add_target;
    VolleyUtils volleyUtils;
    Switch switch_type;
    User_list bundle;
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



        switch_type= (Switch) findViewById(R.id.switch_type);
        iv_target= (ImageView) findViewById(R.id.iv_target);
        tv_name= (TextInputLayout) findViewById(R.id.tv_name);
        tv_dob= (TextInputLayout) findViewById(R.id.tv_dob);
        tv_phone= (TextInputLayout) findViewById(R.id.tv_phone);

        tv_email= (TextInputLayout) findViewById(R.id.tv_email);
        tv_desp= (TextInputLayout) findViewById(R.id.tv_desp);
        tv_addr= (TextInputLayout) findViewById(R.id.tv_addr);
        tv_tracker_id= (TextView) findViewById(R.id.tv_tracker_id);
        tv_generate_tracker_id= (TextView) findViewById(R.id.tv_generate_tracker_id);

        hideSoftKeyboard(tv_dob.getEditText());
        tv_dob.getEditText().setFocusable(false);
        btn_add_target= (Button) findViewById(R.id.btn_add_target);
        generatePIN();
        bundle= (User_list) getIntent().getSerializableExtra("details");
        if (bundle!=null)
        {

            tv_name.getEditText().setText(bundle.getName());
            tv_dob.getEditText().setText(bundle.getDateofbirth());
            tv_phone.getEditText().setText(bundle.getPhone());
            tv_email.getEditText().setText(bundle.getEmail_id());
            tv_desp.getEditText().setText(bundle.getDescription());
            tv_addr.getEditText().setText(bundle.getAddress());
            if (bundle.getUser_type().equals("employee"))
                switch_type.setChecked(false);
            else
                switch_type.setChecked(true);
            tv_tracker_id.setText(bundle.getTrack_id_reg());
            btn_add_target.setVisibility(View.GONE);


            tv_name.setEnabled(false);
            tv_dob.setEnabled(false);
            tv_phone.setEnabled(false);
            tv_email.setEnabled(false);
            tv_addr.setEnabled(false);
            tv_desp.setEnabled(false);
            tv_tracker_id.setEnabled(false);
            switch_type.setEnabled(false);
        }
        btn_add_target.setVisibility(View.VISIBLE);




        tv_generate_tracker_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePIN();
            }
        });



        btn_add_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData())
                {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name",tv_name.getEditText().getText().toString());
                    params.put("user_type",switch_type.isChecked()?"manager":"employee");
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

                                    switch (jsonObject.getString("error"))
                                    {

                                        case "0":
                                            setResult(101);
                                            finish();//finishing activity

                                            Toast.makeText(mContext,"Successfully added.",Toast.LENGTH_SHORT).show();
                                            break;
                                        case "1002":
                                            finish();
                                            Toast.makeText(mContext,"Unsuccessfully.Try again later.",Toast.LENGTH_SHORT).show();
                                            break;
                                        case "1003":
                                            Toast.makeText(mContext,R.string.some_went_wrong_only,Toast.LENGTH_SHORT).show();
                                            if (jsonObject.getBoolean("phone_email"))
                                            {
                                                Toast.makeText(mContext,"Already phone or email exists.",Toast.LENGTH_SHORT).show();
                                                tv_email.getEditText().setError(null);
                                                tv_phone.getEditText().setError(null);

                                            }
                                            if (jsonObject.getBoolean("track_id_reg"))
                                            {
                                                Toast.makeText(mContext,"Tracker ID is already taken.Please regenerate.",Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },new ErrorVolleyUtils(mContext,progressDialog));
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }else {

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

    private boolean validateData() {
        boolean tv_emailBool= tv_email.getEditText().getText().toString().contains("@") && tv_email.getEditText().getText().toString().contains(".");
        if (tv_name.getEditText().getText().toString().equals("")||tv_dob.getEditText().getText().toString().equals("") ||tv_addr.getEditText().getText().toString().equals("")||tv_phone.getEditText().getText().toString().equals("")
                ||tv_email.getEditText().getText().toString().equals("")||tv_desp.getEditText().getText().toString().equals("")||!tv_emailBool ||tv_phone.getEditText().getText().toString().length()!=10)
        {

            if (tv_name.getEditText().getText().toString().equals(""))
            {
                tv_name.setFocusable(true);

                tv_name.setError("Invalid Name");
            }
            else {
                tv_name.setError(null);

            }


            if (tv_dob.getEditText().getText().toString().equals(""))
            {
                tv_dob.setFocusable(true);

                tv_dob.setError("Invalid DOB");
            }
            else {
                tv_dob.setError(null);

            }

            if (tv_addr.getEditText().getText().toString().equals(""))
            {
                tv_addr.setFocusable(true);

                tv_addr.setError("Invalid address");
            }
            else {
                tv_addr.setError(null);

            }

            if (tv_phone.getEditText().getText().toString().equals("") ||tv_phone.getEditText().getText().toString().length()!=10)
            {
                tv_phone.setFocusable(true);

                tv_phone.setError("Invalid phone");
            }
            else {
                tv_phone.setError(null);

            }

            if (tv_email.getEditText().getText().toString().equals(""))
            {
                tv_email.setFocusable(true);

                tv_email.setError("Invalid email");
            }
            else {
                tv_email.setError(null);

            }

            if (tv_desp.getEditText().getText().toString().equals(""))
            {                tv_desp.setError("Invalid Description");
                tv_desp.setFocusable(true);
            }
            else {
                tv_desp.setError(null);

            }
            if (!tv_emailBool)
            {
                tv_email.setFocusable(true);

                tv_email.setError("Invalid email");
            }
            else {
                tv_email.setError(null);

            }

            return false;
        }
        else {
            return true;
        }



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
