package com.example.kronos.juegomisiles;

/**
 * Created by kronos on 03/03/2015.
 */
public class AddAtaque extends Thread {
    private VistaJuego vista;
    private boolean funcionando = false;

    public AddAtaque(VistaJuego vj) {
        this.vista = vj;
    }

    public void setFuncionando(boolean b) {
        funcionando = b;
    }

    @Override
    public void run() {
        /*while (funcionando){
            vista.addAtaque();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
