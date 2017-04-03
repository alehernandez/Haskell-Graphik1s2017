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
public class VariableG implements Cloneable {

    private String nombre;
    private String tipo;
    private Object valor;
    private String visibilidad;
    private ArrayList valores;
    private ArrayList<Integer> tamanios;
    private int tamanioArreglo;
    private Nodo dimensiones;

    public VariableG() {
    }

    public VariableG(String nombre, String tipo, Object valor) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = "publico";
    }

    public VariableG(String nombre, String tipo, Object valor, String visibilidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = visibilidad;
    }

    public VariableG(String nombre, String tipo, Object valor, int tamanio, ArrayList<Integer> tamanios) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = "publico";
        this.valores = new ArrayList(tamanio);
        this.tamanios = tamanios;
        this.tamanioArreglo = tamanio;
    }

    public VariableG(String nombre, String tipo, Object valor, String visibilidad, int tamanio, ArrayList<Integer> tamanios) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.visibilidad = visibilidad;
        this.valores = new ArrayList(tamanio);
        this.tamanios = tamanios;
        this.tamanioArreglo = tamanio;
    }

    public VariableG(VariableG var) {
        this.nombre = var.getNombre();
        this.tipo = var.getTipo();
        this.valor = var.getValor();
        this.visibilidad = var.getVisibilidad();
        this.valores = var.getValores();
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

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public ArrayList getValores() {
        return valores;
    }

    public void setValores(ArrayList valores) {
        this.valores = valores;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public ArrayList<Integer> getTamanios() {
        return tamanios;
    }

    public void setTamanios(ArrayList<Integer> tamanios) {
        this.tamanios = tamanios;
    }

    public int getTamanioArreglo() {
        return tamanioArreglo;
    }

    public void setTamanioArreglo(int tamanioArreglo) {
        this.tamanioArreglo = tamanioArreglo;
    }

    public Nodo getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(Nodo dimensiones) {
        this.dimensiones = dimensiones;
    }

    public void inicializarArray(int tamanio) {
        this.valores = new ArrayList(tamanio);

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
