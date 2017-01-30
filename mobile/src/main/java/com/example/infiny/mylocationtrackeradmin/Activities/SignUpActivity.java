package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.infiny.mylocationtrackeradmin.ConfigApp.Config;
import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.infiny.mylocationtrackeradmin.R.id.tv_addr;
import static com.example.infiny.mylocationtrackeradmin.R.id.tv_passwd_signup_confirm_txt;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout content_sign_up,rel_signup,rel_login,rel_login_sucess;
    TextView tv_register,tv_details_sucess1;

    EditText tv_cmpy_id,tv_passwd;
    EditText tv_name,tv_email,tv_time_interval,tv_phone,tv_passwd_signup,tv_passwd_signup_confirm;

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
        getSupportActionBar().setTitle("My Tracker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext=this;

        sessionManager=new SessionManager(mContext);

        volleyUtils=new VolleyUtils();

        content_sign_up= (RelativeLayout) findViewById(R.id.content_sign_up);
        rel_signup= (RelativeLayout) findViewById(R.id.rel_signup);
        rel_login= (RelativeLayout) findViewById(R.id.rel_login);
        rel_login_sucess= (RelativeLayout) findViewById(R.id.rel_login_sucess);


        tv_cmpy_id= (EditText) findViewById(R.id.tv_cmpy_id);
        tv_passwd= (EditText) findViewById(R.id.tv_passwd);
        tv_name= (EditText) findViewById(R.id.tv_cmpy_name);
        tv_email= (EditText) findViewById(R.id.tv_owner_name);
        tv_time_interval= (EditText) findViewById(R.id.tv_time_interval);
        tv_phone= (EditText) findViewById(tv_addr);
        tv_passwd_signup= (EditText) findViewById(R.id.tv_passwd_signup);
        tv_passwd_signup_confirm= (EditText) findViewById(R.id.tv_passwd_signup_confirm);
        tv_register= (TextView) findViewById(R.id.tv_register);
        tv_details_sucess1= (TextView) findViewById(R.id.tv_details_sucess1);
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
//        tv_time_out.setOnClickListener(this);

//        hideSoftKeyboard(tv_time_out);
//        tv_time_out.setFocusable(false);
        hideSoftKeyboard(tv_time_interval);
        tv_time_interval.setFocusable(false);

//        content_sign_up.setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(this,BitmapFactory.decodeResource(getResources(),R.drawable.homescreen_min))));

        content_sign_up.setBackgroundColor(Color.WHITE);
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
                    if (validate_Uid_Pass()) {
                        progressDialog.show();

                        params=  new HashMap<String, String>();
                        params.put("login_id",tv_cmpy_id.getText().toString().trim());
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
                                            Toast.makeText(mContext,"Invalid credentials.",Toast.LENGTH_SHORT).show();

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
                    } else {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
                break;

            case R.id.btn_sign_up:

                try {
                    if (validate_SignUp()) {
                        progressDialog.show();

                        params=  new HashMap<String, String>();
                        params.put("name",tv_name.getText().toString());
                        params.put("phone",tv_phone.getText().toString());
                        params.put("password",tv_passwd_signup.getText().toString());
                        params.put("email",tv_email.getText().toString());
                        params.put("user_type", Config.CREATOR);


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
                                            tv_details_sucess1.setText(jsonObject.getString("code"));
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
                    } else {
                    }
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

            case R.id.btn_continue:

                flagScreen=0;
                Log.d("in","Data");
                rel_signup.setVisibility(View.GONE);
                rel_login.setVisibility(View.VISIBLE);
                rel_login_sucess.setVisibility(View.GONE);
                tv_name.setText("");
                tv_email.setText("");
                tv_time_interval.setText("");
                tv_phone.setText("");
                tv_passwd.setText("");
                tv_passwd_signup_confirm.setText("");
                tv_passwd_signup.setText("");
                tv_cmpy_id.setText("");
                break;
        }
    }

    private boolean validate_SignUp() {
        boolean tv_emailBool= tv_email.getText().toString().contains("@") && tv_email.getText().toString().contains(".");

        if (tv_name.getText().toString().equals("")||tv_email.getText().toString().equals("")||tv_phone.getText().toString().equals("")||tv_passwd_signup.getText().toString().equals("")||!tv_emailBool)
        {

            if (tv_name.getText().toString().equals(""))
            {
                tv_name.setFocusable(true);

                tv_name.setError("Invalid Name");
            }
            else {
                tv_name.setError(null);

            }


            if (tv_email.getText().toString().equals(""))
            {
                tv_email.setFocusable(true);

                tv_email.setError("Invalid Email");
            }
            else {
                tv_email.setError(null);

            }
            if (!tv_emailBool)
            {
                tv_email.setFocusable(true);

                tv_email.setError("Invalid Email");
            }
            else {
                tv_email.setError(null);

            }

            if (tv_phone.getText().toString().equals(""))
            {
                tv_phone.setFocusable(true);

                tv_phone.setError("Invalid Phone");
            }
            else {
                tv_phone.setError(null);

            }


            if (tv_passwd_signup.getText().toString().equals(""))
            {
                tv_passwd_signup.setFocusable(true);

                tv_passwd_signup.setError("Invalid Password");
            }
            else {
                tv_passwd_signup.setError(null);

            }
            if (tv_passwd_signup_confirm.getText().toString().equals(""))
            {
                tv_passwd_signup_confirm.setFocusable(true);
                tv_passwd_signup_confirm.setError("Invalid Confirm Password");
            }
            else {
                tv_passwd_signup_confirm.setError(null);

            }


            return false;
        }
        else {


            if (tv_passwd_signup_confirm.getText().toString().equals(""))
            {
                tv_passwd_signup_confirm.setFocusable(true);
                tv_passwd_signup_confirm.setError("Password mismatch");
                return  false;
            }
            else {
                if(tv_passwd_signup.getText().toString().trim().equals(tv_passwd_signup_confirm.getText().toString().trim())){
                    return true;
                }
                else {
                    tv_passwd_signup_confirm.setError("Password mismatch");
                    return false;
                }
            }
        }
    }

    private boolean validate_Uid_Pass() {
        if (tv_cmpy_id.getText().toString().equals("")||tv_passwd.getText().toString().equals(""))
        {

            if (tv_cmpy_id.getText().toString().equals(""))
            {
                tv_cmpy_id.setFocusable(true);
                tv_cmpy_id.setError("Invalid Company ID");
            }
            else {
                tv_cmpy_id.setError(null);

            }


            if (tv_passwd.getText().toString().equals(""))
            {
                tv_passwd.setFocusable(true);

                tv_passwd.setError("Invalid Password");
            }
            else {
                tv_passwd.setError(null);

            }



            return false;
        }
        else {


            return true;
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
            tv_name.setText("");
            tv_email.setText("");
            tv_time_interval.setText("");
            tv_phone.setText("");
            tv_passwd.setText("");
            tv_passwd_signup.setText("");
            tv_cmpy_id.setText("");


        }else if (flagScreen==2)
        {
            rel_login_sucess.setVisibility(View.VISIBLE);
            tv_cmpy_id.setText("");
            tv_passwd.setText("");


        }
        else
            super.onBackPressed();

    }
}
