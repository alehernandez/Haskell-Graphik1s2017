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
public class Simbolo {
    private VariableG varG;
    private String ambito;
    private String rol;
    private String dimensiones;

    public Simbolo() {
    }

    public Simbolo(VariableG varG, String ambito, String rol, String dimensiones) {
        this.varG = varG;
        this.ambito = ambito;
        this.rol = rol;
        this.dimensiones = dimensiones;
    }

    public VariableG getVarG() {
        return varG;
    }

    public void setVarG(VariableG varG) {
        this.varG = varG;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }
    
}
