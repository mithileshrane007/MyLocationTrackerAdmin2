package com.example.infiny.mylocationtrackeradmin.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infiny.mylocationtrackeradmin.ConfigApp.Config;
import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtrackeradmin.Models.User_list;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtrackeradmin.NetworkUtils.VolleyUtils;
import com.example.infiny.mylocationtrackeradmin.R;
import com.example.infiny.mylocationtrackeradmin.Utils.ExifUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddTargetActivty extends AppCompatActivity {

    private static final int MY_PERMISSION_WRITE_READ = 25;
    private static final int CHOOSEGALLERY = 300;
    private static final int CHOOSECAMERA = 200;


    final Calendar myCalendar = Calendar.getInstance();
    ImageView iv_target;
    TextInputLayout tv_name,tv_dob,tv_addr,tv_phone,tv_desp,tv_email,tv_time_interval,tv_time_out,tv_pass;
    TextView tv_tracker_id,tv_generate_tracker_id;
    Button btn_add_target;
    VolleyUtils volleyUtils;
    Switch switch_type;
    User_list bundle;
    SessionManager sessionManager;
    private Context mContext;
    private Uri imageUri;
    private String image_string,pin_string;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyTracker");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else {
            return null;
        }

        return mediaFile;
    }

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

        tv_time_interval= (TextInputLayout) findViewById(R.id.tv_time_interval);
        tv_time_out= (TextInputLayout) findViewById(R.id.tv_time_out);

        tv_email= (TextInputLayout) findViewById(R.id.tv_email);
        tv_desp= (TextInputLayout) findViewById(R.id.tv_desp);
        tv_addr= (TextInputLayout) findViewById(R.id.tv_addr);
        tv_tracker_id= (TextView) findViewById(R.id.tv_tracker_id);
        tv_generate_tracker_id= (TextView) findViewById(R.id.tv_generate_tracker_id);

        tv_pass= (TextInputLayout) findViewById(R.id.tv_pass);


        hideSoftKeyboard(tv_dob.getEditText());
        tv_dob.getEditText().setFocusable(false);
        btn_add_target= (Button) findViewById(R.id.btn_add_target);
        if(getIntent().getStringExtra("pin")==null)
            bundle= (User_list) getIntent().getSerializableExtra("details");

        pin_string=getIntent().getStringExtra("pin");
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


            tv_time_out.getEditText().setText(bundle.getAddress());   // need to change
            tv_time_interval.getEditText().setText(bundle.getAddress());// need to change

            tv_time_out.setEnabled(false);
            tv_time_interval.setEnabled(false);
            tv_name.setEnabled(false);
            tv_dob.setEnabled(false);
            tv_phone.setEnabled(false);
            tv_email.setEnabled(false);
            tv_addr.setEnabled(false);
            tv_pass.setEnabled(false);
            tv_desp.setEnabled(false);
            tv_tracker_id.setEnabled(false);
            switch_type.setEnabled(false);
        }
        generatePIN();

        btn_add_target.setVisibility(View.VISIBLE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera_default);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        image_string = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        iv_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsMenu();
            }
        });


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
                    params.put("user_type", Config.TARGET);
                    params.put("address",tv_addr.getEditText().getText().toString());
                    params.put("phone",tv_phone.getEditText().getText().toString());
                    params.put("dateofbirth",tv_dob.getEditText().getText().toString());
                    params.put("description",tv_desp.getEditText().getText().toString());
                    params.put("email",tv_email.getEditText().getText().toString());
                    params.put("image",image_string);
                    params.put("job_time_interval",tv_time_interval.getEditText().getText().toString());
                    params.put("job_time_out",tv_time_out.getEditText().getText().toString());
                    params.put("login_id",sessionManager.getCompanyID());
                    params.put("password",tv_pass.getEditText().getText().toString());
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

                DatePickerDialog datePickerDialog=new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
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
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
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

    private void showOptionsMenu() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String[] alertList={getResources().getString(R.string.Choose_from_camera),getResources().getString(R.string.Choose_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.choose_image).setCancelable(true).setItems(alertList,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            switch (i)
                            {
                                case 0:
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent, CHOOSECAMERA);
                                    break;

                                case 1:
                                    Intent intent1 = new Intent();
                                    intent1.setType("image/*");
                                    intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent1, "Select Picture"), CHOOSEGALLERY);

                                    break;
                            }
                        }
                    }
            )
                    .show();

        }else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ) {
                ActivityCompat.requestPermissions((Activity)this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AddTargetActivty.MY_PERMISSION_WRITE_READ);
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            byte[] imgarr=null;


                switch (requestCode)
                {
                    case CHOOSECAMERA:
                        imgarr =fromCamera(imageUri);
                        image_string = Base64.encodeToString(imgarr, Base64.DEFAULT);
                        iv_target.setImageBitmap(BitmapFactory.decodeByteArray(imgarr,0,imgarr.length));

                        break;
                    case CHOOSEGALLERY:
                        imgarr = fromGallary(data.getData());
                        iv_target.setImageBitmap(BitmapFactory.decodeByteArray(imgarr,0,imgarr.length));
                        image_string = Base64.encodeToString(imgarr, Base64.DEFAULT);
                        break;
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] fromGallary(Uri filePath)
    {        Bitmap photo11 = null;



        try {
            Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath), null, null);

            final int sizeInBytes = image.getByteCount();
            Log.e("sizeInBytes:", String.valueOf(sizeInBytes));
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options1.inSampleSize = 2;

            Bitmap scaled = Bitmap.createScaledBitmap(image, 350, 350, false);


            Bitmap scaled1= ExifUtils.rotateBitmap(ExifUtils.getPath(this.mContext, filePath),scaled);


            Bitmap bitmapCorr= ExifUtils.rotateBitmap(filePath.getPath(),scaled1);
            if(sizeInBytes>18000000) {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte[] ba = bao.toByteArray();
                return ba;
            }
            else if(sizeInBytes>2000000)
            {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte[] ba = bao.toByteArray();
                return ba;
            }
            else {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);

                byte[] ba = bao.toByteArray();

                return ba;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public byte[] fromCamera(Uri filePath)
    {
        try {
//            ExifInterface ei = null;
//            try {
//                ei = new ExifInterface(imageUri.getPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//
//            Bitmap photo;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            options.inSampleSize = 6;
//            photo = BitmapFactory.decodeFile(imageUri.getPath(), options);
//            switch(orientation) {
//
//
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    photo =rotateImage(photo, 90);
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    photo =rotateImage(photo, 180);
//                    break;
//
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    photo =rotateImage(photo, 270);
//                    break;
//                // etc.
//            }
//
//
//            Bitmap scaled = Bitmap.createScaledBitmap(photo, 350, 350, false);
//
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG, 75, bao);
//            byte[] ba = bao.toByteArray();

            Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath), null, null);
            final int sizeInBytes = image.getByteCount();

            Bitmap scaled = Bitmap.createScaledBitmap(image, 350, 350, false);


            Bitmap scaled1= ExifUtils.rotateBitmap(ExifUtils.getPath(this.mContext, filePath),scaled);


            Bitmap bitmapCorr= ExifUtils.rotateBitmap(filePath.getPath(),scaled1);
            if(sizeInBytes>18000000) {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte[] ba = bao.toByteArray();
                return ba;
            }
            else if(sizeInBytes>2000000)
            {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);
                byte[] ba = bao.toByteArray();
                return ba;
            }
            else {

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapCorr.compress(Bitmap.CompressFormat.JPEG, 75, bao);

                byte[] ba = bao.toByteArray();

                return ba;
            }
        } catch(NullPointerException e)
        {
            return null;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private boolean validateData() {
        boolean tv_emailBool= tv_email.getEditText().getText().toString().contains("@") && tv_email.getEditText().getText().toString().contains(".");
        if (tv_name.getEditText().getText().toString().equals("")||tv_dob.getEditText().getText().toString().equals("") ||tv_addr.getEditText().getText().toString().equals("")||tv_phone.getEditText().getText().toString().equals("")
             ||tv_pass.getEditText().getText().toString().equals("")   || image_string.toString().equals("") ||tv_email.getEditText().getText().toString().equals("")||tv_desp.getEditText().getText().toString().equals("")||!tv_emailBool ||tv_phone.getEditText().getText().toString().length()!=10 ||tv_time_interval.getEditText().getText().toString().equals("")||tv_time_out.getEditText().getText().toString().equals(""))
        {

            if (tv_name.getEditText().getText().toString().equals(""))
            {
                tv_name.setFocusable(true);

                tv_name.setError("Invalid Name");
            }
            else {
                tv_name.setError(null);
            }

            if (tv_pass.getEditText().getText().toString().equals(""))
            {
                tv_pass.setFocusable(true);

                tv_pass.setError("Invalid Password");
            }
            else {
                tv_pass.setError(null);
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

            if (tv_phone.getEditText().getText().toString().equals("") ||tv_phone.getEditText().getText().toString().length()!=10) {
                tv_phone.setFocusable(true);
                tv_phone.setError("Invalid phone");
            }
            else {
                tv_phone.setError(null);

            }


            if (image_string.toString().equals("")) {
                iv_target.setFocusable(true);
                Toast.makeText(mContext,R.string.no_image,Toast.LENGTH_SHORT).show();
            }
            else {


            }



            if (tv_email.getEditText().getText().toString().equals("")) {
                tv_email.setFocusable(true);

                tv_email.setError("Invalid email");
            }
            else {
                tv_email.setError(null);

            }

            if (tv_desp.getEditText().getText().toString().equals("")) {
                tv_desp.setError("Invalid Description");
                tv_desp.setFocusable(true);
            }
            else {
                tv_desp.setError(null);

            }

            if (tv_time_out.getEditText().getText().toString().equals("")) {
                tv_time_out.setFocusable(true);
                tv_time_out.setError("Invalid Time out");
            }
            else {
                tv_time_out.setError(null);

            }

            if (tv_time_interval.getEditText().getText().toString().equals("")) {
                tv_time_interval.setError("Invalid Time Interval");
                tv_time_interval.setFocusable(true);
            }
            else {
                tv_time_interval.setError(null);
            }

            if (!tv_emailBool) {
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
//        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        tv_tracker_id.setText(""+pin_string);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
