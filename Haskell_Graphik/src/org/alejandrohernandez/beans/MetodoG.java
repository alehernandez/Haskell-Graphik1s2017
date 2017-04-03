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
public class MetodoG implements Cloneable{
      private String nombre;
    private String tipo;
    private ArrayList<VariableG> listaParametros;
    private String visibilidad;
    private Nodo instrucciones;

    public MetodoG() {
    }

    public MetodoG(String nombre, String tipo, ArrayList<VariableG> listaParametros, Nodo instrucciones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.listaParametros = listaParametros;
        this.instrucciones = instrucciones;
        this.visibilidad = "publico";
    }

    public MetodoG(String nombre, String tipo, ArrayList<VariableG> listaParametros, String visibilidad, Nodo instrucciones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.listaParametros = listaParametros;
        this.visibilidad = visibilidad;
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

    public ArrayList<VariableG> getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(ArrayList<VariableG> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public Nodo getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(Nodo instrucciones) {
        this.instrucciones = instrucciones;
    }
    @Override
     public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}
