package com.study.ratotion;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ElementContainerView ecv = (ElementContainerView)this.findViewById(R.id.containerv); //new ElementContainerView(this);

        ElementView element = new ElementView(this);
        ecv.addView(element);

        ElementView element1 = new ElementView(this);
        element1.setBitmapSource(R.drawable.icon_chenzhen);
        ecv.addView(element1);

        ElementView element2 = new ElementView(this);
        element2.setBitmapSource(R.drawable.icon_xp);
        ecv.addView(element2);

//        FrameLayout container = (FrameLayout)this.findViewById(R.id.container);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        container.addView(ecv,lp);
    }

}
