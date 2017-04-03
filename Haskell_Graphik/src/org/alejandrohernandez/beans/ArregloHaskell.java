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
public class ArregloHaskell{
    private int numeroDimension;
    private ArrayList<Object> dimension1;
    private String tipo;
    public ArregloHaskell() {
        dimension1 = new ArrayList<Object>();
    }

    public ArregloHaskell(int dimension,String tipo) {
        this.numeroDimension = dimension;
       dimension1 = new ArrayList<Object>();
        this.tipo = tipo;
    }

    public int getDimension() {
        return numeroDimension;
    }

    public void setDimension(int dimension) {
        this.numeroDimension = dimension;
    }

    public ArrayList<Object> getDimension1() {
        return dimension1;
    }

    public void setDimension1(ArrayList<Object> dimension1) {
        this.dimension1 = dimension1;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
  

   

    
}
