package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;
import com.example.infiny.mylocationtrackeradmin.Utils.BlurBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout content_sign_up,rel_signup,rel_login,rel_login_sucess;
    TextView tv_cmpy_id_txt,tv_passwd_txt,tv_register,tv_details_sucess1;
    TextView tv_cmpy_name_title,tv_owner_name_title,tv_time_interval_title,tv_time_out_title;

    EditText tv_cmpy_id,tv_passwd;
    EditText tv_cmpy_name,tv_owner_name,tv_time_interval,tv_time_out,tv_passwd_signup;
    Button btn_sign_in,btn_sign_up,btn_continue;
    VolleyUtils volleyUtils;
    SessionManager sessionManager;
    private Context mContext;
    private int flagScreen=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        mContext=this;

        sessionManager=new SessionManager(mContext);

        volleyUtils=new VolleyUtils();

        content_sign_up= (RelativeLayout) findViewById(R.id.content_sign_up);
        rel_signup= (RelativeLayout) findViewById(R.id.rel_signup);
        rel_login= (RelativeLayout) findViewById(R.id.rel_login);
        rel_login_sucess= (RelativeLayout) findViewById(R.id.rel_login_sucess);

        tv_cmpy_id_txt= (TextView) findViewById(R.id.tv_cmpy_id_txt);
        tv_passwd_txt= (TextView) findViewById(R.id.tv_passwd_txt);
        tv_register= (TextView) findViewById(R.id.tv_register);
        tv_cmpy_name_title= (TextView) findViewById(R.id.tv_cmpy_name_title);
        tv_owner_name_title= (TextView) findViewById(R.id.tv_owner_name_title);
        tv_time_interval_title= (TextView) findViewById(R.id.tv_time_interval_title);
        tv_time_out_title= (TextView) findViewById(R.id.tv_time_out_title);
        tv_details_sucess1= (TextView) findViewById(R.id.tv_details_sucess1);

        tv_cmpy_id= (EditText) findViewById(R.id.tv_cmpy_id);
        tv_passwd= (EditText) findViewById(R.id.tv_passwd);
        tv_cmpy_name= (EditText) findViewById(R.id.tv_cmpy_name);
        tv_owner_name= (EditText) findViewById(R.id.tv_owner_name);
        tv_time_interval= (EditText) findViewById(R.id.tv_time_interval);
        tv_time_out= (EditText) findViewById(R.id.tv_time_out);
        tv_passwd_signup= (EditText) findViewById(R.id.tv_passwd_signup);

        btn_sign_in= (Button) findViewById(R.id.btn_sign_in);
        btn_sign_up= (Button) findViewById(R.id.btn_sign_up);
        btn_continue= (Button) findViewById(R.id.btn_continue);



        if (sessionManager.isLogin())
        {
            startActivity(new Intent(SignUpActivity.this,TargetActivity.class));
            finish();
        }
        if (flagScreen==0) {
            rel_signup.setVisibility(View.GONE);
            rel_login.setVisibility(View.VISIBLE);
            rel_login_sucess.setVisibility(View.GONE);
        }
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        tv_time_interval.setOnClickListener(this);
        tv_time_out.setOnClickListener(this);

        hideSoftKeyboard(tv_time_out);
        tv_time_out.setFocusable(false);
        hideSoftKeyboard(tv_time_interval);
        tv_time_interval.setFocusable(false);

        content_sign_up.setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(this,BitmapFactory.decodeResource(getResources(),R.drawable.homescreen_min))));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flagScreen==0) {
            rel_signup.setVisibility(View.GONE);
            rel_login.setVisibility(View.VISIBLE);
        }
    }
    public void hideSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    @Override
    public void onClick(View view) {
        Map<String, String> params = null;
        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        switch (view.getId())
        {
            case R.id.btn_sign_in:
                try {
                    progressDialog.show();

                    params=  new HashMap<String, String>();
                    params.put("company_id",tv_cmpy_id.getText().toString());
                    params.put("password",tv_passwd.getText().toString());
                    volleyUtils.signIn(params, new NetworkResponse() {
                        @Override
                        public void receiveResult(Object result) {
                            try {
                                progressDialog.dismiss();

                                JSONObject jsonObject=new JSONObject(result.toString());
                                switch (jsonObject.getString("error"))
                                {
                                    case "0":
                                        startActivity(new Intent(SignUpActivity.this,TargetActivity.class));
                                        sessionManager.storeCompanyID(tv_cmpy_id.getText().toString().trim());
                                        sessionManager.setLogin(true);
                                        tv_cmpy_id.setText("");
                                        tv_passwd.setText("");
                                        break;
                                    case "1002":
                                        Toast.makeText(mContext,"Invalid details.",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "1003":
                                        Toast.makeText(mContext,"Data is invalid",Toast.LENGTH_SHORT).show();

                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },new ErrorVolleyUtils(mContext,progressDialog));
                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
                break;

            case R.id.btn_sign_up:

                try {
                    progressDialog.show();

                    params=  new HashMap<String, String>();
                    params.put("name",tv_cmpy_name.getText().toString());
                    params.put("time_interval",tv_time_interval.getText().toString());
                    params.put("time_out",tv_time_out.getText().toString());
                    params.put("password",tv_passwd_signup.getText().toString());
                    params.put("owner_name",tv_owner_name.getText().toString());

                    volleyUtils.signUp(params, new NetworkResponse() {
                        @Override
                        public void receiveResult(Object result) {
                            try {
                                progressDialog.dismiss();

                                JSONObject jsonObject=new JSONObject(result.toString());
                                switch (jsonObject.getString("error"))
                                {
                                    case "0":
                                        rel_signup.setVisibility(View.GONE);
                                        rel_login.setVisibility(View.GONE);
                                        rel_login_sucess.setVisibility(View.VISIBLE);
                                        flagScreen=2;
                                        tv_details_sucess1.setText(jsonObject.getString("company_user_id"));
                                        Toast.makeText(mContext,"Successfully Created",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "1002":
                                        rel_signup.setVisibility(View.VISIBLE);
                                        rel_login.setVisibility(View.GONE);
                                        rel_login_sucess.setVisibility(View.GONE);

                                        Toast.makeText(mContext,R.string.some_went_wrong_only,Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },new ErrorVolleyUtils(mContext,progressDialog));
                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                break;

            case R.id.tv_register:
                flagScreen=1;
                rel_signup.setVisibility(View.VISIBLE);
                rel_login.setVisibility(View.GONE);
                break;

            case R.id.tv_time_interval:
                showTime(tv_time_interval);
                break;

            case R.id.tv_time_out:

                showTime(tv_time_out);
                break;

            case R.id.btn_continue:

                flagScreen=0;
                Log.d("in","Data");
                rel_signup.setVisibility(View.GONE);
                rel_login.setVisibility(View.VISIBLE);
                rel_login_sucess.setVisibility(View.GONE);
                tv_cmpy_name.setText("");
                tv_owner_name.setText("");
                tv_time_interval.setText("");
                tv_time_out.setText("");
                tv_passwd.setText("");
                break;
        }
    }

    private void showTime(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( ""+selectedHour + ":" + selectedMinute);
            }
        }, hour, minute,false);

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    @Override
    public void onBackPressed() {

        if (flagScreen==1)
        {
            flagScreen=0;
            rel_signup.setVisibility(View.GONE);
            rel_login.setVisibility(View.VISIBLE);
            rel_login_sucess.setVisibility(View.GONE);


        }else if (flagScreen==2)
        {
            rel_login_sucess.setVisibility(View.VISIBLE);


        }
        else
            super.onBackPressed();

    }
}
