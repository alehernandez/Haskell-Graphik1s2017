/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.ArrayList;
import java.util.Stack;
import org.alejandrohernandez.beans.Metodo;
import org.alejandrohernandez.beans.Variable;

/**
 *
 * @author oscar
 */
public class TablaSimbolosHaskell {

    private static TablaSimbolosHaskell instancia;
    private ArrayList<Metodo> listaMetodos;
    private Stack<ListaVariables> pilaVariables;
    private ArrayList<Variable> listaVariablesGlobales;
    private String archivo;
    public static TablaSimbolosHaskell getInstancia() {
        instanciar();
        return instancia;
    }

    private static synchronized void instanciar() {
        if (instancia == null) {
            instancia = new TablaSimbolosHaskell();
        }
    }

    public TablaSimbolosHaskell() {
        listaMetodos = new ArrayList<Metodo>();
        pilaVariables = new Stack<ListaVariables>();
        listaVariablesGlobales = new ArrayList<Variable>();
    }

    public void agregarAmbito(String ambito) {
        ListaVariables nuevoAmbito = new ListaVariables();
        if (ambito.equalsIgnoreCase("metodo")) {
            nuevoAmbito.setAmbitoGeneral("metodo");
        }
        pilaVariables.push(nuevoAmbito);
    }

    public void quitarAmbito() {
        pilaVariables.pop();
    }

    public boolean agregarVariableLocal(Variable var) {
        boolean sePuede = true;
        for (int i = pilaVariables.size() -1; i>=0 ;i--) {
            if (pilaVariables.get(i).getListaVariables().containsKey(var.getNombre()) == true) {
                sePuede = false;
            }
            if (pilaVariables.get(i).getAmbitoGeneral().equalsIgnoreCase("metodo")) {
                break;
            }
        }
        if (sePuede) {
            pilaVariables.lastElement().getListaVariables().put(var.getNombre(), var);
            return true;
        }
        return false;
    }

    public Variable buscarLocalmente(String id) {
        for (int i = pilaVariables.size() -1; i>=0 ;i--) {
            if (pilaVariables.get(i).getListaVariables().containsKey(id)) {
                return pilaVariables.get(i).getListaVariables().get(id);
            }
            if (pilaVariables.get(i).getAmbitoGeneral().equalsIgnoreCase("metodo")) {
                return null;    
            }
        }
        return null;
    }

    public boolean agregarVarGlobal(Variable varGlobal) {
        for (Variable listaVariablesGlobale : listaVariablesGlobales) {
            if (listaVariablesGlobale.getNombre().equalsIgnoreCase(varGlobal.getNombre())) {
                return false;
            }
        }
        listaVariablesGlobales.add(varGlobal);
        return true;
    }

    public Variable buscarGlobalmente(String id) {
        for (Variable listaVariablesGlobale : listaVariablesGlobales) {
            if (listaVariablesGlobale.getNombre().equalsIgnoreCase(id)) {
                return listaVariablesGlobale;
            }
        }
        return null;
    }

    public boolean agregarMetodo(Metodo metodoNuevo) {
        for (Metodo listaMetodo : listaMetodos) {
            if (listaMetodo.getNombre().equals(metodoNuevo.getNombre())) {
                return false;
            }
        }
        listaMetodos.add(metodoNuevo);
        return true;
    }

    public Metodo buscarMetodo(String id, ArrayList listaParametrosEnviados) {
        for (Metodo listaMetodo : listaMetodos) {
            if (listaMetodo.getNombre().equalsIgnoreCase(id)) {
                if (listaMetodo.getListaParametros()==null && listaParametrosEnviados ==null) {
                    
                }else if(listaParametrosEnviados!=null && listaMetodo.getListaParametros()!=null){
                    if (listaParametrosEnviados.size() == listaMetodo.getListaParametros().size()) {
                        return listaMetodo;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Metodo> getListaMetodos() {
        return listaMetodos;
    }

    public void setListaMetodos(ArrayList<Metodo> listaMetodos) {
        this.listaMetodos = listaMetodos;
    }

    public Stack<ListaVariables> getPilaVariables() {
        return pilaVariables;
    }

    public void setPilaVariables(Stack<ListaVariables> pilaVariables) {
        this.pilaVariables = pilaVariables;
    }

    public ArrayList<Variable> getListaVariablesGlobales() {
        return listaVariablesGlobales;
    }

    public void setListaVariablesGlobales(ArrayList<Variable> listaVariablesGlobales) {
        this.listaVariablesGlobales = listaVariablesGlobales;
    }
    
    public void limpiarInstancia() {
        instancia = null;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    
}
