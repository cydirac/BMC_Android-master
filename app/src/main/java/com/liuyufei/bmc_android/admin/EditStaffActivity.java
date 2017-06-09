package com.liuyufei.bmc_android.admin;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.data.BMCQueryHandler;
import com.liuyufei.bmc_android.databinding.ActivityEditStaffBinding;
import com.liuyufei.bmc_android.model.Staff;
import com.squareup.picasso.Picasso;

import java.io.File;


public class EditStaffActivity extends AppCompatActivity {

    Staff staff;

    int RESULT_LOAD_IMAGE = 1;
    int REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
        if(staff==null) staff = new Staff(0,"name", "no photo", "department", "title", "mobile");
        ActivityEditStaffBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_edit_staff);

        binding.setStaff(staff);

        ImageView imageView = (ImageView) findViewById(R.id.staffProfile);

        Picasso.with(this)
                .load(new File(staff.photo.get()))
                .error(R.drawable.staff)
                .resize(200, 200).centerCrop() //for performance,downsize the pic
                .into(imageView);

        Button button = (Button) findViewById(R.id.action_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveOrUpdateTODO()){
                    Intent goBack = new Intent(EditStaffActivity.this,AdminActivity.class);
                    startActivity(goBack);
                }else{
                    new AlertDialog.Builder(EditStaffActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Cannot Save or Update the Staff")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            }).show();
                }
            }
        });

        findViewById(R.id.staffProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(EditStaffActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditStaffActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                    return;
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            } else {
                // User refused to grant permission.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.staffProfile);

            Picasso.with(this)
                    .load(new File(picturePath))
                    .error(R.drawable.staff)
                    .resize(200, 200).centerCrop() //for performance,downsize the pic
                    .into(imageView);
            staff.photo.set(picturePath);
        }
    }

    private boolean saveOrUpdateTODO() {
        String [] args = new String[1];
        BMCQueryHandler handler = new BMCQueryHandler(getContentResolver());
        ContentValues values = new ContentValues();
        values.put(BMCContract.StaffEntry.COLUMN_NAME, staff.name.get());
        values.put(BMCContract.StaffEntry.COLUMN_PHOTO, staff.photo.get());
        values.put(BMCContract.StaffEntry.COLUMN_TITLE, staff.title.get());
        values.put(BMCContract.StaffEntry.COLUMN_MOBILE, staff.mobile.get());
        values.put(BMCContract.StaffEntry.COLUMN_DEPARTMENT, staff.department.get());

        if(staff!=null&&staff.Id.get()!=0){
            args[0] = String.valueOf(staff.Id.get());
            handler.startUpdate(1, null, BMCContract.StaffEntry.CONTENT_URI, values,
                    BMCContract.StaffEntry._ID+"=?",args);
        }else{
            //save new
            handler.startInsert(1,null,BMCContract.StaffEntry.CONTENT_URI, values);
        }

        return true;
    }
}
