package com.example.kronos.juegomisiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by kronos on 03/03/2015.
 */
public class Misil {

    private Bitmap bmp;
    private int ancho, alto;
    private int ejeY = 0;
    private int direccionY;
    private int ejeX = 0;
    private int direccionX;
    private float ejeXFijo = 0.0f, ejeYFijo = 0.0f;
    private static int anchoMax=0, altoMax=0;


    /* ************************************************************************* */
    /* *********** Constructor misil ****************************************** */
    /* ************************************************************************* */
    public Misil(Bitmap bmp, int ancho, int alto) {
        this.bmp = bmp;
        this.ancho = bmp.getWidth();
        this.alto = bmp.getHeight();
        Random rnd = new Random();
        this.anchoMax = ancho;
        this.altoMax = alto;
        ejeX = ancho + rnd.nextInt(anchoMax  - ancho/2);
        ejeY = this.altoMax / 2;
        direccionX = 1;
        direccionY = 5;
    }

     /* ************************************************************************* */
    /* *********** Dibujar el misil ******************************************* */
    /* ************************************************************************* */

    public boolean dibujar(Canvas canvas) {
        boolean seguir = movimiento();
        canvas.drawBitmap(bmp, ejeX, ejeY, null);
        return seguir;
    }

    /* ************************************************************************* */
    /* *********** Mover el misil ********************************************* */
    /* ************************************************************************* */

    private boolean movimiento() {
        if (ejeX > anchoMax - ancho - direccionX ||
                ejeX + direccionX < 0) {
            direccionX = -direccionX;
        }
        ejeX = ejeX + direccionX;
        if (ejeY + direccionY < altoMax * 6 / 100) {
            direccionY = -direccionY;
        } else if (ejeY > altoMax - alto - direccionY) {
            return false;
        }
        ejeY = ejeY + direccionY;
        return true;
    }

    /* ************************************************************************* */
    /* *********** evento del misil ********************************************* */
    /* ************************************************************************* */

    public boolean tocado(float x, float y){
        return x > ejeX && x < ejeX + ancho && y > ejeY && y < ejeY + alto;
    }

    //Detiene el elemento, en la posicion que lo hemos tocado
    //para mostrar la explosion

    public void parar(float x, float y){
        direccionX=0;
        direccionY=0;
        ejeXFijo=x;
        ejeYFijo=y;
    }

    //Cambia la imagen del misil por una explosion
    public void cambiarMosca(Context context){
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
    }

}

