package com.example.infiny.mylocationtrackeradmin.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.claudiodegio.msv.MaterialSearchView;
import com.example.infiny.mylocationtrackeradmin.Adapter.TargetAdapter;
import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.IClickListener;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TargetActivity extends AppCompatActivity implements IClickListener {
    MenuItem  menuItem;
    SearchView search_view;
    MaterialSearchView materialSearchView;
    RecyclerView recycler_view;
    ArrayList<String> arrayList= new ArrayList<String>();
    TargetAdapter targetAdapter;
    TextView tv_no_records;
    boolean flag=false;
    VolleyUtils volleyUtils;
    IClickListener iClickListener;
    SessionManager sessionManager;
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
        fetchData();

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

        targetAdapter=new TargetAdapter(this,arrayList,iClickListener,tv_no_records);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(targetAdapter);
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
                targetAdapter.getFilter().filter(newText);

                return true;
            }
        });
    }

    private void fetchData() {
        ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        Map<String, String> params =new HashMap<String, String>();
        params.put("company_uid",sessionManager.getCompanyID());
        volleyUtils.getUserList(params, new NetworkResponse() {
            @Override
            public void receiveResult(Object result) {
                try {
                    JSONObject jsonObject=new JSONObject(result.toString());
                    switch (jsonObject.getString("error"))
                    {
                        case "0":
                            if (jsonObject.getJSONArray("user_list").length()==0) {
                                recycler_view.setVisibility(View.GONE);
                                tv_no_records.setVisibility(View.VISIBLE);
                            }
                            else {
                                recycler_view.setVisibility(View.VISIBLE);
                                tv_no_records.setVisibility(View.GONE);

                            }
                            break;
                        case "1002":
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },new ErrorVolleyUtils(mContext,progressDialog));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menuItem=menu.findItem(R.id.add_target);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.add_target:
                Intent intent=new Intent(this,AddTargetActivty.class);
                startActivityForResult(intent,100);
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
    public void click(String data) {
//        if(true)
//            startActivity(new Intent(TargetActivity.this,DetailsPreviousActivity.class));
//
//        else
        Intent intent=new Intent(TargetActivity.this,DetailsMapActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("data",data);
        intent.putExtra("dataBundle",bundle);
        startActivity(intent);
    }
}
