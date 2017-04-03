/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author oscar
 */
public class Als implements Cloneable {

    private String nombre;
    private String visibilidad;
    private ArrayList<MetodoG> listaMetodos;
    private LinkedHashMap<String, VariableG> listaAtributos;
    private Nodo cuerpoAls;
    private String nombrePadre;//nombre del id que le heredan
    private boolean seHizoHerencia;
    private Nodo principal;
    private Nodo datos;

    public Als() {
    }

    public Als(String nombre, String visiblidad, Nodo cuerpoAls, String nombrePadre, boolean seHizoHerencia) {
        this.nombre = nombre;
        this.visibilidad = visiblidad;
        this.cuerpoAls = cuerpoAls;
        this.nombrePadre = nombrePadre;
        this.seHizoHerencia = seHizoHerencia;
        listaMetodos = new ArrayList<MetodoG>();
        listaAtributos = new LinkedHashMap<String, VariableG>();
    }

    public Als(Als als) {
        this.nombre = als.getNombre();
        this.visibilidad = als.getVisibilidad();
        this.listaMetodos = als.getListaMetodos();
        this.listaAtributos = als.getListaAtributos();
        this.cuerpoAls = als.getCuerpoAls();
        this.nombrePadre = als.getNombrePadre();
        this.seHizoHerencia = als.getSeHizoHerencia();
    }

    public void inicializarArray() {

        listaAtributos = new LinkedHashMap<String, VariableG>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public ArrayList<MetodoG> getListaMetodos() {
        return listaMetodos;
    }

    public void setListaMetodos(ArrayList<MetodoG> listaMetodos) {
        this.listaMetodos = listaMetodos;
    }

    public LinkedHashMap<String, VariableG> getListaAtributos() {
        return listaAtributos;
    }

    public void setListaAtributos(LinkedHashMap<String, VariableG> listaAtributos) {
        this.listaAtributos = listaAtributos;
    }

    public Nodo getCuerpoAls() {
        return cuerpoAls;
    }

    public void setCuerpoAls(Nodo cuerpoAls) {
        this.cuerpoAls = cuerpoAls;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }

    public boolean getSeHizoHerencia() {
        return seHizoHerencia;
    }

    public void setSeHizoHerencia(boolean seHizoHerencia) {
        this.seHizoHerencia = seHizoHerencia;
    }

    public Nodo getPrincipal() {
        return principal;
    }

    public void setPrincipal(Nodo principal) {
        this.principal = principal;
    }

    public Nodo getDatos() {
        return datos;
    }

    public void setDatos(Nodo datos) {
        this.datos = datos;
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

}
