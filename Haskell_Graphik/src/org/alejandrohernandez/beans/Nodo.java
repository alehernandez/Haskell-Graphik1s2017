/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.beans;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oscar
 */
public class Nodo {

    public String nombre; //nombre del no terminal o token
    public String valor; // valor de los no terminales o tokens que va tomando al sintetizar
    public int numeroHijos; // numero de hijos del nodo
    public Nodo padre; // padre de un nodo
    public Nodo hijos[]; // arreglo de hijos de tipo nodos
    public ArrayList<Nodo> hijos2;
    public String tipo; // tipo, para numeros, cadenas o caracters
    public int columna=0; //numero para saber en que columna esta
    public int fila=0;//numero para saber en que fila esta

    public Nodo() {
        hijos2= new ArrayList<Nodo>();
    }

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.valor = nombre;
        this.tipo = nombre;
        this.numeroHijos = 0;
         hijos2= new ArrayList<Nodo>();
    }

    public Nodo(String nombre, int columna, int fila) {
        this.nombre = nombre;
        this.valor = nombre;
        this.tipo = nombre;
        this.numeroHijos = 0;
        this.columna = columna + 1;
        this.fila = fila + 1;
         hijos2= new ArrayList<Nodo>();
    }

    public Nodo(String nombre, String tipo, int columna, int fila) {
        this.nombre = nombre;
        this.valor = nombre;
        this.tipo = tipo;
        this.numeroHijos = 0;
        this.columna = columna + 1;
        this.fila = fila + 1;
         hijos2= new ArrayList<Nodo>();
    }

    public void insertarNodo(Nodo nuevo) {
        if (nuevo == null) {
            return;
        }
//        Nodo auxHijos[] = new Nodo[this.numeroHijos +1];
//        for (int i = 0; i < this.numeroHijos; i++) {
//            auxHijos[i] =this.hijos[i];
//            auxHijos[i].padre = this;
//        }
//        nuevo.padre = this;
//        auxHijos[this.numeroHijos] = nuevo;
//        
//        this.hijos = auxHijos;
        nuevo.padre = this;
        this.hijos2.add(nuevo);
        this.numeroHijos++;
    }
    
    public void graficarAst(Nodo nodo,String nombre) throws IOException {
        
        String cadena = llenarAst(nodo);
        cadena = cadena.replace("\"\"", "\"");

        cadena = "digraph G{" + cadena + "}";
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombre+".dot");
            pw = new PrintWriter(fichero);
            pw.write(cadena);
        } catch (Exception g) {

        }
        fichero.close();
        if(nombre.equals("HASKELL")){
        File f = new File("comando.cmd");
        Desktop.getDesktop().open(f);
        }else{
        File f = new File("comandoGk.cmd");
        Desktop.getDesktop().open(f);
        }
        
        

    }

    public String llenarAst(Nodo nodo) {
        String cadena = System.identityHashCode(nodo) + "[label=\"" + nodo.nombre + "\"]" + "\n";
        for (int i = 0; i < nodo.numeroHijos; i++) {
            cadena = cadena + llenarAst(nodo.hijos2.get(i));
        }
        for (int i = 0; i < nodo.numeroHijos; i++) {
            cadena = cadena + System.identityHashCode(nodo) + "->" + System.identityHashCode(nodo.hijos2.get(i)) + "\n";
        }
        return cadena;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getNumeroHijos() {
        return numeroHijos;
    }

    public void setNumeroHijos(int numeroHijos) {
        this.numeroHijos = numeroHijos;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public ArrayList<Nodo> getHijos2() {
        return hijos2;
    }

    public void setHijos2(ArrayList<Nodo> hijos2) {
        this.hijos2 = hijos2;
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
    
}
