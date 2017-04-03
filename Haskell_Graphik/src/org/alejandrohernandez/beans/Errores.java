/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.beans;

/**
 *
 * @author oscar
 */
public class Errores {
    private String descripcion;
    private String tipo;
    private int columna;
    private int fila;
    private String archivo;

    public Errores() {
    }

    public Errores(String descripcion, String tipo, int columna, int fila, String archivo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.columna = columna;
        this.fila = fila;
        this.archivo = archivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    
}
