package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiodegio.msv.MaterialSearchView;
import com.example.infiny.mylocationtrackeradmin.Adapter.TargetAdapter;
import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.IClickListener;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.Models.CompanyList;
import com.example.infiny.mylocationtrackeradmin.Models.User_list;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TargetActivity extends AppCompatActivity implements IClickListener {
    MenuItem  menuItem;
    SearchView search_view;
    MaterialSearchView materialSearchView;
    RecyclerView recycler_view;
    ArrayList<User_list> arrayList= new ArrayList<User_list>();
    TargetAdapter targetAdapter;
    TextView tv_no_records;
    boolean flag=false;
    VolleyUtils volleyUtils;
    IClickListener iClickListener;
    SessionManager sessionManager;
    SwipeRefreshLayout swipe_refresh_layout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        mContext=this;
        volleyUtils=new VolleyUtils();
        sessionManager=new SessionManager(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Target");
        recycler_view= (RecyclerView) findViewById(R.id.recycler_view);
        search_view= (SearchView) findViewById(R.id.search_view);
        tv_no_records= (TextView) findViewById(R.id.tv_no_records);

//        if (Build.VERSION.SDK_INT >= 24) {
//            search_view.setQueryHint(Html.fromHtml("<font color = #fff/>" + "Search Target",1) );
//        } else {
//            search_view.setQueryHint(Html.fromHtml("<font color = #fff/>" + "Search Target") );
//        }

        search_view.clearFocus(); // close the keyboard on load
        iClickListener=this;
//        arrayList.add("January");
//        arrayList.add("February");
//        arrayList.add("March");
//        arrayList.add("April");
//        arrayList.add("May");
//        arrayList.add("June");
//        arrayList.add("July");
//        arrayList.add("August");
//        arrayList.add("September");
//        arrayList.add("October");
//        arrayList.add("November");
//        arrayList.add("December");
//
//


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        swipe_refresh_layout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tv_no_records.setVisibility(View.GONE);
                swipe_refresh_layout.setRefreshing(true);

                fetchData();
            }
        });





        swipe_refresh_layout.post(new Runnable() {
                                      @Override
                                      public void run() {
                                          tv_no_records.setVisibility(View.GONE);
                                          fetchData();
                                      }
                                  }
        );

//        recycler_view.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        search_view.setIconifiedByDefault(false);

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                targetAdapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    targetAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    private void fetchData() {
        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
//        progressDialog.show();
        swipe_refresh_layout.setRefreshing(true);

        progressDialog.setCancelable(false);
        Map<String, String> params =new HashMap<String, String>();
        params.put("company_uid",sessionManager.getCompanyID());
        volleyUtils.getUserList(params, new NetworkResponse() {
            @Override
            public void receiveResult(Object result) {
                try {
                    search_view.clearFocus(); // close the keyboard on load

                    JSONObject jsonObject=new JSONObject(result.toString());
                    switch (jsonObject.getString("error"))
                    {
                        case "0":
                            if (jsonObject.getJSONArray("user_list").length()==0) {
                                recycler_view.setVisibility(View.GONE);
                                swipe_refresh_layout.setRefreshing(false);

                                tv_no_records.setVisibility(View.VISIBLE);
                                swipe_refresh_layout.setRefreshing(false);

                            }
                            else {
                                arrayList.clear();
                                swipe_refresh_layout.setRefreshing(false);

                                recycler_view.setVisibility(View.VISIBLE);
                                tv_no_records.setVisibility(View.GONE);
                                CompanyList companyList=new CompanyList();
                                Gson gson=new Gson();
                                companyList=gson.fromJson(jsonObject.toString(), CompanyList.class);

                                User_list[] user_list=new User_list[companyList.getUser_list().length];
                                user_list=companyList.getUser_list();
                                for (int i=0;i<companyList.getUser_list().length;i++)
                                {
                                    arrayList.add(user_list[i]);
                                }

                                targetAdapter=new TargetAdapter(mContext,arrayList,iClickListener,tv_no_records);
                                recycler_view.setAdapter(targetAdapter);



                            }

//                            progressDialog.dismiss();
                            break;
                        case "1002":
//                            progressDialog.dismiss();
                            swipe_refresh_layout.setRefreshing(false);

                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                            Toast.makeText(mContext,"No data found",Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (Exception e) {
                    swipe_refresh_layout.setRefreshing(false);
                    search_view.clearFocus(); // close the keyboard on load

//                    progressDialog.dismiss();
                    tv_no_records.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        },new ErrorVolleyUtils(mContext,progressDialog,swipe_refresh_layout,tv_no_records,recycler_view));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menuItem=menu.findItem(R.id.add_target);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        search_view.clearFocus(); // close the keyboard on load

        if (requestCode==100&& resultCode==101)
        {
            fetchData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.add_target:
                final Intent intent=new Intent(this,AddTargetActivty.class);
//                final ProgressDialog progressDialog=new ProgressDialog(mContext);
//                progressDialog.setMessage("Please wait..");
//                progressDialog.show();

                startActivityForResult(intent,100);

//                volleyUtils.generatePin(new NetworkResponse() {
//                    @Override
//                    public void receiveResult(Object result) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(result.toString());
//                            progressDialog.dismiss();
////                            intent.putExtra("pin",jsonObject.getString("pin"));
////                            startActivityForResult(intent,100);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },new ErrorVolleyUtils(mContext,progressDialog));
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void click(User_list data) {
//        if(true)
//            startActivity(new Intent(TargetActivity.this,DetailsPreviousActivity.class));
//
//        else
        Intent intent=new Intent(TargetActivity.this,DetailsMapActivity.class);

        intent.putExtra("dataBundle",data);
        startActivity(intent);

    }
}
