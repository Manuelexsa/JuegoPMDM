package com.example.kronos.juegomisiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kronos on 03/03/2015.
 */
public class VistaJuego extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bmpMisil;
    private int alto, ancho;
    private HebraJuego hebraJuego;
    private AddAtaque hAtaque;
    private ArrayList<Misil> misiles;

    private int vidas = 7;
    private int puntuacion;
    boolean seguir;
    private Misil misil;
    private AlertDialog alerta;
    private Context contexto;

    /* ************************************************************************* */
    /* *********** Constructor VistaJuego ****************************************** */
    /* ************************************************************************* */
    public VistaJuego(Context context) {
        super(context);
        getHolder().addCallback(this);
        bmpMisil = BitmapFactory.decodeResource(getResources(), R.drawable.misil);
        hAtaque  = new AddAtaque(this);
        hebraJuego = new HebraJuego(this);
        misiles = new ArrayList<Misil>();
        contexto = context;
        puntuacion = 0;
    }

    /* ************************************************************************* */
    /* *********** Metodo Draw ****************************************** */
    /* ************************************************************************* */

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (seguir) {
            pintarMisiles(canvas);
        }
        if (vidas == 0){
            mostrarPuntuacion();
        }
    }

    /* ************************************************************************* */
    /* *********** Metodos surfaceview ****************************************** */
    /* ************************************************************************* */

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        hebraJuego.setFuncionando(true);
       // hAtaque.setFuncionando(true);
       // hAtaque.start();
        seguir = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
        crearMisil();
       misiles.add(crearMisil());

        hebraJuego.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reintentar = true;
        hebraJuego.setFuncionando(false);
        hAtaque.setFuncionando(false);
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }
    }

    /* ************************************************************************* */
    /* *********** Eventos de dedo ****************************************** */
    /* ************************************************************************* */

    private long ultimoClick = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - ultimoClick > 300) {
            ultimoClick = System.currentTimeMillis();
            float x, y;
            x = event.getX();
            y = event.getY();
            synchronized (getHolder()) {
                for (final Misil m : misiles) {
                    if (m.tocado(x, y)) {
                        m.parar(x, y);
                        m.cambiarMosca(contexto);
                        puntuacion = puntuacion + 10;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                misiles.remove(m);
                            };
                        }, 1000);
                        break;
                    }
                }

            }
        }
        return true;
    }


    /* ************************************************************************* */
    /* *********** Metodos Usados ****************************************** */
    /* ************************************************************************* */

    private Misil crearMisil(){
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.misil);
        bmpMisil = Bitmap.createScaledBitmap(b, ancho * 5 / 100, ancho * 5 / 100, false);
        misil = new Misil(bmpMisil, ancho, alto);
        return misil;
    }

   /* public void addAtaque() {//Anadir misiles cada tiempo
        for (int i = 0; i < 6; i++) {
            misiles.add(new Misil(bmpMisil));
        }
    }*/

    private void pintarMisiles(Canvas canvas){
       /* for (Misil m : misiles) {
            m.dibujar(canvas);
        }*/
        if (misil != null) {
            seguir = misil.dibujar(canvas);
            if (!seguir) {
                mostrarPuntuacion();
            }
        }
    }

    private void mostrarPuntuacion() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
        alert.setTitle(contexto.getString(R.string.puntuacion));
        LayoutInflater inflater = LayoutInflater.from(contexto);
        final View vista = inflater.inflate(R.layout.puntuacion_dialogo, null);
        alert.setView(vista);
        alert.setCancelable(false);
        TextView texto = (TextView) vista.findViewById(R.id.tvPuntuacion);
        texto.setText(puntuacion + "");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent();
                i.putExtra(contexto.getString(R.string.puntuacion), puntuacion);
                ((Activity) contexto).setResult(Activity.RESULT_OK, i);
                ((Activity) contexto).finish();
            }
        });
        ((Activity) contexto).runOnUiThread(new Runnable() {
            public void run() {
                alerta = alert.create();
                alerta.show();
            }
        });
    }

}
