/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.util.ArrayList;
import org.alejandrohernandez.analizadorgraphik.EjecucionGraphik;
import org.alejandrohernandez.analizadorgraphik.RecoleccionGraphik;
import org.alejandrohernandez.beans.Als;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.MetodoG;
import org.alejandrohernandez.beans.Nodo;
import org.alejandrohernandez.beans.VariableG;

/**
 *
 * @author oscar
 */
public class ManejadorMetodos {

    EjecucionGraphik eje = new EjecucionGraphik();
    RecoleccionGraphik reco = new RecoleccionGraphik();
    private static ManejadorMetodos instancia;

    public static ManejadorMetodos getInstancia() {
        instanciar();
        return instancia;
    }

    private static synchronized void instanciar() {
        if (instancia == null) {
            instancia = new ManejadorMetodos();
        }
    }
    public int linealizar(ArrayList<Integer> numeros,ArrayList<Integer> tamanios){
        
     int posicion = 0;
        for (int i = 0; i < numeros.size(); i++) {
            if (numeros.get(i)>=tamanios.get(i)) {//se verifica que la posicion sea menor que el tamanio de la dimension
                return -1;
            }
            if (i == 0) {
                posicion += numeros.get(i);
            } else {
                posicion =(posicion *tamanios.get(i)) + numeros.get(i) ;
            }
        }
        return posicion;
    }
    public Object accederObj(Nodo node) {
        Object resultado = new Errores("Imposible acceso", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
        /*Se va a buscar solo el id porque no es objeto y no tiene mas hijos*/
        if (node.numeroHijos == 1) {
            if (node.getHijos2().get(0).getNumeroHijos() == 1) {//es solo un id
                Object var1 = reco.buscarValorVariable(node.getHijos2().get(0).getHijos2().get(0));//se busca el valor de la variable(devuelve una variableg)
                if (!(var1 instanceof Errores)) {
                    return var1;
                }
            } else if (node.getHijos2().get(0).getNumeroHijos() == 2) {//llamada a metodo
                if (node.getHijos2().get(0).getHijos2().get(1).getNombre().equals("sinparametros")) {
                    String idMetodo = node.getHijos2().get(0).getHijos2().get(0).getValor();
                    Object metodoObj = TablaSimbolosGraphik.getInstancia().buscarMetodoG(idMetodo, null, "metodo");
                    if (metodoObj instanceof MetodoG) {
                        MetodoG meto = (MetodoG) metodoObj;
                        TablaSimbolosGraphik.getInstancia().agregarAmbito("metodo");
                        Object result = eje.ejecucion(meto.getInstrucciones());//si devielve un return o no
                        if (meto.getTipo().equals("vacio")) {//si trae retorno no debe tener ningun valor
                            if (result instanceof VariableG) {//se pregunta si trae una variable(retorno)
                                VariableG retorno = (VariableG) result;// se convierte a variable
                                if (!retorno.getValor().equals("sin valor")) {
                                    //se marca error
                                    eje.agregarError("El metodo es tipo void y en el retorno no debe de tener ningun valor", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getColumna());
                                }
                                resultado = result;
                            }
                        } else if (result instanceof VariableG) {//se pregunta si trae una variable(retorno)
                            VariableG retorno = (VariableG) result;// se convierte a variable
                            if (reco.compararTipo(meto.getTipo())) {//es un atributo de tipo normal(Entero, decima...)
                                resultado = retorno;
                            } else if (meto.getTipo().equals("arreglo")) {//es de tipo arreglo

                            } else if (!(reco.compararTipo(meto.getTipo()))) {//es tipo objeto
                                if (retorno.getValor() instanceof Als) {//

                                    Als alsRetorno = (Als) retorno.getValor();
                                    //   TablaSimbolosGraphik.getInstancia().setAlsActivo(alsRetorno);//se pone como als activo el valor de retorno que trae el metodo
                                    resultado = alsRetorno;
                                }

                            }
                        }
                        TablaSimbolosGraphik.getInstancia().quitarAmbito();
                    }
                }

            }

        } else {

            /**
             *
             *
             *
             *
             *
             *
             * esta parte es para el acceso de un objeto porque viene mas hijos
             * en el nodo
             */
            String idObjeto = node.getHijos2().get(0).getHijos2().get(0).getValor();
            Object varObjeto1 = reco.buscarValorVariable(node.getHijos2().get(0).getHijos2().get(0));//se buscar el objeto
            if (varObjeto1 instanceof VariableG) {//se verifica que si lo encuentre
                Als alsObjeto = TablaSimbolosGraphik.getInstancia().buscarAls(((VariableG) varObjeto1).getTipo());
                if (alsObjeto != null) {
                    TablaSimbolosGraphik.getInstancia().setAlsActivo(((Als) ((VariableG) varObjeto1).getValor()));//el primer als encontrado es el als activo
                    for (int i = 1; i < node.getNumeroHijos(); i++) {

                        Nodo nodo1 = node.getHijos2().get(i);
                        if (nodo1.numeroHijos == 1) {//solo es un id
                            String id = nodo1.getHijos2().get(0).getValor();//se obtiene el nombre del id
                            VariableG var = TablaSimbolosGraphik.getInstancia().buscarGlobalmente(id);//se va a buscar globalmente, porque es un atributo de una clase
                            if (var != null) {//se pregunta si existe la veriable o no
                                if (reco.compararTipo(var.getTipo())) {//es un atributo de tipo normal(Entero, decima...)
                                    resultado = var;
                                } else if (var.getTipo().equals("arreglo")) {//es de tipo arreglo

                                } else if (!(reco.compararTipo(var.getTipo()))) {//es tipo objeto

                                    Object varEncontradaObj = reco.buscarValorVariableSoloId(var.getNombre());
                                    if (varEncontradaObj instanceof VariableG) {
                                        VariableG varEncontrada = (VariableG) varEncontradaObj;
                                        TablaSimbolosGraphik.getInstancia().setAlsActivo((Als) varEncontrada.getValor());
                                        resultado = varEncontrada;
                                    } else {
                                        break;
                                    }

                                }
                            } else {//no se ecuentra la variable, error
                                resultado = eje.agregarError("la variable " + id + " no existe.", "semantico", nodo1.getHijos2().get(0).columna, nodo1.getHijos2().get(0).fila);
                                break;
                            }
                        } else if (nodo1.numeroHijos == 2) {//es una llamada a metodo
                            if (nodo1.getHijos2().get(1).getNombre().equals("sinparametros")) {//es una llamada sin parametros
                                String idMetodo = nodo1.getHijos2().get(0).getValor();
                                Object metodoObj = TablaSimbolosGraphik.getInstancia().buscarMetodoG(idMetodo, null, "metodo");
                                if (metodoObj instanceof MetodoG) {
                                    MetodoG meto = (MetodoG) metodoObj;
                                    TablaSimbolosGraphik.getInstancia().agregarAmbito("metodo");
                                    Object result = eje.ejecucion(meto.getInstrucciones());//si devielve un return o no
                                    if (meto.getTipo().equals("vacio")) {//si trae retorno no debe tener ningun valor
                                        if (result instanceof VariableG) {//se pregunta si trae una variable(retorno)
                                            VariableG retorno = (VariableG) result;// se convierte a variable
                                            if (!retorno.getValor().equals("sin valor")) {
                                                //se marca error
                                                eje.agregarError("El metodo es tipo void y en el retorno no debe de tener ningun valor", "Semantico", nodo1.getHijos2().get(0).getColumna(), nodo1.getHijos2().get(0).getColumna());
                                                break;
                                            }
                                            resultado = result;
                                        }
                                    } else if (result instanceof VariableG) {//se pregunta si trae una variable(retorno)
                                        VariableG retorno = (VariableG) result;// se convierte a variable
                                        if (reco.compararTipo(meto.getTipo())) {//es un atributo de tipo normal(Entero, decima...)
                                            resultado = retorno;
                                        } else if (meto.getTipo().equals("arreglo")) {//es de tipo arreglo

                                        } else if (!(reco.compararTipo(meto.getTipo()))) {//es tipo objeto
                                            if (retorno.getValor() instanceof Als) {//

                                                Als alsRetorno = (Als) retorno.getValor();
                                                TablaSimbolosGraphik.getInstancia().setAlsActivo(alsRetorno);//se pone como als activo el valor de retorno que trae el metodo
                                                resultado = alsRetorno;
                                            }

                                        }
                                    }
                                    TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                }
                            } else {///ES UNA LLAMADA CON METODOS

                            }
                        }

                    }

                }

            }
        }
        TablaSimbolosGraphik.getInstancia().setAlsActivo(TablaSimbolosGraphik.getInstancia().getAlsPrincipal());//el principal vuelve hacer el als activo
        return resultado;
    }
}
