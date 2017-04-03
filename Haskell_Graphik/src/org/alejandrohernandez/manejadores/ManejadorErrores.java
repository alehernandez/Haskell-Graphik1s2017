/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.ArrayList;
import org.alejandrohernandez.beans.Errores;
/**
 *
 * @author oscar
 */
public class ManejadorErrores {

    private static ManejadorErrores instancia;
    private ArrayList<Errores> listaErroresHaskell;
    private ArrayList<Errores> listaErroresGraphik;

    public static ManejadorErrores getInstancia() {
        instanciar();
        return instancia;
    }

    private static synchronized void instanciar() {
        if (instancia == null) {
            instancia = new ManejadorErrores();
        }
    }
    private ManejadorErrores(){
        listaErroresGraphik = new ArrayList<Errores>();
        listaErroresHaskell = new ArrayList<>();
    }
    public void agregarErrorHaskell(Errores nuevoError){
        listaErroresHaskell.add(nuevoError);
    }
    public void agregarErrorGraphik(Errores errorGraphik){
        listaErroresGraphik.add(errorGraphik);
    }
    public ArrayList<Errores> getListaErroresHaskell() {
        return listaErroresHaskell;
    }

    public void setListaErroresHaskell(ArrayList<Errores> listaErroresHaskell) {
        this.listaErroresHaskell = listaErroresHaskell;
    }

    public ArrayList<Errores> getListaErroresGraphik() {
        return listaErroresGraphik;
    }

    public void setListaErroresGraphik(ArrayList<Errores> listaErroresGraphik) {
        this.listaErroresGraphik = listaErroresGraphik;
    }
    public void limpiarInstancia(){
        instancia =null;
    }
}
