package com.liuyufei.bmc_android.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;


public class BMCQueryHandler extends AsyncQueryHandler {
    public BMCQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
