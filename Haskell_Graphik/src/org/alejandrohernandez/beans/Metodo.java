/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.beans;

import java.util.ArrayList;

/**
 *
 * @author oscar
 */
public class Metodo {
    private String nombre;
    private String tipo;
    private ArrayList<Variable> listaParametros;
    private Nodo instrucciones;

    public Metodo() {
    }

    public Metodo(String nombre, String tipo, ArrayList<Variable> listaParametros, Nodo instrucciones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.listaParametros = listaParametros;
        this.instrucciones = instrucciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Variable> getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(ArrayList<Variable> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public Nodo getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(Nodo instrucciones) {
        this.instrucciones = instrucciones;
    }
    
    
}
