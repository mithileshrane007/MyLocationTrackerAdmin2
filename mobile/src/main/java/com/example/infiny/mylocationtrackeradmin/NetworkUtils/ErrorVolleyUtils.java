package com.example.infiny.mylocationtrackeradmin.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.infiny.mylocationtrackeradmin.R;

/**
 * Created by infiny on 13/12/16.
 */

public class ErrorVolleyUtils implements Response.ErrorListener  {


    private final Context mContext;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipe_refresh_layout;
    TextView tv_no_records;
    RecyclerView recycler_view;

    public ErrorVolleyUtils(Context context)
    {
        this.mContext=context;
    }

    public ErrorVolleyUtils(Context mContext, ProgressDialog progressDialog) {
        this.mContext=mContext;
        this.progressDialog=progressDialog;
    }


    public ErrorVolleyUtils(Context mContext, ProgressDialog progressDialog, SwipeRefreshLayout swipe_refresh_layout, TextView tv_no_records, RecyclerView recycler_view) {
        this.mContext=mContext;
        this.progressDialog=progressDialog;
        this.swipe_refresh_layout=swipe_refresh_layout;
        this.tv_no_records=tv_no_records;
        this.recycler_view=recycler_view;
    }

    public ErrorVolleyUtils(Context mContext, ProgressDialog progressDialog, TextView tv_no_records, RecyclerView recycler_view) {
        this.mContext=mContext;
        this.progressDialog=progressDialog;
        this.tv_no_records=tv_no_records;
        this.recycler_view=recycler_view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        error.printStackTrace();
        if (error instanceof TimeoutError)
        {
            Toast.makeText(mContext,"Ooops !!! Slow Internet Connection", Toast.LENGTH_SHORT).show();
        }
        if( error instanceof NetworkError) {
            Toast.makeText(mContext,"Ooops !!! Slow Internet Connection - Network Error", Toast.LENGTH_SHORT).show();

        }
        if( error instanceof ServerError) {
            Toast.makeText(mContext, R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof AuthFailureError) {
            Toast.makeText(mContext, R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof ParseError) {
            Toast.makeText(mContext,R.string.some_went_wrong_only, Toast.LENGTH_SHORT).show();

        }
        if( error instanceof NoConnectionError) {
            Toast.makeText(mContext,"No Internet Connection", Toast.LENGTH_SHORT).show();

        }


        if (progressDialog!=null)
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();

            }
        if (swipe_refresh_layout!=null)
            if (swipe_refresh_layout.isRefreshing())
            {
                swipe_refresh_layout.setRefreshing(false);
            }
        if (recycler_view!=null)
            recycler_view.setVisibility(View.GONE);
        if (tv_no_records!=null)
            tv_no_records.setVisibility(View.VISIBLE);
    }
}
