package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;
import com.example.infiny.mylocationtrackeradmin.Utils.BlurBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout content_sign_up,rel_signup,rel_login,rel_login_sucess;
    TextView tv_cmpy_id_txt,tv_passwd_txt,tv_register,tv_details_sucess1;
    TextView tv_cmpy_name_title,tv_owner_name_title,tv_time_interval_title,tv_time_out_title;

    EditText tv_cmpy_id,tv_passwd;
    EditText tv_cmpy_name,tv_owner_name,tv_time_interval,tv_time_out,tv_passwd_signup;
    Button btn_sign_in,btn_sign_up;
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

        if (flagScreen==0) {
            rel_signup.setVisibility(View.GONE);
            rel_login.setVisibility(View.VISIBLE);
            rel_login_sucess.setVisibility(View.GONE);
        }
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        tv_register.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Map<String, String> params = null;
        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");

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

        }
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
