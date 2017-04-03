/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.HashMap;
import java.util.Map;
import org.alejandrohernandez.beans.Variable;

/**
 *
 * @author oscar
 */
public class ListaVariables {
    private Map<String,Variable> listaVariables;
    private String ambitoGeneral;
    public ListaVariables() {
        listaVariables  = new HashMap<String, Variable>();
        ambitoGeneral = "normal";
    }

    public Map<String, Variable> getListaVariables() {
        return listaVariables;
    }

    public ListaVariables(Map<String, Variable> listaVariables) {
        this.listaVariables = listaVariables;
    }
    
    public void setListaVariables(Map<String, Variable> listaVariables) {
        this.listaVariables = listaVariables;
    }

    public String getAmbitoGeneral() {
        return ambitoGeneral;
    }

    public void setAmbitoGeneral(String ambitoGeneral) {
        this.ambitoGeneral = ambitoGeneral;
    }
    
}
