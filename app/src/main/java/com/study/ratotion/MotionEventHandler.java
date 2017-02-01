package com.study.ratotion;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/5/2 0002.
 */
public class MotionEventHandler {
    public static void HandleMotionEvent(MotionEvent event,String className,String method){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(className,String.format("class:[%1s] action:[ACTION_DOWN] method[%2s]",className,method));
                break;
            case MotionEvent.ACTION_UP:
                Log.d(className,String.format("class:[%1s] action:[ACTION_UP] method[%2s]",className,method));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(className,String.format("class:[%1s] action:[ACTION_MOVE] method[%2s]",className,method));
                break;
        }
    }
}
