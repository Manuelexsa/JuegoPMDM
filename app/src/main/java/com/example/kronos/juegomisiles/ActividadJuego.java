package com.example.kronos.juegomisiles;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class ActividadJuego extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.actividad_juego);
        VistaJuego vj = new VistaJuego(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.vista_juego);
        vj.setZOrderOnTop(true);
        SurfaceHolder sfhTrack = vj.getHolder();
        sfhTrack.setFormat(PixelFormat.TRANSPARENT);
        ll.addView(vj);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}