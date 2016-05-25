package com.sage.hedonicmentality.utils;

import com.sage.hedonicmentality.bean.HRVData;

/**
 * Created by Sage on 2015/8/11.
 */
public interface AsyncTaskListener {
     void onTaskComplete(HRVData data);
}
