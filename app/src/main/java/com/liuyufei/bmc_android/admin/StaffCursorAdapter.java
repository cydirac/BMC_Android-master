package com.liuyufei.bmc_android.admin;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.squareup.picasso.Picasso;

import java.io.File;


public class StaffCursorAdapter extends CursorAdapter {
    public StaffCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_staff, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView staffName = (TextView) view.findViewById(R.id.staffName);
        ImageView photo = (ImageView) view.findViewById(R.id.staffImg);
        int textColumn = cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME);
        int photoColumn = cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_PHOTO);
        String text = cursor.getString(textColumn);
        String photoPath = cursor.getString(photoColumn);
        staffName.setText(text);

        Picasso.with(context)
                .load(new File(photoPath))
                .error(R.drawable.staff)
                .resize(100,100).centerCrop() //for performance,downsize the pic
                .into(photo);
    }
}
