package com.local.render.composition;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;

public class GLTestActivity extends Activity {
    Surface mySurface;
    Handler mHandler = new Handler();
    VirtualDisplay virtualDisplay;
    GLStreoView glStreoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        glStreoView = new GLStreoView(this, mSurfaceHandleCallback);
        setContentView(glStreoView);

        L.i("onCreate completed");

    }

    SurfaceHandleCallback mSurfaceHandleCallback = new SurfaceHandleCallback() {
        @Override
        public void onSurfaceCreated(Surface surface) {
            L.i("mSurfaceHandleCallback onSurfaceCreated valid " + surface.isValid());
            mySurface = surface;
           mHandler.postDelayed(runnable, 1000);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            L.i("runnable surface valid " + mySurface.isValid());

            DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            virtualDisplay = displayManager.createVirtualDisplay("com.local.composition", 1280,1280,240,mySurface , DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC | DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY);
            Presentation presentation = new Presentation(GLTestActivity.this.getApplicationContext(), virtualDisplay.getDisplay());
            presentation.setContentView(R.layout.activity_main);
            presentation.show();
            mHandler.postDelayed(runnable2, 50);
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            glStreoView.requestRender();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            L.i("finish");
            finish();

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

