/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.analizadorgraphik;

import java.util.ArrayList;
import org.alejandrohernandez.beans.Als;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.Nodo;
import org.alejandrohernandez.beans.VariableG;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.ManejadorMetodos;
import org.alejandrohernandez.manejadores.TablaSimbolosGraphik;

/**
 *
 * @author oscar
 */
public class EjecucionGraphik {

    public static String resultadoFinalGk = "";
    public RecoleccionGraphik reco = new RecoleccionGraphik();
    TablaSimbolosGraphik tabla = TablaSimbolosGraphik.getInstancia();
    public static Errores errorG = new Errores("Error de compilacion", "semantico", 0, 0, "");

    public Object ejecucion(Nodo node) {
        Object result = errorG;
        switch (node.getTipo()) {
            case "SENT": {
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.hijos2.get(0).getHijos2().get(0));
                } else if (node.numeroHijos == 2) {
                    result = ejecucion(node.hijos2.get(0));
                    if (result instanceof VariableG) {
                        VariableG var = (VariableG) result;
                        if (var.getNombre().equalsIgnoreCase("retornar") || var.getNombre().equalsIgnoreCase("terminar") || var.getNombre().equalsIgnoreCase("continuar")) {//si viene un retornar ya no se ejecuta mas codigo
                            return var;
                        }
                    }
                    result = ejecucion(node.hijos2.get(1));
                    if (result instanceof VariableG) {
                        VariableG var = (VariableG) result;
                        if (var.getNombre().equalsIgnoreCase("retornar")) {//si viene un retornar ya no se ejecuta mas codigo
                            return var;
                        }
                    }
                }
                break;
            }
            case "CUERPOSENT": {
                if (node.numeroHijos == 1) {
                    /*  if (node.getHijos2().get(0).getNombre().equals("CONTROLES")) {
                        ejecucion(node.hijos2.get(0).getHijos2().get(0));
                    }   */
                    result = ejecucion(node.hijos2.get(0));
                }
                break;
            }
            case "SENTLOOP": {//INSTRUCCIONES PARA CICLOS
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.hijos2.get(0));
                    if (result instanceof VariableG) {
                        VariableG var = (VariableG) result;
                        if (var.getNombre().equalsIgnoreCase("retornar") || var.getNombre().equalsIgnoreCase("terminar") || var.getNombre().equalsIgnoreCase("continuar")) {//si viene un retornar ya no se ejecuta mas codigo
                            return var;
                        }
                    } else {
                        result = "nada";
                    }
                } else if (node.numeroHijos == 2) {
                    result = ejecucion(node.hijos2.get(0));//PRIMERAS INSTRUCCIONES
                    if (result instanceof VariableG) {
                        VariableG var = (VariableG) result;
                        if (var.getNombre().equalsIgnoreCase("retornar") || var.getNombre().equalsIgnoreCase("terminar") || var.getNombre().equalsIgnoreCase("continuar")) {//si viene un retornar ya no se ejecuta mas codigo
                            return var;
                        } else {
                            result = "nada";
                        }
                    } else {
                        result = "nada";
                    }
                    result = ejecucion(node.hijos2.get(1));//SEGUNDAS INSTRUCCIONES
                    if (result instanceof VariableG) {
                        VariableG var = (VariableG) result;
                        if (var.getNombre().equalsIgnoreCase("retornar") || var.getNombre().equalsIgnoreCase("terminar") || var.getNombre().equalsIgnoreCase("continuar")) {//si viene un retornar ya no se ejecuta mas codigo
                            return var;
                        } else {
                            result = "nada";
                        }
                    } else {
                        result = "nada";
                    }
                }
                break;
            }
            case "CUERPOSENTLOOP": {
                if (node.numeroHijos == 1) {
                    if (node.getHijos2().get(0).getNombre().equals("continuar")) {
                        VariableG retorna = new VariableG("continuar", "continuar", "sin valor");
                        return retorna;
                    } else if (node.getHijos2().get(0).getNombre().equals("terminar")) {
                        VariableG retorna = new VariableG("terminar", "terminar", "sin valor");
                        return retorna;
                    } else {
                        result = ejecucion(node.hijos2.get(0));

                    }

                }
                break;
            }
            case "SINLOOP": {
                if (node.numeroHijos == 1) {
                    if (node.getHijos2().get(0).getNombre().equals("retornar")) {
                        VariableG retorna = new VariableG("retornar", "retornar", "sin valor");
                        return retorna;
                    }
                    result = ejecucion(node.getHijos2().get(0));
                } else if (node.numeroHijos == 2) {
                    if (node.getHijos2().get(0).getNombre().equals("retornar")) {
                        Object valorRetorno = ejecucion(node.getHijos2().get(1));
                        VariableG retorna = new VariableG("retornar", "retornar", valorRetorno);
                        return retorna;
                    }
                }
                break;
            }
            case "FUNNATAS": {
                if (node.numeroHijos == 2) {
                    if (node.getHijos2().get(0).getNombre().equals("graphikar_funcion")) {

                    } else if (node.getHijos2().get(0).getNombre().equals("imprimir")) {
                        Object valorImprime = ejecucion(node.getHijos2().get(1));
                        if (!(valorImprime instanceof Errores)) {
                            resultadoFinalGk += ">>" + valorImprime.toString() + "\n";
                        }

                    } else if (node.getHijos2().get(0).getNombre().equals("llamarhk")) {

                    }
                } else if (node.numeroHijos == 3) {//funcion llamarhk pero con parametros

                }
                break;
            }
            case "VARIABLESLOCAL": {
                boolean sePudo = true;
                if (node.numeroHijos == 2) {//solo DECLARACION
                    String tipo = node.getHijos2().get(0).getValor();
                    for (Nodo nodo : node.getHijos2().get(1).getHijos2()) {
                        VariableG varNuevaLocal = new VariableG(nodo.getHijos2().get(0).getValor(), tipo, "sin valor");
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(varNuevaLocal);
                    }
                } else if (node.numeroHijos == 3) {//declaracion mas asignacion de una variable
                    String tipo = node.getHijos2().get(0).getValor();
                    Object valorVerificar = ejecucion(node.getHijos2().get(2));
                    Object valor = reco.verifacionTipos(tipo, valorVerificar);
                    VariableG varNuevaLocal = new VariableG(node.getHijos2().get(1).getValor(), tipo, valor);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(varNuevaLocal);
                }
                if (!sePudo) {
                    Errores error = new Errores("La variable " + node.getHijos2().get(1).getNombre() + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
                result = true;
                break;
            }
            case "ARREGLOS": {
                boolean sePudo = true;
                if (node.numeroHijos == 3) {//solo declaracion sin visiblidad
                    String tipo = node.getHijos2().get(0).getValor();
                    String id = node.getHijos2().get(1).getValor();
                    int tamanio = reco.tamanioArreglo(node.getHijos2().get(2));
                    ArrayList<Integer> tamanios = reco.tamaniosArreglo(node.getHijos2().get(2));
                    VariableG varGlobalNueva = new VariableG(id, tipo, "arreglo", tamanio, tamanios);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(varGlobalNueva);

                } else if (node.numeroHijos == 4) {
                    if (node.getHijos2().get(3).getNombre().equals("VISIBILIDAD")) {
                        sePudo = false;
                    } else {//declaracion mas asignacion sin visibilidad
                        sePudo = false;
                        String tipo = node.getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        int tamanio = reco.tamanioArreglo(node.getHijos2().get(2));
                        ArrayList<Integer> tamanios = reco.tamaniosArreglo(node.getHijos2().get(2));
                        VariableG varGlobalNueva = new VariableG(id, tipo, "arreglo", tamanio, tamanios);
                        Object asignacion = reco.asignarValoresArreglo(varGlobalNueva, node.getHijos2().get(3)); //en esta parte se asignara los valores

                        if (asignacion instanceof Boolean) {
                            if ((Boolean) asignacion) {
                                sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(varGlobalNueva);
                            }
                        }
                    }

                } else if (node.numeroHijos == 5) {//asignacion mas asignacion y visibilidad
                    sePudo = false;
                }
                if (!sePudo) {
                    Errores error = new Errores("La variable " + node.getHijos2().get(1).getNombre() + " no se pudo guardar. Ya esta creada. O esta poniendole visibilidad", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
                break;
            }
            case "ASIGNACION": {
                if (node.numeroHijos == 2) {//aumento o disminucion de variable o asignacion
                    if (node.getHijos2().get(1).getValor().equals("++")) {//incremento de una variable
                        Object var1 = ManejadorMetodos.getInstancia().accederObj(node.getHijos2().get(0)); //va a buscar la variable, y la va devolver
                        if (var1 instanceof VariableG)//comparo si la variable existe o si el valor es diferente a error
                        {
                            if (((VariableG) var1).getTipo().equals("entero") || ((VariableG) var1).getTipo().equals("decimal")) {
                                if (((VariableG) var1).getValor() instanceof Long) {
                                    long valor = (long) ((VariableG) var1).getValor();
                                    ((VariableG) var1).setValor(valor + 1);
                                } else if (((VariableG) var1).getValor() instanceof Double) {
                                    double valor = (double) ((VariableG) var1).getValor();
                                    ((VariableG) var1).setValor(valor + 1);
                                } else if (((VariableG) var1).getValor() instanceof Character) {
                                    char caracterOperando1 = ((VariableG) var1).getValor().toString().charAt(0);
                                    int cara = caracterOperando1 + 1;
                                    ((VariableG) var1).setValor(cara);
                                }
                            }
                        }
                    } else if (node.getHijos2().get(1).getValor().equals("--")) {
                        Object var1 = ManejadorMetodos.getInstancia().accederObj(node.getHijos2().get(0)); //va a buscar la variable, y la va devolver
                        if (var1 instanceof VariableG)//comparo si la variable existe o si el valor es diferente a error
                        {
                            if (((VariableG) var1).getTipo().equals("entero") || ((VariableG) var1).getTipo().equals("decimal")) {
                                if (((VariableG) var1).getValor() instanceof Long) {
                                    long valor = (long) ((VariableG) var1).getValor();
                                    ((VariableG) var1).setValor(valor - 1);
                                } else if (((VariableG) var1).getValor() instanceof Double) {
                                    double valor = (double) ((VariableG) var1).getValor();
                                    ((VariableG) var1).setValor(valor - 1);
                                } else if (((VariableG) var1).getValor() instanceof Character) {
                                    char caracterOperando1 = ((VariableG) var1).getValor().toString().charAt(0);
                                    int cara = caracterOperando1 - 1;
                                    ((VariableG) var1).setValor(cara);
                                }
                            }
                        }
                    } else {
                        Object valor = ejecucion(node.getHijos2().get(1));
                        if ((valor instanceof VariableG)) {//es un acceso de objetos o un id o metodo
                            cambiarValor(node.getHijos2().get(0), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                        } else if (!(valor instanceof Errores)) {//valores puntuales
                            cambiarValor(node.getHijos2().get(0), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                        }
                    }
                } else if (node.numeroHijos == 3) {//asignacion a una posicion de un arreglo
                    Boolean sePudo = false;
                    Object valor = ejecucion(node.getHijos2().get(2));
                    if ((valor instanceof VariableG)) {//es un acceso de objetos o un id o metodo
                        sePudo = cambiarValorArreglo(node.getHijos2().get(0), node.getHijos2().get(1), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                    } else if (!(valor instanceof Errores)) {//valores puntuales
                        sePudo = cambiarValorArreglo(node.getHijos2().get(0), node.getHijos2().get(1), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                    }
                    if (sePudo == false) {
                        result = errorG;
                    }
                }
                break;
            }

            case "IF": {
                if (node.numeroHijos == 3) {//solo ifs
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {

                        if ((Boolean) entrarIf) {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 4) {
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {
                        if (node.getHijos2().get(2).getNombre().equals("SENT")) {//IF CON SENTENCIAS EN LADO VERDADERO
                            if ((Boolean) entrarIf) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(2));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                            }
                        } else if (node.getHijos2().get(2).getNombre().equals("sino")) {

                            if ((Boolean) entrarIf) {
                            } else {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(3));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                            }
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 5) {//if con else
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {

                        if ((Boolean) entrarIf) {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        } else {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(4));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                }
                break;
            }
            case "SELECCIONA": {
                if (node.numeroHijos == 3) {
                    Object expresion = ejecucion(node.getHijos2().get(1));
                    if (expresion instanceof String || expresion instanceof Double || expresion instanceof Character || expresion instanceof Long) {
                        for (Nodo nodo1 : node.getHijos2().get(2).getHijos2()) {
                            Nodo nod = (Nodo) ejecucion(nodo1.getHijos2().get(0));
                            Object comparar = reco.devolverValor(nod.getTipo(), nod);
                            if (comparar.toString().equals(expresion.toString())) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(1));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                break;
                            }
                        }
                    } else {
                        Errores error = new Errores("El seleccion solo acepta valores numericos, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 4) {
                    boolean entrarDef = true;
                    Object expresion = ejecucion(node.getHijos2().get(1));
                    if (expresion instanceof String || expresion instanceof Double || expresion instanceof Character || expresion instanceof Long) {
                        for (Nodo nodo1 : node.getHijos2().get(2).getHijos2()) {
                            Nodo nod = (Nodo) ejecucion(nodo1.getHijos2().get(0));
                            Object comparar = reco.devolverValor(nod.getTipo(), nod);
                            if (comparar.toString().equals(expresion.toString())) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(1));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                entrarDef = false;
                                break;
                            }
                        }
                        if (entrarDef) {//entro al defecto
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(3));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        Errores error = new Errores("El seleccion solo acepta valores numericos, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                }
                break;
            }
            case "MIENTRAS": {
                Object condicion = ejecucion(node.getHijos2().get(1));
                if (condicion instanceof Boolean) {
                    while ((Boolean) condicion) {
                        TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                        result = ejecucion(node.getHijos2().get(2));
                        if (result instanceof VariableG) {
                            VariableG var = (VariableG) result;
                            if (var.getNombre().equalsIgnoreCase("terminar")) {
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                result = "nada";
                                break;
                            } else if (var.getNombre().equalsIgnoreCase("retornar")) {
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();

                                return var;
                            }
                        }

                        condicion = ejecucion(node.getHijos2().get(1));
                        TablaSimbolosGraphik.getInstancia().quitarAmbito();
                    }
                } else {
                    Errores error = new Errores("Se esperaba una expreison boolean en el while, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }

                break;
            }
            case "HACER": {
                Object condicion = ejecucion(node.getHijos2().get(2));
                if (condicion instanceof Boolean) {
                    do {
                        TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                        result = ejecucion(node.getHijos2().get(1));
                        if (result instanceof VariableG) {
                            VariableG var = (VariableG) result;
                            if (var.getNombre().equalsIgnoreCase("terminar")) {
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                result = "nada";
                                break;
                            } else if (var.getNombre().equalsIgnoreCase("retornar")) {
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();

                                return var;
                            }
                        }
                        condicion = ejecucion(node.getHijos2().get(2));
                        TablaSimbolosGraphik.getInstancia().quitarAmbito();
                    } while ((Boolean) condicion);
                } else {
                    Errores error = new Errores("Se esperaba una expreison boolean en el while, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
            }
            case "PARAA": {
                Object asigPara = ejecucion(node.getHijos2().get(1));
                if (asigPara instanceof Boolean) {
                    if ((Boolean) asigPara) {
                        Object condicion = ejecucion(node.getHijos2().get(2));
                        if (condicion instanceof Boolean) {
                            while ((Boolean) condicion) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(4));//instrcciones
                                if (result instanceof VariableG) {
                                    VariableG var = (VariableG) result;
                                    if (var.getNombre().equalsIgnoreCase("terminar")) {
                                        TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                        result = "nada";
                                        break;
                                    } else if (var.getNombre().equalsIgnoreCase("retornar")) {
                                        TablaSimbolosGraphik.getInstancia().quitarAmbito();

                                        return var;
                                    }
                                }
                                ejecucion(node.getHijos2().get(3));//se incrementa la variable
                                condicion = ejecucion(node.getHijos2().get(2));//se verifica la condicion
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                            }
                        }
                    }

                }
                break;
            }
            case "VARPARA": {
                boolean sePudo = true;
                if (node.numeroHijos == 2) {//asignacion
                    Object valor = ejecucion(node.getHijos2().get(1));
                    if ((valor instanceof VariableG)) {//es un acceso de objetos o un id o metodo
                        if (((VariableG) valor).getValor() instanceof Long) {
                            cambiarValor(node.getHijos2().get(0), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                        }

                    } else if (!(valor instanceof Errores)) {//valores puntuales
                        cambiarValor(node.getHijos2().get(0), valor);//se manda el nodo 0(id al que se le cambiara de valor) y el valor
                    }
                } else if (node.numeroHijos == 3) {//una declaracion
                    String tipo = node.getHijos2().get(0).getValor();
                    Object valorVerificar = ejecucion(node.getHijos2().get(2));
                    Object valor = reco.verifacionTipos(tipo, valorVerificar);
                    VariableG varNuevaLocal = new VariableG(node.getHijos2().get(1).getValor(), tipo, valor);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(varNuevaLocal);
                }
                if (!sePudo) {
                    Errores error = new Errores("La variable " + node.getHijos2().get(1).getNombre() + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
                result = true;
                break;
            }
            case "IFLOOP": {
                if (node.numeroHijos == 3) {//solo ifs
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {

                        if ((Boolean) entrarIf) {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 4) {
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {
                        if (node.getHijos2().get(2).getNombre().equals("SENTLOOP")) {//IF CON SENTENCIAS EN LADO VERDADERO
                            if ((Boolean) entrarIf) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(2));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                            }
                        } else if (node.getHijos2().get(2).getNombre().equals("sino")) {

                            if ((Boolean) entrarIf) {
                            } else {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(3));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                            }
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 5) {//if con else
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {

                        if ((Boolean) entrarIf) {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        } else {
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(4));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        /*SE REPORTA ERROR*/
                        Errores error = new Errores("Se esperaba una expresion booleana", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                }
                break;
            }
            case "SELECCIONALOOP": {
                if (node.numeroHijos == 3) {
                    Object expresion = ejecucion(node.getHijos2().get(1));
                    if (expresion instanceof String || expresion instanceof Double || expresion instanceof Character || expresion instanceof Long) {
                        for (Nodo nodo1 : node.getHijos2().get(2).getHijos2()) {
                            Nodo nod = (Nodo) ejecucion(nodo1.getHijos2().get(0));
                            Object comparar = reco.devolverValor(nod.getTipo(), nod);
                            if (comparar.toString().equals(expresion.toString())) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(1));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                break;
                            }
                        }
                    } else {
                        Errores error = new Errores("El seleccion solo acepta valores numericos, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                } else if (node.numeroHijos == 4) {
                    boolean entrarDef = true;
                    Object expresion = ejecucion(node.getHijos2().get(1));
                    if (expresion instanceof String || expresion instanceof Double || expresion instanceof Character || expresion instanceof Long) {
                        for (Nodo nodo1 : node.getHijos2().get(2).getHijos2()) {
                            Nodo nod = (Nodo) ejecucion(nodo1.getHijos2().get(0));
                            Object comparar = reco.devolverValor(nod.getTipo(), nod);
                            if (comparar.toString().equals(expresion.toString())) {
                                TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                                result = ejecucion(node.getHijos2().get(1));
                                TablaSimbolosGraphik.getInstancia().quitarAmbito();
                                entrarDef = false;
                                break;
                            }
                        }
                        if (entrarDef) {//entro al defecto
                            TablaSimbolosGraphik.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(3));
                            TablaSimbolosGraphik.getInstancia().quitarAmbito();
                        }
                    } else {
                        Errores error = new Errores("El seleccion solo acepta valores numericos, cadenas o caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                        return error;
                    }
                }
                break;
            }
            case "LLAMARFUN": {
                result = ManejadorMetodos.getInstancia().accederObj(node.getHijos2().get(0));
                if (result instanceof VariableG) {
                    VariableG retorno = (VariableG) result;
                    if (true) {

                    }
                }
            }
            case "INSTANCIA": {
                boolean sePudo = true;
                boolean sePudoObj = true;
                Errores errorObj = new Errores("El objeto no existe " + node.getHijos2().get(0).getValor() + " ese ALs no ha sido creado.", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());

                if (node.numeroHijos == 2) {//solo se declara el objeto sin visibilidad
                    String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {
                            String id = node.getHijos2().get(1).getValor();
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, "sin valor");
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(nuevaVar);

                        } else {

                            sePudoObj = false;
                        }

                    } else {
                        sePudoObj = false;
                    }
                } else if (node.numeroHijos == 3) {//se declara el objeto con visibilidad
                    /*String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {

                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, "sin valor", visibilidad);
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(nuevaVar);
                        } else {

                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }*/
                    sePudo = false;
                } else if (node.numeroHijos == 4) {//instancia SIN visibilidad
                    String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {
                            String id = node.getHijos2().get(1).getValor();
                            Als als = reco.clonarAls(tipoObjAls);
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, als);
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(nuevaVar);
                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }
                } else if (node.numeroHijos == 5) {//instancia con visibilidad
                    /*   String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {
                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            Als als = reco.clonarAls(tipoObjAls);
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, als, visibilidad);
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVariableLocal(nuevaVar);
                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }*/
                    sePudo = false;
                }
                if (!sePudo) {
                    Errores error = new Errores("La variable " + node.getHijos2().get(1).getValor() + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
                //   if (!sePudoObj) {
                //     ManejadorErrores.getInstancia().agregarErrorGraphik(errorObj);
                //   return errorObj;
                //}
                result = sePudo;
                break;
            }
            case "E": {
                result = reco.operaciones(node);
                break;
            }
            default:
                result = node;
        }
        return result;
    }

    public String queTipo(Object val) {
        if (val instanceof Double) {
            return "decimal";
        } else if (val instanceof Long) {
            return "entero";
        } else if (val instanceof Boolean) {
            return "bool";
        } else if (val instanceof Character) {
            return "caracter";
        } else if (val instanceof String) {
            return "cadena";
        }
        return "";
    }

    public boolean cambiarValor(Nodo node, Object valor) {
        /*
            Se recibe el nodo del id y el valor que se le quiere asignar como object
         */
        Object var1 = ManejadorMetodos.getInstancia().accederObj(node); //va a buscar la variable, y la va devolver
        if (var1 instanceof VariableG)//comparo si la variable existe o si el valor es diferente a error
        {
            VariableG var1Cast = (VariableG) var1;
            Object resultado = reco.verifacionTipos(var1Cast.getTipo(), valor);//verifico si el valor de la variable que le quiero asignar a la variable guardada se puede castear implicitamente
            if (!resultado.toString().equals("sin valor"))//se verifica si no hubo error en la verificacion de tipos
            {
                if (var1Cast.getValores() == null) {
                    var1Cast.setValor(valor);//se guarda el valor
                } else {//es un arreglo y se debe cambiar el valor de la posicion

                }

                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public boolean cambiarValorArreglo(Nodo node, Nodo nodePosiciones, Object valor) {
        /*
            Se recibe el nodo del id y el valor que se le quiere asignar como object
         */
        Object var1 = ManejadorMetodos.getInstancia().accederObj(node); //va a buscar la variable, y la va devolver
        if (var1 instanceof VariableG)//comparo si la variable existe o si el valor es diferente a error
        {
            VariableG var1Cast = (VariableG) var1;
            Object resultado = reco.verifacionTipos(var1Cast.getTipo(), valor);//verifico si el valor de la variable que le quiero asignar a la variable guardada se puede castear implicitamente
            if (!resultado.toString().equals("sin valor"))//se verifica si no hubo error en la verificacion de tipos
            {
                if (var1Cast.getValores() != null) {
                    ArrayList<Integer> dimensiones = reco.tamaniosArreglo(nodePosiciones);
                    int posicion = ManejadorMetodos.getInstancia().linealizar(dimensiones, var1Cast.getTamanios());
                    if (posicion == -1) {
                        return false;
                    }
                    var1Cast.getValores().set(posicion, valor);//se guarda el valor
                    return true;
                }

            } else {
                return false;
            }

        }
        return false;
    }

    public Errores agregarError(String descripcion, String tipo, int columna, int fila) {
        Errores error = new Errores(descripcion, tipo, columna, fila, TablaSimbolosGraphik.getInstancia().getArchivo());
        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
        return error;
    }
}
