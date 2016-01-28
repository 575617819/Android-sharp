package com.nicodelee.base;

import android.app.Application;
import com.nicodelee.common.util.DevicesUtil;

public class APP extends Application {

  @Override public void onCreate() {
    super.onCreate();
    DevicesUtil.getScreenConfig(this);
  }


}
