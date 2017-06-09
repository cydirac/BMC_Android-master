package com.liuyufei.bmc_android.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;

public class Staff implements Serializable {
    public final ObservableInt Id = new ObservableInt();
    public final ObservableField<String> name = new ObservableField<String>();
    public final ObservableField<String> photo = new ObservableField<String>();
    public final ObservableField<String> department = new ObservableField<String>();
    public final ObservableField<String> title = new ObservableField<String>();
    public final ObservableField<String> mobile = new ObservableField<String>();

    public Staff(int id, String name, String photo, String department, String title, String mobile) {
        this.Id.set(id);
        this.name.set(name);
        this.photo.set(photo);
        this.department.set(department);
        this.title.set(title);
        this.mobile.set(mobile);
    }

}

