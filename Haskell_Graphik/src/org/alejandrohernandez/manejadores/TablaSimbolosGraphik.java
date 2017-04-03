/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import org.alejandrohernandez.beans.Als;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.MetodoG;
import org.alejandrohernandez.beans.VariableG;
import org.alejandrohernandez.beans.Nodo;
import org.alejandrohernandez.beans.Simbolo;

/**/
/**
 *
 * @author oscar
 */
public class TablaSimbolosGraphik {

    private static TablaSimbolosGraphik instancia;
    private ArrayList<MetodoG> listaMetodos;
    private Stack<ListaVariablesG> pilaVariables;
    private LinkedHashMap<String, VariableG> listaVariablesGlobales;
    private LinkedHashMap<String, Als> listaAls;
    private String archivo;
    private Als alsActivo;
    private Als alsPrincipal;
    private ArrayList<Simbolo> listaSimbolos;

    public static TablaSimbolosGraphik getInstancia() {
        instanciar();
        return instancia;
    }

    private static synchronized void instanciar() {
        if (instancia == null) {
            instancia = new TablaSimbolosGraphik();
        }
    }

    public TablaSimbolosGraphik() {
        //listaMetodos = new ArrayList<MetodoG>();
        pilaVariables = new Stack<ListaVariablesG>();
        // listaVariablesGlobales = new HashMap<String, VariableG>();
        listaAls = new LinkedHashMap<String, Als>();
        listaSimbolos = new ArrayList<Simbolo>();
    }

    public boolean agregarAls(Als alsNuevo) {
        if (listaAls.containsKey(alsNuevo.getNombre())) {
            return false;
        }
        listaAls.put(alsNuevo.getNombre(), alsNuevo);
        return true;
    }

    public Als buscarAls(String id) {
        if (listaAls.containsKey(id)) {
            return listaAls.get(id);
        }
        return null;
    }

    public void agregarAmbito(String ambito) {
        ListaVariablesG nuevoAmbito = new ListaVariablesG();
        if (ambito.equalsIgnoreCase("metodo")) {
            nuevoAmbito.setAmbitoGeneral("metodo");
        }
        pilaVariables.push(nuevoAmbito);
    }

    public void quitarAmbito() {
        pilaVariables.pop();
    }

    public boolean agregarVariableLocal(VariableG var) {
        boolean sePuede = true;
        for (int i = pilaVariables.size() - 1; i >= 0; i--) {
            if (pilaVariables.get(i).getListaVariables().containsKey(var.getNombre()) == true) {
                sePuede = false;
            }
            if (pilaVariables.get(i).getAmbitoGeneral().equalsIgnoreCase("metodo")) {
                break;
            }
        }
        if (sePuede) {
            pilaVariables.lastElement().getListaVariables().put(var.getNombre(), var);
            listaSimbolos.add(new Simbolo(var, "Local", "", ""));
            return true;
        }
        return false;
    }

    public VariableG buscarLocalmente(String id) {
        for (int i = pilaVariables.size() - 1; i >= 0; i--) {
            if (pilaVariables.get(i).getListaVariables().containsKey(id)) {
                return pilaVariables.get(i).getListaVariables().get(id);
            }
            if (pilaVariables.get(i).getAmbitoGeneral().equalsIgnoreCase("metodo")) {
                return null;
            }
        }
        return null;
    }

    public boolean agregarVarGlobal(VariableG varGlobal) {
        if (alsActivo.getListaAtributos().containsKey(varGlobal.getNombre()) == true) {
            return false;
        }
        //listaVariablesGlobales.put(varGlobal.getNombre(), varGlobal);
        alsActivo.getListaAtributos().put(varGlobal.getNombre(), varGlobal);
        listaSimbolos.add(new Simbolo(varGlobal, "Global", "", ""));
        return true;
    }

    public VariableG buscarGlobalmente(String id) {
        if (alsActivo.getListaAtributos().containsKey(id)) {
            return alsActivo.getListaAtributos().get(id);
        }
        return null;
    }

    public Errores agregarMetodoG(MetodoG metodoNuevo, Nodo node) {
        Boolean banderahacerSobreCarga = false;
        if (alsActivo.getListaMetodos().isEmpty())//Cuando es el primer metodo que se agregara
        {
            alsActivo.getListaMetodos().add(metodoNuevo);
            return null;
        }
        banderahacerSobreCarga = existeMetodoG(metodoNuevo);
        if (banderahacerSobreCarga) {
            //Se llama al metodo de sobrecarga de metodos, este regresa un error para saber si se agrego o no
            Errores error = hacerSobreCarga(metodoNuevo, node);
            if (error != null) {
                return error;
            }
        } else {//SINO EXISTE EL METODO SOLO SE GUARDA COMO UN NUEVO SIN HACER VALIDACIONES
            alsActivo.getListaMetodos().add(metodoNuevo);
        }

        return null;
    }

    public Errores hacerSobreCarga(MetodoG nuevoMetodoG, Nodo node) {//METODO PARA HACER LA SOBRECARGA
        int guardaMetodoGInt = 0;
        int tiposIguales = 0;
        for (MetodoG item : alsActivo.getListaMetodos()) {
            if (item.getNombre().equals(nuevoMetodoG.getNombre())) {
                if (item.getListaParametros() != null & nuevoMetodoG.getListaParametros() != null)//se verifica que los dos no tengan nula su lista de parametros
                {
                    if (item.getListaParametros().size() == nuevoMetodoG.getListaParametros().size())//si el numero de parametros es igual, se debe verificar con los parametros
                    {
                        //se va a verificar parametro por parametro, si todos los parametros son del mismo tipo se devolvera error
                        for (int i = 0; i < item.getListaParametros().size(); i++) {
                            if (item.getListaParametros().get(i).getTipo().equals(nuevoMetodoG.getListaParametros().get(i).getTipo())) {
                                tiposIguales++;//si entra, se aumenta el contador 
                            }
                        }
                        if (item.getListaParametros().size() == tiposIguales)//si el contador de tipos iguales es igual al numero de parametros, quiere decir que todos son iguales.
                        {
                            guardaMetodoGInt++;
                        }
                    }
                } else if (item.getListaParametros() == null && nuevoMetodoG.getListaParametros() == null)//si los dos no tienen parametros, el nuevo metodo es rechazado porque ya existe
                {
                    guardaMetodoGInt++;
                } else if (item.getListaParametros() != null && nuevoMetodoG.getListaParametros() == null)//si el nuevo metodo no tienen parametros y el que esta guardado si, entonces se puede
                {
                }
            }

        }
        if (guardaMetodoGInt == 0)//si la bandera es true se va guardar el metodo, de lo contrario ocurrio un error porque ya existe el metodo.
        {
            alsActivo.getListaMetodos().add(nuevoMetodoG);
        } else {
            Errores errorGraphik = new Errores("El " + node.getNombre() + " metodo ya existe", "Semantico", node.getColumna(), node.getFila(), archivo);
            ManejadorErrores.getInstancia().agregarErrorGraphik(errorGraphik);
            return errorGraphik;
        }
        return null;
    }

    public Boolean existeMetodoG(MetodoG metodoNuevo) {
        Boolean hacerSobreCarga = false;
        for (MetodoG item : alsActivo.getListaMetodos())//se busca si el metodo nuevo ya existe, para hacer la sobrecarga
        {
            if (metodoNuevo.getNombre().equals(item.getNombre())) {
                //se tiene que hacer sobrecarga 
                hacerSobreCarga = true;
                break;
            } else {
                hacerSobreCarga = false;
            }
        }
        return hacerSobreCarga;
    }

    public Object buscarMetodoG(String metodoBuscar, ArrayList<VariableG> listaParametrosEnviados, String metoPuedeLista) {
        int guardaMetodoGInt = 0;
        int tiposIguales = 0;
        MetodoG devolverMetodoG = null;
        for (MetodoG item : alsActivo.getListaMetodos()) {
            if (item.getNombre().equals(metodoBuscar)) {
                if (item.getListaParametros() != null & listaParametrosEnviados != null)//se verifica que los dos no tengan nula su lista de parametros
                {
                    if (item.getListaParametros().size() == listaParametrosEnviados.size())//si el numero de parametros es igual, se debe verificar con los parametros
                    {
                        //se va a verificar parametro por parametro, si todos los parametros son del mismo tipo se devolvera error
                        for (int i = 0; i < item.getListaParametros().size(); i++) {
                            if (listaParametrosEnviados.get(i).getNombre().equals("sin valor")) {
                                return "sin valor";
                            }
                            if (item.getListaParametros().get(i).getTipo().equals("entero") || item.getListaParametros().get(i).getTipo().equals("decimal"))//aca se empieza con la verificacion de tipos, si es number 
                            {
                                //Se verifica que venga un double o un boolean(se cambia a 1 o 0)
                                if (listaParametrosEnviados.get(i).getValor() instanceof Double) //|| listaParametrosEnviados[i].Valor.GetType().Equals(typeof(Boolean)))
                                {
                                    /* if (listaParametrosEnviados[i].Valor.GetType().Equals(typeof(Boolean)))//Si es boolean se cambia a 1 o 0 y se guarda el valor
                                         {
                                             if ((Boolean)listaParametrosEnviados[i].Valor == true)
                                             {
                                                 listaParametrosEnviados[i].Valor = (double)1;
                                             }
                                             else
                                             {
                                                 listaParametrosEnviados[i].Valor = (double)0;
                                             }
                                         }*/
                                    listaParametrosEnviados.get(i).setNombre(item.getListaParametros().get(i).getNombre()); //se debe setarle el nombre a la variable local que se va guardar
                                    listaParametrosEnviados.get(i).setTipo(item.getListaParametros().get(i).getTipo());//se debe setarle el tipo a la variable local que se va guardar cuando regrese la lista
                                    tiposIguales++;//se aumenta el contador para saber si al final si tienen los mismos parametros y si son del mismo tipo
                                } else {
                                    //ES ERROR PORQUE NO ES IGUAL EL TIPO, CON QUE NO TENGA UN TIPO IGUAL SE PARA EL FOR PARA QUE NO SIGA 
                                    break;
                                }
                            } else if (item.getListaParametros().get(i).getTipo().equals("cadena"))//el string aguanta con todo, solo se tiene que pasar a string si es un DOuble o un boolean
                            {
                                if (listaParametrosEnviados.get(i).getValor() instanceof Boolean || listaParametrosEnviados.get(i).getValor() instanceof Double)//Si es boolean se cambia a 1 o 0 y se guarda el valor
                                {
                                    if ((Boolean) listaParametrosEnviados.get(i).getValor() == true) {

                                        listaParametrosEnviados.get(i).setValor("1");
                                    } else {
                                        listaParametrosEnviados.get(i).setValor("0");
                                    }
                                    break;
                                } else {
                                    listaParametrosEnviados.get(i).setNombre(item.getListaParametros().get(i).getNombre()); //se debe setarle el nombre a la variable local que se va guardar
                                    listaParametrosEnviados.get(i).setTipo(item.getListaParametros().get(i).getTipo());//se debe setarle el tipo a la variable local que se va guardar cuando regrese la lista
                                    listaParametrosEnviados.get(i).setValor(item.getListaParametros().get(i).getValor().toString());

                                    tiposIguales++;
                                }

                            } else if (item.getListaParametros().get(i).getTipo().equals("bool")) {
                                if (listaParametrosEnviados.get(i).getValor() instanceof Double || listaParametrosEnviados.get(i).getValor() instanceof String) {

                                    //ES ERROR PORQUE NO ES IGUAL EL TIPO, CON QUE NO TENGA UN TIPO IGUAL SE PARA EL FOR PARA QUE NO SIGA 
                                    break;
                                } else {
                                    listaParametrosEnviados.get(i).setNombre(item.getListaParametros().get(i).getNombre()); //se debe setarle el nombre a la variable local que se va guardar
                                    listaParametrosEnviados.get(i).setTipo(item.getListaParametros().get(i).getTipo());//se debe setarle el tipo a la variable local que se va guardar cuando regrese la lista
                                    tiposIguales++;

                                }
                            }
                        }
                        if (item.getListaParametros().size() == tiposIguales)//si el contador de tipos iguales es igual al numero de parametros, quiere decir que todos son iguales.
                        {
                            if (metoPuedeLista.equals("metodo"))//si es metodo solo debo de enviar el metodo, para ejecutar sus instruccions
                            {
                                return item;//regreso el metodo
                            } else//sino es porque cambie los parametros, por si venian number = boolean etc..
                            {
                                return listaParametrosEnviados;
                            }
                        }
                    }
                } else if (item.getListaParametros() == null && listaParametrosEnviados == null)//si los dos no tienen parametros, solo se regresa el metodo
                {
                    return item;
                }
            }

        }
        if (guardaMetodoGInt == 1)//si la bandera es true se va guardar el metodo, de lo contrario ocurrio un error porque ya existe el metodo.
        {
            return devolverMetodoG;
        } else {
            Errores nuevoError = new Errores("No existe el metodo " + metodoBuscar, "Semantico", 0, 0, archivo);
            return nuevoError;//se debe guardar el error cuando regresa a ejecuccion
        }
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void limpiarInstancia() {
        instancia = null;
    }

    public Stack<ListaVariablesG> getPilaVariables() {
        return pilaVariables;
    }

    public void setPilaVariables(Stack<ListaVariablesG> pilaVariables) {
        this.pilaVariables = pilaVariables;
    }

    public LinkedHashMap<String, VariableG> getListaVariablesGlobales() {
        return listaVariablesGlobales;
    }

    public void setListaVariablesGlobales(LinkedHashMap<String, VariableG> listaVariablesGlobales) {
        this.listaVariablesGlobales = listaVariablesGlobales;
    }

    public LinkedHashMap<String, Als> getListaAls() {
        return listaAls;
    }

    public void setListaAls(LinkedHashMap<String, Als> listaAls) {
        this.listaAls = listaAls;
    }

    

    public Als getAlsActivo() {
        return alsActivo;
    }

    public void setAlsActivo(Als alsActivo) {
        this.alsActivo = alsActivo;
    }

    public Als getAlsPrincipal() {
        return alsPrincipal;
    }

    public void setAlsPrincipal(Als alsPrincipal) {
        this.alsPrincipal = alsPrincipal;
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public void setListaSimbolos(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
    }

    public ArrayList<MetodoG> getListaMetodos() {
        return listaMetodos;
    }

    public void setListaMetodos(ArrayList<MetodoG> listaMetodos) {
        this.listaMetodos = listaMetodos;
    }

}
