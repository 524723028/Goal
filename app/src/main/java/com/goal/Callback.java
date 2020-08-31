package com.goal;

import android.view.View;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface Callback {
    void Enter_next(int position, boolean want_minus_times_of_item);
    void explotion (int position, View view);


}
