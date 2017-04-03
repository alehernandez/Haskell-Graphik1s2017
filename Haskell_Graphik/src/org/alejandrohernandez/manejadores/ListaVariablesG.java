/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.HashMap;
import java.util.Map;
import org.alejandrohernandez.beans.VariableG;

/**
 *
 * @author oscar
 */
public class ListaVariablesG {
     private Map<String,VariableG> listaVariables;
    private String ambitoGeneral;
    public ListaVariablesG() {
            listaVariables  = new HashMap<String, VariableG>();
        ambitoGeneral = "normal";
    }

    public Map<String, VariableG> getListaVariables() {
        return listaVariables;
    }

    public ListaVariablesG(Map<String, VariableG> listaVariables) {
        this.listaVariables = listaVariables;
    }
    
    public void setListaVariables(Map<String, VariableG> listaVariables) {
        this.listaVariables = listaVariables;
    }

    public String getAmbitoGeneral() {
        return ambitoGeneral;
    }

    public void setAmbitoGeneral(String ambitoGeneral) {
        this.ambitoGeneral = ambitoGeneral;
    }
}
