/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.analizadorHaskell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.alejandrohernandez.beans.ArregloHaskell;
import org.alejandrohernandez.beans.Nodo;
import org.alejandrohernandez.beans.Variable;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.Metodo;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.TablaSimbolosHaskell;

/**
 *
 * @author oscar
 */
public class EjecucionHaskell {

    public static String resultadoFinalHaskell = "";
    public static Object ultimaOperacion;

    public Object ejecucion(Nodo node) {
        Object result = new Errores();

        switch (node.getTipo()) {
            case "I": {
                if (node.getNumeroHijos() == 1) {//se debe evualar el retorno
                    result = ejecucion(node.getHijos2().get(0));
                    ultimaOperacion = result;
                } else if (node.getNumeroHijos() == 2) { // se debe evualar el retorno
                    ultimaOperacion = ejecucion(node.getHijos2().get(0));

                    ultimaOperacion = ejecucion(node.getHijos2().get(1));
                }
                break;
            }
            case "SENT": {//aca se debe de imprimir el valor que retorne cada instruccion

                if (node.getNumeroHijos() == 1) {
                    if (node.getHijos2().get(0).getNumeroHijos() == 1) {
                        result = ejecucion(node.getHijos2().get(0).getHijos2().get(0));
                        if (!(result instanceof Errores)) {
                            if (result instanceof ArregloHaskell) {
                                resultadoFinalHaskell += ">>" + imprimirLista((ArregloHaskell) result) + "\n";
                            } else {
                                resultadoFinalHaskell += ">>" + result.toString() + "\n";
                            }

                        }

                        return result;
                    }
                    result = ejecucion(node.getHijos2().get(0));
                    if (!(result instanceof Errores)) {
                        resultadoFinalHaskell += ">>" + result.toString() + "\n";

                    }
                } else if (node.getNumeroHijos() == 3) {//guardar una lista
                    if (node.getHijos2().get(0).getNombre().equals("let")) {//se declara una lista con su valor
                        //se guardar en la lista de variables globales
                        String id = node.getHijos2().get(1).getValor();
                        Object valor = ejecucion(node.getHijos2().get(2));
                        Variable varGlobalNuevo = new Variable(id, "lista", valor);
                        boolean guardar = TablaSimbolosHaskell.getInstancia().agregarVarGlobal(varGlobalNuevo);
                        if (!guardar) {
                            Errores error = new Errores("La variable " + id + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                            return error;
                        }
                        result = valor;
                    }
                }
                break;
            }
            case "OPLISTAS": {
                if (node.getNumeroHijos() == 2) {
                    if (node.getHijos2().get(0).getValor().equals("sum")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        if (arre.getDimension() == 1) {//una dimension
                            result = sumArreglos(arre.getDimension1(), arre.getTipo());
                        } else {//mas de una dimension
                            //se recorre cada dimension
                            double sum = 0.0;
                            for (Object object : arre.getDimension1()) {
                                ArregloHaskell arregloFor = (ArregloHaskell) object;//se convierte de object a ARREGLO HASKELL para operar
                                sum += (Double) sumArreglos(arregloFor.getDimension1(), arregloFor.getTipo());

                            }
                            result = sum;
                        }
                    } else if (node.getHijos2().get(0).getValor().equals("product")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        if (arre.getDimension() == 1) {//una dimension
                            result = productArreglos(arre.getDimension1(), arre.getTipo());
                        } else {//mas de una dimension
                            //se recorre cada dimension
                            double product = 1;
                            for (Object object : arre.getDimension1()) {
                                ArregloHaskell arregloFor = (ArregloHaskell) object;//se convierte de object a ARREGLO HASKELL para operar
                                product *= (Double) productArreglos(arregloFor.getDimension1(), arregloFor.getTipo());

                            }
                            result = product;
                        }
                    } else if (node.getHijos2().get(0).getValor().equals("revers")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        result = reversArreglos(arre);
                    } else if (node.getHijos2().get(0).getValor().equals("impr")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        result = imparesArreglos(arre);
                    } else if (node.getHijos2().get(0).getValor().equals("par")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        result = paresArreglos(arre);
                    } else if (node.getHijos2().get(0).getValor().equals("asc")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        result = ascArreglos(arre);
                    } else if (node.getHijos2().get(0).getValor().equals("desc")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        result = descArreglos(arre);
                    } else if (node.getHijos2().get(0).getValor().equals("length")) {
                        result = ejecucion(node.getHijos2().get(1));
                        ArregloHaskell arre = (ArregloHaskell) result;
                        double tamanio = 0;
                        if (arre.getDimension() == 1) {
                            tamanio = arre.getDimension1().size();
                        } else {
                            //se recorre cada dimension
                            for (Object object : arre.getDimension1()) {
                                ArregloHaskell arregloFor = (ArregloHaskell) object;//se convierte de object a ARREGLO HASKELL para operar
                                tamanio += arregloFor.getDimension1().size();

                            }
                        }
                        result = tamanio;
                    }
                }
                break;
            }
            case "INDICELISTA": {
                if (node.numeroHijos == 3) {
                    if (node.getHijos2().get(1).getNombre().equals("!!")) {//es un indice de lista
                        Object arreglo = ejecucion(node.getHijos2().get(0));
                        double indice = (Double) ejecucion(node.getHijos2().get(2));
                        int indice2 = 0;
                        result = obtenerIndice((ArregloHaskell) arreglo, (int) indice, indice2);
                        if (result instanceof Errores) {
                            Errores err = (Errores) result;
                            err.setColumna(node.getHijos2().get(1).getColumna());
                            err.setFila(node.getHijos2().get(1).getFila());
                            ManejadorErrores.getInstancia().agregarErrorHaskell(err);
                            return err;
                        }
                    } else if (node.getHijos2().get(1).getNombre().equals("++")) {
                        Object arre1 = ejecucion(node.getHijos2().get(0));
                        Object arre2 = ejecucion(node.getHijos2().get(2));
                        if ((arre1 instanceof ArregloHaskell) && arre2 instanceof ArregloHaskell) {
                            //se manda a concatenar los arreglos
                            result = concatenar((ArregloHaskell) arre1, (ArregloHaskell) arre2);
                            if (result instanceof Errores) {
                                Errores err = (Errores) result;
                                err.setColumna(node.getHijos2().get(1).getColumna());
                                err.setFila(node.getHijos2().get(1).getFila());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(err);
                                return err;
                            }
                        } else {
                            //error
                            Errores error = new Errores("No se pudo concatenar sus listas.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                            return error;
                        }
                    }
                } else if (node.numeroHijos == 5) {
                    if (node.getHijos2().get(1).getNombre().equals("!!")) {//es un indice de lista
                        Object arreglo = ejecucion(node.getHijos2().get(0));
                        double indice = (Double) ejecucion(node.getHijos2().get(2));
                        double indice2 = (Double) ejecucion(node.getHijos2().get(4));
                        result = obtenerIndice((ArregloHaskell) arreglo, (int) indice, (int) indice2);
                        if (result instanceof Errores) {
                            Errores err = (Errores) result;
                            err.setColumna(node.getHijos2().get(1).getColumna());
                            err.setFila(node.getHijos2().get(1).getFila());
                            ManejadorErrores.getInstancia().agregarErrorHaskell(err);
                        }
                    }
                }
                break;
            }
            case "LLAMARINDICE": {
                result = ejecucion(node.getHijos2().get(0));
                break;
            }
            case "LLAMADASNETAS": {
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.getHijos2().get(0));
                } else if (node.getNumeroHijos() == 2) {
                    if (node.getHijos2().get(0).getValor().equals("calcular")) {//de vuelve el valor de realizar una operacion aritmetica
                        result = ejecucion(node.getHijos2().get(1));
                    } else if (node.getHijos2().get(0).getValor().equals("succ")) {//el resultado se le debe sumar 1
                        result = ejecucion(node.getHijos2().get(1));
                        if (!(result instanceof Errores)) {
                            if (result instanceof Character) {
                                result = ((Character) result) + 1;
                            } else {
                                result = ((double) result + 1);
                            }

                        }
                    } else if (node.getHijos2().get(0).getValor().equals("decc")) {//al resultado se le debe de restar 1
                        result = ejecucion(node.getHijos2().get(1));
                        if (!(result instanceof Errores)) {
                            if (result instanceof Character) {
                                result = ((Character) result) - 1;
                            } else {
                                result = ((double) result - 1);
                            }

                        }
                    } else if (node.getHijos2().get(0).getValor().equals("min")) {//el resultado sera el minimo de una lista
                        result = ejecucion(node.getHijos2().get(1));
                        if (!(result instanceof Errores) && (result instanceof ArregloHaskell)) {
                            ArregloHaskell arre = (ArregloHaskell) result;
                            if (arre.getDimension() == 1) {//es de una dimension
                                Object min;
                                if (arre.getTipo().equals("entero") || arre.getTipo().equals("decimal")) {
                                    min = obtenerNumeroMenor(arre.getDimension1());
                                } else {
                                    min = obtenerMenorCaracter(arre.getDimension1());
                                }

                                return min;
                            } else {//de mas dimensiones
                                ArregloHaskell arr = ((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0));

                                Double min = null;//variable usada para guardar el numero menor y retornarlo
                                char minC = 0;//variable usda para guardar el caracter menor y retornarlo
                                if (arr.getTipo().equals("entero") || arr.getTipo().equals("decimal")) {
                                    min = obtenerNumeroMenor(((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0)).getDimension1());//se obtiene el primer arraylist de las dimensiones para saber cual es el minio y asi empezar a evalu
                                } else {
                                    minC = obtenerMenorCaracter(((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0)).getDimension1());//se obtiene el primer arraylist de las dimensiones para saber cual es el minio y asi empezar a evalu
                                }
                                //se recorre cada dimension
                                for (Object object : ((ArregloHaskell) result).getDimension1()) {
                                    ArregloHaskell arregloFor = (ArregloHaskell) object;//se convierte de object a ARREGLO HASKELL para operar
                                    if (arregloFor.getTipo().equals("entero") || arregloFor.getTipo().equals("decimal")) {//se verifica si es en o dec para llamar al metodp
                                        Double minEvaluar = obtenerNumeroMenor(arregloFor.getDimension1());//devuelve el numero menor de cada lista
                                        if (minEvaluar < min) {
                                            min = minEvaluar;
                                        }
                                    } else {
                                        char minEvaluarC = obtenerMenorCaracter(arregloFor.getDimension1());//devuelve el caracter menor
                                        if (minEvaluarC < minC) {
                                            minC = minEvaluarC;
                                        }
                                    }

                                }
                                if (arr.getTipo().equals("caracter")) {//si es de tipo caracter se devuelve la variable minC
                                    return minC;
                                }
                                return min;
                            }
                        }
                    } else if (node.getHijos2().get(0).getValor().equals("max")) {
                        result = ejecucion(node.getHijos2().get(1));
                        if (!(result instanceof Errores) && (result instanceof ArregloHaskell)) {
                            ArregloHaskell arre = (ArregloHaskell) result;
                            if (arre.getDimension() == 1) {//es de una dimension
                                Object max;
                                if (arre.getTipo().equals("entero") || arre.getTipo().equals("decimal")) {
                                    max = obtenerNumeroMayor(arre.getDimension1());
                                } else {
                                    max = obtenerMayorCaracter(arre.getDimension1());
                                }

                                return max;
                            } else {//de mas dimensiones
                                ArregloHaskell arr = ((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0));

                                Double max = null;//variable usada para guardar el numero menor y retornarlo
                                char maxC = 0;//variable usda para guardar el caracter menor y retornarlo
                                if (arr.getTipo().equals("entero") || arr.getTipo().equals("decimal")) {
                                    max = obtenerNumeroMayor(((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0)).getDimension1());//se obtiene el primer arraylist de las dimensiones para saber cual es el maxio y asi empezar a evalu
                                } else {
                                    maxC = obtenerMayorCaracter(((ArregloHaskell) ((ArregloHaskell) result).getDimension1().get(0)).getDimension1());//se obtiene el primer arraylist de las dimensiones para saber cual es el maxio y asi empezar a evalu
                                }
                                //se recorre cada dimension
                                for (Object object : ((ArregloHaskell) result).getDimension1()) {
                                    ArregloHaskell arregloFor = (ArregloHaskell) object;//se convierte de object a ARREGLO HASKELL para operar
                                    if (arregloFor.getTipo().equals("entero") || arregloFor.getTipo().equals("decimal")) {//se verifica si es en o dec para llamar al metodp
                                        Double maxEvaluar = obtenerNumeroMayor(arregloFor.getDimension1());//devuelve el numero menor de cada lista
                                        if (maxEvaluar > max) {
                                            max = maxEvaluar;
                                        }
                                    } else {
                                        char maxEvaluarC = obtenerMayorCaracter(arregloFor.getDimension1());//devuelve el caracter menor
                                        if (maxEvaluarC > maxC) {
                                            maxC = maxEvaluarC;
                                        }
                                    }

                                }
                                if (arr.getTipo().equals("caracter")) {//si es de tipo caracter se devuelve la variable maxC
                                    return maxC;
                                }
                                return max;
                            }
                        }
                    }
                }
                break;
            }
            case "FUNCIONES": {
                if (node.getNumeroHijos() == 2) {//Funcion SIN parametros 
                    String id = node.getHijos2().get(0).getValor();

                    Metodo metodoNuevo = new Metodo(id, "", null, node.getHijos2().get(2));
                    boolean sePuede = TablaSimbolosHaskell.getInstancia().agregarMetodo(metodoNuevo);
                    if (!sePuede) {
                        Errores error = new Errores("El metodo " + id + " ya existe. Ya esta creada.", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        return error;
                    }
                } else if (node.getNumeroHijos() == 3) {//Funcion CON parametros
                    String id = node.getHijos2().get(0).getValor();
                    ArrayList<Variable> listaPara = new ArrayList<Variable>();
                    for (Nodo nodo : node.getHijos2().get(1).getHijos2()) {
                        Variable varNueva = new Variable(nodo.getNombre(), "", "sin valor");
                        listaPara.add(varNueva);
                    }
                    Metodo metodoNuevo = new Metodo(id, "", listaPara, node.getHijos2().get(2));
                    boolean sePuede = TablaSimbolosHaskell.getInstancia().agregarMetodo(metodoNuevo);
                    if (!sePuede) {
                        Errores error = new Errores("El metodo " + id + " ya existe. Ya esta creada.", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        return error;
                    }
                    result = "";
                }
                break;
            }
            case "DIME": {
                if (node.getNumeroHijos() == 1) {//solo de una dimension
                    if (node.getHijos2().get(0).getTipo().equals("cadena")) {
                        return devolverArreglo(node.getHijos2().get(0));
                    } else if (node.getHijos2().get(0).getTipo().equals("entero") || node.getHijos2().get(0).getTipo().equals("decimal") || node.getHijos2().get(0).getTipo().equals("caracter")) {
                        return arregloUnaDimension(node, "");
                    } else if (node.getHijos2().get(0).getNombre().equals("OPLISTAS") || node.getHijos2().get(0).getNombre().equals("LLAMADAS")) {
                        return ejecucion(node.getHijos2().get(0));
                    } else if (node.getHijos2().get(0).getTipo().equals("id")) {
                        //se busca el id
                        Object id = ejecucion(node.getHijos2().get(0));//se trae el nombre del id
                        Object var = buscarValorVariable((Nodo) id);//se busca la variable
                        if (var instanceof Variable) {//se verifica si se encuentra
                            Variable var2 = ((Variable) var);
                            Object valor = var2.getValor();
                            if (valor instanceof ArregloHaskell) {//se verifica que sea de tipo lista
                                return valor;
                            } else {
                                Errores error = new Errores("La variable" + var2.getNombre() + " no es de tipo lista", "Semantico", ((Nodo) id).getColumna(), ((Nodo) id).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }
                        }
                    } else if (node.getHijos2().get(0).getTipo().equals("%")) {
                        return ultimaOperacion;
                    }
                    return arregloUnaDimension(node, "");
                } else {//mas dimensiones
                    if (node.numeroHijos == 3) {
                        if (node.getHijos2().get(1).getNombre().equals("++")) {
                            Object arre1 = ejecucion(node.getHijos2().get(0));
                            Object arre2 = ejecucion(node.getHijos2().get(2));
                            if ((arre1 instanceof ArregloHaskell) && arre2 instanceof ArregloHaskell) {
                                //se manda a concatenar los arreglos
                                result = concatenar((ArregloHaskell) arre1, (ArregloHaskell) arre2);
                                if (result instanceof Errores) {
                                    Errores err = (Errores) result;
                                    err.setColumna(node.getHijos2().get(1).getColumna());
                                    err.setFila(node.getHijos2().get(1).getFila());
                                    ManejadorErrores.getInstancia().agregarErrorHaskell(err);
                                    return err;
                                }
                                return result;
                            } else {
                                //error
                                Errores error = new Errores("No se pudo concatenar sus listas.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                                return error;
                            }
                        }
                    }
                    return arregloMasDimensiones(node, "");

                }
            }
            case "DIME2": {

                break;
            }
            case "LLAMADAS": {
                if (node.numeroHijos == 1) {

                    result = ejecucion(node.getHijos2().get(0));

                }
                break;
            }
            case "LLAMARFUN": {
                if (node.numeroHijos == 1) {//llamada SIN parametros
                    Object id = ejecucion(node.getHijos2().get(0));
                    Metodo met = TablaSimbolosHaskell.getInstancia().buscarMetodo(((Nodo) id).getValor(), null);
                    if (met != null) {
                        TablaSimbolosHaskell.getInstancia().agregarAmbito("metodo");
                        result = ejecucion(met.getInstrucciones());
                        TablaSimbolosHaskell.getInstancia().quitarAmbito();
                    } else {
                        Errores error = new Errores("El metodo" + ((Nodo) id).getNombre() + " no existe.", "Semantico", ((Nodo) id).getColumna(), ((Nodo) id).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                        return error;
                    }
                } else if (node.numeroHijos == 2) {//llamada CON parametros
                    Object id = ejecucion(node.getHijos2().get(0));
                    ArrayList listaPar = new ArrayList();
                    for (Nodo nodo : node.getHijos2().get(1).getHijos2()) {

                        listaPar.add(ejecucion(nodo));
                    }
                    Metodo met = TablaSimbolosHaskell.getInstancia().buscarMetodo(((Nodo) id).getValor(), listaPar);
                    if (met != null) {
                        TablaSimbolosHaskell.getInstancia().agregarAmbito("metodo");
                        for (int i = 0; i < met.getListaParametros().size(); i++) {//agreag las variables a la pila
                            if (listaPar.get(i) instanceof String) {
                                listaPar.set(i, devolverArreglo(new Nodo(listaPar.get(i).toString())));
                            }
                            TablaSimbolosHaskell.getInstancia().agregarVariableLocal(new Variable(met.getListaParametros().get(i).getNombre(), "var", listaPar.get(i)));
                        }
                        result = ejecucion(met.getInstrucciones());
                        TablaSimbolosHaskell.getInstancia().quitarAmbito();
                    } else {
                        Errores error = new Errores("El metodo" + ((Nodo) id).getNombre() + " no existe.", "Semantico", ((Nodo) id).getColumna(), ((Nodo) id).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                        return error;
                    }
                }
                break;
            }
            case "INS": {
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.getHijos2().get(0));
                } else if (node.numeroHijos == 2) {
                    result = ejecucion(node.hijos2.get(0));
                    result = ejecucion(node.hijos2.get(1));
                }
                break;
            }
            case "INSTRUCCIONES": {
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.getHijos2().get(0));
                } else if (node.numeroHijos == 3) {
                    if (node.getHijos2().get(0).getNombre().equals("let")) {//se declara una lista con su valor
                        //se guardar en la lista de variables globales
                        String id = node.getHijos2().get(1).getValor();
                        Object valor = ejecucion(node.getHijos2().get(2));
                        Variable varGlobalNuevo = new Variable(id, "lista", valor);
                        boolean guardar = TablaSimbolosHaskell.getInstancia().agregarVariableLocal(varGlobalNuevo);
                        if (!guardar) {
                            Errores error = new Errores("La variable " + id + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                            return error;
                        }
                        result = valor;
                    }
                }
                break;
            }
            case "IF": {
                if (node.numeroHijos == 3) {
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {
                        if (((Boolean) entrarIf)) {
                            TablaSimbolosHaskell.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosHaskell.getInstancia().quitarAmbito();
                        }
                    }
                } else if (node.numeroHijos == 4) {
                    Object entrarIf = ejecucion(node.getHijos2().get(1));
                    if (entrarIf instanceof Boolean) {
                        if (((Boolean) entrarIf)) {
                            TablaSimbolosHaskell.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(2));
                            TablaSimbolosHaskell.getInstancia().quitarAmbito();
                        } else {
                            TablaSimbolosHaskell.getInstancia().agregarAmbito("normal");
                            result = ejecucion(node.getHijos2().get(3));
                            TablaSimbolosHaskell.getInstancia().quitarAmbito();
                        }
                    }
                }
                break;
            }
            case "CASE": {
                if (node.numeroHijos == 3) {
                    TablaSimbolosHaskell.getInstancia().agregarAmbito("normal");
                    Object exp = ejecucion(node.getHijos2().get(1));
                    int entro = 0;
                    Object valEvaluar = null;
                    for (Nodo nodo : node.getHijos2().get(2).getHijos2()) {

                        valEvaluar = devolverValor(((Nodo) ejecucion(nodo.getHijos2().get(0))).tipo, (Nodo) ejecucion(nodo.getHijos2().get(0)));;
                        if (exp.toString().equals(valEvaluar.toString())) {
                            result = ejecucion(nodo.getHijos2().get(1));
                            entro = 1;
                            break;
                        }
                    }
                    if (entro == 0) {
                        if (valEvaluar != null) {
                            if (valEvaluar instanceof Character) {
                                result = ' ';
                            } else if (valEvaluar instanceof Double) {
                                result = 0.0;
                            }
                        }
                    }
                    TablaSimbolosHaskell.getInstancia().quitarAmbito();
                }
                break;
            }
            case "EXP": {
                //se verificara si son de tipo lista LIST2 o DIME2
                if (node.getHijos2().get(0).getNombre().equals("LIST2")) {// DE UNA DIMENSION

                    return arregloUnaDimension(node, "E");
                } else if (node.getHijos2().get(0).getNombre().equals("DIME2")) {//MAS DE UNA DIMENSION
                    return arregloMasDimensiones(node, "");
                }

                if (node.getNumeroHijos() == 1) {
                    Object re = ejecucion(node.getHijos2().get(0));

                    if (re instanceof Nodo) {
                        Nodo nodo = (Nodo) re;
                        if (nodo.getTipo().equals("entero") || nodo.getTipo().equals("decimal") || nodo.getTipo().equals("cadena") || nodo.getTipo().equals("caracter")) {
                            return devolverValor(nodo.tipo, nodo);
                        } else if (nodo.getTipo().equals("id")) {
                            //se busca el id
                            Object var = buscarValorVariable((Nodo) re);//se busca la variable
                            if (var instanceof Variable) {//se verifica si se encuentra
                                Variable var2 = ((Variable) var);
                                Object valor = var2.getValor();
                                // if (valor instanceof ArregloHaskell) {//se verifica que sea de tipo lista
                                return valor;
                                /*   } else {
                                Errores error = new Errores("La variable" + var2.getNombre() + " no es de tipo lista", "Semantico", ((Nodo) re).getColumna(), ((Nodo) re).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }*/
                            }
                        } else if (nodo.getValor().equals("%")) {

                            return ultimaOperacion;
                        }
                    }
                    result = re;

                } else if (node.getNumeroHijos() == 1) {

                } else if (node.numeroHijos == 2) {
                    if (node.getHijos2().get(0).getNombre().equals("calcular")) {
                        result = ejecucion(node.getHijos2().get(1));
                    } else if (node.getHijos2().get(0).getNombre().equals("-")) {
                        result = (Double) ((-1) * (Double) ejecucion(node.getHijos2().get(1)));
                    }
                } else if (node.getNumeroHijos() == 3) {
                    Object operando1;
                    Object operador;
                    Object operando2;
                    if (node.getHijos2().get(0).getNombre().equals("EXP")) {
                        boolean hacerOp;
                        operando1 = ejecucion(node.getHijos2().get(0));
                        operador = ejecucion(node.getHijos2().get(1));
                        operando2 = ejecucion(node.getHijos2().get(2));
                        if (!(operando1 instanceof Errores) && !(operando2 instanceof Errores)) {
                            if (((Nodo) operador).getValor().equals("++")) {//operacion de listas
                                if ((operando1 instanceof ArregloHaskell) && (operando2 instanceof ArregloHaskell)) {
                                    return concatenar((ArregloHaskell) operando1, (ArregloHaskell) operando2);
                                }
                            }
                            //es una operacion normal aritmetica
                            result = OperacioAritmetica(operando1, ((Nodo) operador).getValor(), operando2);
                        }
                        if (result instanceof Errores) {
                            Errores error = (Errores) result;
                            error.setColumna(((Nodo) operando1).getColumna());
                            error.setFila(((Nodo) operando1).getFila());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        }
                    } else if (node.getHijos2().get(0).getNombre().equals("(")) {
                        result = ejecucion(node.getHijos2().get(1));
                    }
                }
                break;
            }
            /*
                -------------------------------------------------EXPRESION LOGICA -------------------------- 
             */
            case "EXPL": {
                if (node.numeroHijos == 1) {
                    result = ejecucion(node.getHijos2().get(0));
                } else if (node.getNumeroHijos() == 3) {
                    Object operando1;
                    Object operador;
                    Object operando2;
                    if (node.getHijos2().get(0).getNombre().equals("EXPL")) {
                        boolean hacerOp;
                        operando1 = ejecucion(node.getHijos2().get(0));
                        operador = ejecucion(node.getHijos2().get(1));
                        operando2 = ejecucion(node.getHijos2().get(2));
                        if (!(operando1 instanceof Errores) && !(operando2 instanceof Errores)) {
                            if ((operando1 instanceof Boolean) && (operando2 instanceof Boolean)) {
                                //es una operacion normal aritmetica
                                result = operacionLogica(operando1, ((Nodo) operador).getValor(), operando2);
                            } else {
                                Errores error = new Errores("La operacion relacional solo acepta numeros", "Semantico", ((Nodo) operador).getColumna(), ((Nodo) operador).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }
                        }
                        if (result instanceof Errores) {
                            Errores error = (Errores) result;
                            error.setColumna(((Nodo) operando1).getColumna());
                            error.setFila(((Nodo) operando1).getFila());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        }
                    } else if (node.getHijos2().get(0).getNombre().equals("(")) {
                        result = ejecucion(node.getHijos2().get(1));
                    }
                }
                break;
            }
            case "EXPR": {
                //se verificara si son de tipo lista LIST2 o DIME2
                if (node.getHijos2().get(0).getNombre().equals("LIST2")) {// DE UNA DIMENSION

                    return arregloUnaDimension(node, "E");
                } else if (node.getHijos2().get(0).getNombre().equals("DIME2")) {//MAS DE UNA DIMENSION
                    return arregloMasDimensiones(node, "");
                }

                if (node.getNumeroHijos() == 1) {
                    Object re = ejecucion(node.getHijos2().get(0));

                    if (re instanceof Nodo) {
                        Nodo nodo = (Nodo) re;
                        if (nodo.getTipo().equals("entero") || nodo.getTipo().equals("decimal") || nodo.getTipo().equals("cadena") || nodo.getTipo().equals("caracter")) {
                            return devolverValor(nodo.tipo, nodo);
                        } else if (nodo.getTipo().equals("id")) {
                            //se busca el id
                            Object var = buscarValorVariable((Nodo) re);//se busca la variable
                            if (var instanceof Variable) {//se verifica si se encuentra
                                Variable var2 = ((Variable) var);
                                Object valor = var2.getValor();
                                // if (valor instanceof ArregloHaskell) {//se verifica que sea de tipo lista
                                return valor;
                                /*   } else {
                                Errores error = new Errores("La variable" + var2.getNombre() + " no es de tipo lista", "Semantico", ((Nodo) re).getColumna(), ((Nodo) re).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }*/
                            }
                        } else if (nodo.getValor().equals("%")) {

                            return ultimaOperacion;
                        }
                    }
                    result = re;

                } else if (node.numeroHijos == 2) {
                    if (node.getNombre().equals("calcular")) {
                        result = ejecucion(node.getHijos2().get(1));
                    }
                } else if (node.getNumeroHijos() == 3) {
                    Object operando1;
                    Object operador;
                    Object operando2;
                    if (node.getHijos2().get(0).getNombre().equals("EXPR")) {
                        boolean hacerOp;
                        operando1 = ejecucion(node.getHijos2().get(0));
                        operador = ejecucion(node.getHijos2().get(1));
                        operando2 = ejecucion(node.getHijos2().get(2));
                        if (!(operando1 instanceof Errores) && !(operando2 instanceof Errores)) {
                            if ((operando1 instanceof Double) && (operando2 instanceof Double)) {
                                //es una operacion normal aritmetica
                                result = operacionRelacional(operando1, ((Nodo) operador).getValor(), operando2);
                            } else {
                                Errores error = new Errores("La operacion relacional solo acepta numeros", "Semantico", ((Nodo) operador).getColumna(), ((Nodo) operador).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }
                        }
                        if (result instanceof Errores) {
                            Errores error = (Errores) result;
                            error.setColumna(((Nodo) operando1).getColumna());
                            error.setFila(((Nodo) operando1).getFila());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        }
                    }
                }
                break;
            }
            case "EXP2": {
                //se verificara si son de tipo lista LIST2 o DIME2
                if (node.getHijos2().get(0).getNombre().equals("LIST2")) {// DE UNA DIMENSION

                    return arregloUnaDimension(node, "E");
                } else if (node.getHijos2().get(0).getNombre().equals("DIME2")) {//MAS DE UNA DIMENSION
                    return arregloMasDimensiones(node, "");
                }

                if (node.getNumeroHijos() == 1) {
                    Object re = ejecucion(node.getHijos2().get(0));

                    if (re instanceof Nodo) {
                        Nodo nodo = (Nodo) re;
                        if (nodo.getTipo().equals("entero") || nodo.getTipo().equals("decimal") || nodo.getTipo().equals("cadena") || nodo.getTipo().equals("caracter")) {
                            return devolverValor(nodo.tipo, nodo);
                        } else if (nodo.getTipo().equals("id")) {
                            //se busca el id
                            Object var = buscarValorVariable((Nodo) re);//se busca la variable
                            if (var instanceof Variable) {//se verifica si se encuentra
                                Variable var2 = ((Variable) var);
                                Object valor = var2.getValor();
                                // if (valor instanceof ArregloHaskell) {//se verifica que sea de tipo lista
                                return valor;
                                /*   } else {
                                Errores error = new Errores("La variable" + var2.getNombre() + " no es de tipo lista", "Semantico", ((Nodo) re).getColumna(), ((Nodo) re).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                                ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                                return error;
                            }*/
                            }
                        } else if (nodo.getValor().equals("%")) {

                            return ultimaOperacion;
                        }
                    }
                    result = re;

                } else if (node.getNumeroHijos() == 1) {

                } else if (node.numeroHijos == 2) {
                    if (node.getHijos2().get(0).getNombre().equals("calcular")) {
                        result = ejecucion(node.getHijos2().get(1));
                    } else if (node.getHijos2().get(0).getNombre().equals("-")) {
                        result = (Double) ((-1) * (Double) ejecucion(node.getHijos2().get(1)));
                    }
                } else if (node.getNumeroHijos() == 3) {
                    Object operando1;
                    Object operador;
                    Object operando2;
                    if (node.getHijos2().get(0).getNombre().equals("EXP2")) {
                        boolean hacerOp;
                        operando1 = ejecucion(node.getHijos2().get(0));
                        operador = ejecucion(node.getHijos2().get(1));
                        operando2 = ejecucion(node.getHijos2().get(2));
                        if (!(operando1 instanceof Errores) && !(operando2 instanceof Errores)) {
                            if (((Nodo) operador).getValor().equals("++")) {//operacion de listas
                                if ((operando1 instanceof ArregloHaskell) && (operando2 instanceof ArregloHaskell)) {
                                    return concatenar((ArregloHaskell) operando1, (ArregloHaskell) operando2);
                                }
                            }
                            //es una operacion normal aritmetica
                            result = OperacioAritmetica(operando1, ((Nodo) operador).getValor(), operando2);
                        }
                        if (result instanceof Errores) {
                            Errores error = (Errores) result;
                            error.setColumna(((Nodo) operando1).getColumna());
                            error.setFila(((Nodo) operando1).getFila());
                            ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        }
                    } else if (node.getHijos2().get(0).getNombre().equals("(")) {
                        result = ejecucion(node.getHijos2().get(1));
                    }
                }
                break;
            }
            default:
                result = node;
        }
        return result;
    }

    private ArregloHaskell devolverArreglo(Nodo node) {
        String cadena = node.getValor();
        cadena = cadena.replaceAll("\"", "");
        ArrayList dimension1 = new ArrayList();
        for (int i = 0; i < cadena.length(); i++) {
            dimension1.add(cadena.charAt(i));
        }
        ArregloHaskell arregloNuevo = new ArregloHaskell(1, "caracter");
        arregloNuevo.setDimension1(dimension1);
        return arregloNuevo;
    }

    public Object OperacioAritmetica(Object operando1, Object operador, Object operando2) {
        operando1 = (Double) (operando1);
        operando2 = (Double) operando2;
        if (operador.equals("+")) {
            return (double) ((double) operando1 + (double) operando2);
        } else if (operador.equals("-")) {
            return (double) ((double) operando1 - (double) operando2);
        } else if (operador.equals("*")) {
            return (double) ((double) operando1 * (double) operando2);
        } else if (operador.equals("/")) {
            if ((double) operando2 == 0) {
                return new Errores("No se puede hacer una division dentro de 0", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
            }
            return (double) ((double) operando1 / (double) operando2);
        } else if (operador.equals("'mod'")) {
            if ((double) operando2 == 0) {
                return new Errores("No se puede hacer mod dentro de 0", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
            }
            return (double) ((double) operando1 % (double) operando2);

        } else if (operador.equals("'pot'")) {
            return Math.pow((double) operando1, (double) operando2);
        } else if (operador.equals("'sqrt'")) {
            return Math.pow((double) operando2, 1 / (double) operando1);
        }
        return new Errores("No se puede hacer la operacion", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
    }

    public Object operacionRelacional(Object operando1, Object operador, Object operando2) {
        double ope1 = (Double) operando1;
        double ope2 = (Double) operando2;
        if (operador.equals("<")) {
            return ope1 < ope2;
        } else if (operador.equals(">")) {
            return ope1 > ope2;
        } else if (operador.equals("<=")) {
            return ope1 <= ope2;
        } else if (operador.equals(">=")) {
            return ope1 >= ope2;
        } else if (operador.equals("==")) {
            return ope1 == ope2;
        } else if (operador.equals("!=")) {
            return ope1 != ope2;
        }
        return new Errores("No se puede hacer la operacion relacional", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
    }

    public Object operacionLogica(Object operando1, Object operador, Object operando2) {
        if ((operando1 instanceof Boolean) && (operando2 instanceof Boolean)) {//se verifica que los dos operandos sean booleanos
            boolean ope1 = (Boolean) operando1;
            boolean ope2 = (Boolean) operando2;
            if (operador.equals("&&")) {

                if (ope1 && ope2) {
                    return true;
                } else {
                    return false;
                }
            } else if (operador.equals("||")) {
                if (ope1 || ope2) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return new Errores("No se puede hacer la operacion, no son de tipo booleano los operadores", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
        }
        return new Errores("No se puede hacer la operacion", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
    }

    public Object devolverValor(String tipo, Nodo nodo) {
        if (tipo.equals("entero") || tipo.equals("decimal")) {
            return Double.parseDouble(nodo.getValor());
        } else if (tipo.equals("caracter")) {
            char cha = nodo.getValor().replaceAll("'", "").charAt(0);
            return cha;
        }
        String valor = nodo.getValor().replaceAll("\"", "").replaceAll("'", "");
        return valor;
    }

    public double obtenerNumeroMayor(ArrayList lista) {
        double max = (Double) lista.get(0);
        for (Object object : lista) {
            if (object instanceof Double) {
                if ((Double) object > max) {
                    max = (Double) object;
                }
            }
        }
        return max;
    }

    public double obtenerNumeroMenor(ArrayList lista) {
        double min = (Double) lista.get(0);
        for (Object object : lista) {
            if (object instanceof Double) {
                if ((Double) object < min) {
                    min = (Double) object;
                }
            }
        }
        return min;
    }

    public char obtenerMenorCaracter(ArrayList lista) {
        char min = (Character) lista.get(0);
        for (Object object : lista) {
            if ((Character) object < min) {
                min = (Character) object;
            }
        }
        return min;
    }

    public char obtenerMayorCaracter(ArrayList lista) {
        char max = (Character) lista.get(0);
        for (Object object : lista) {
            if ((Character) object > max) {
                max = (Character) object;
            }
        }
        return max;
    }

    public Object arregloUnaDimension(Nodo node, String nom) {
        ArrayList<Nodo> hijos = new ArrayList<Nodo>();
        hijos = node.getHijos2().get(0).getHijos2();
        if (nom.equals("E")) {
            hijos = node.getHijos2();
        }
        ArrayList dime = new ArrayList();
        String tipo = "";
        int conta = 0;
        for (Nodo nodo : hijos) {//se obtiene los valores de la dimension
            if (conta == 0) {
                if (nodo.getHijos2().get(0).getTipo().equals("entero") || nodo.getHijos2().get(0).getTipo().equals("cadena") || nodo.getHijos2().get(0).getTipo().equals("caracter") || nodo.getHijos2().get(0).getTipo().equals("decimal")) {
                    tipo = nodo.getHijos2().get(0).getTipo();
                    dime.add(devolverValor(tipo, nodo.getHijos2().get(0)));
                } else {//se manda a ejecutar el hijo para que devuleva un valor

                    Object resul = ejecucion(nodo.getHijos2().get(0));
                    if (resul instanceof Double) {
                        tipo = "entero";
                    } else if (resul instanceof Character) {
                        tipo = "caracter";
                    }
                    dime.add(resul);
                }

            } else if (nodo.getHijos2().get(0).getTipo().equals("entero") || nodo.getHijos2().get(0).getTipo().equals("cadena") || nodo.getHijos2().get(0).getTipo().equals("caracter") || nodo.getHijos2().get(0).getTipo().equals("decimal")) {
                if (tipo.equals(nodo.getHijos2().get(0).getTipo())) {//se verifica que sean del mismo tipo
                    tipo = nodo.getHijos2().get(0).getTipo();
                    dime.add(devolverValor(tipo, nodo.getHijos2().get(0)));
                } else {//error porque no son del mismo tipo se detiene el foro y se manda un error
                    Errores error = new Errores("Los tipos de todos los valores no coinciden", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
                    return error;
                }
            } else {//se manda a ejecutar el hijo para que devuleva un valor
                Object resul = ejecucion(nodo.getHijos2().get(0));
                if (resul instanceof Double && tipo.equals("entero")) {
                    dime.add(resul);
                } else if (resul instanceof Character && tipo.equals("caracter")) {
                    dime.add(resul);
                } else {//error porque no son del mismo tipo se detiene el foro y se manda un error
                    Errores error = new Errores("Los tipos de todos los valores no coinciden", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
                    return error;
                }

            }

            conta++;
        }
        ArregloHaskell arreglo = new ArregloHaskell(1, tipo);
        arreglo.setDimension1(dime);
        return arreglo;
    }

    public Object arregloMasDimensiones(Nodo node, String nom) {
        ArrayList<Nodo> hijos = new ArrayList<Nodo>();
        hijos = node.getHijos2().get(0).getHijos2();
        if (nom.equals("E")) {
            hijos = node.getHijos2();
        }
        String tipo = "";
        int conta = 0;
        ArregloHaskell arreglo = new ArregloHaskell(2, tipo);
        for (int i = 0; i < node.numeroHijos; i++) {
            ArrayList dime = new ArrayList();
            for (Nodo nodo : node.getHijos2().get(i).getHijos2()) {//se obtiene los valores de la dimension

                if (conta == 0) {
                    if (nodo.getHijos2().get(0).getTipo().equals("entero") || nodo.getHijos2().get(0).getTipo().equals("cadena") || nodo.getHijos2().get(0).getTipo().equals("caracter") || nodo.getHijos2().get(0).getTipo().equals("decimal")) {
                        tipo = nodo.getHijos2().get(0).getTipo();
                        dime.add(devolverValor(tipo, nodo.getHijos2().get(0)));
                    } else {//se manda a ejecutar el hijo para que devuleva un valor
                        Object resul = ejecucion(nodo.getHijos2().get(0));
                        if (resul instanceof Double) {
                            tipo = "entero";
                        } else if (resul instanceof Character) {
                            tipo = "caracter";
                        }
                        dime.add(resul);
                    }
                } else if (nodo.getHijos2().get(0).getTipo().equals("entero") || nodo.getHijos2().get(0).getTipo().equals("cadena") || nodo.getHijos2().get(0).getTipo().equals("caracter") || nodo.getHijos2().get(0).getTipo().equals("decimal")) {
                    if (tipo.equals(nodo.getHijos2().get(0).getTipo())) {//se verifica que sean del mismo tipo
                        tipo = nodo.getHijos2().get(0).getTipo();
                        dime.add(devolverValor(tipo, nodo.getHijos2().get(0)));
                    } else {//error porque no son del mismo tipo se detiene el foro y se manda un error
                        Errores error = new Errores("El valor de la lista " + nodo.getHijos2().get(0).getNombre() + " ya existe. Ya esta creada.", "Semantico", nodo.getHijos2().get(0).getColumna(), nodo.getHijos2().get(0).getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().getListaErroresHaskell().add(error);
                        return error;
                    }
                } else {//se manda a ejecutar el hijo para que devuleva un valor
                    Object resul = ejecucion(nodo.getHijos2().get(0));
                    if (resul instanceof Double && tipo.equals("entero")) {
                        dime.add(resul);
                    } else if (resul instanceof Character && tipo.equals("caracter")) {
                        dime.add(resul);
                    } else {//error porque no son del mismo tipo se detiene el foro y se manda un error
                        Errores error = new Errores("Los tipos de todos los valores no coinciden", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
                        return error;
                    }
                }

                conta++;
            }
            ArregloHaskell arregloDime = new ArregloHaskell(1, tipo);
            arregloDime.setDimension1(dime);
            arreglo.getDimension1().add(arregloDime);
        }
        arreglo.setTipo(tipo);
        return arreglo;
    }

    public Object sumArreglos(ArrayList list, String tipo) {
        Double suma = 0.0;
        if (tipo.equals("caracter")) {
            for (Object object : list) {
                suma += (Character) object;
            }
            return suma;
        } else {
            for (Object object : list) {
                suma += (Double) object;
            }
            return suma;
        }
    }

    public Object productArreglos(ArrayList list, String tipo) {
        Double producto = 1.0;
        if (tipo.equals("caracter")) {
            for (Object object : list) {
                producto *= (Character) object;
            }
            return producto;
        } else {
            for (Object object : list) {
                producto *= (Double) object;
            }
            return producto;
        }
    }

    public Object reversArreglos(ArregloHaskell arre) {
        ArregloHaskell listaRevers = new ArregloHaskell(arre.getDimension(), arre.getTipo());
        if (arre.getDimension() == 1) {//para una dimension
            for (int i = (arre.getDimension1().size() - 1); i >= 0; i--) {
                listaRevers.getDimension1().add(arre.getDimension1().get(i));
            }
        } else {
            for (int i = arre.getDimension1().size() - 1; i >= 0; i--) {
                ArregloHaskell arregloFor = (ArregloHaskell) (arre.getDimension1().get(i));//se convierte de object a ARREGLO HASKELL para operar
                for (int j = arregloFor.getDimension1().size() - 1; j >= 0; j--) {
                    listaRevers.getDimension1().add(arregloFor.getDimension1().get(j));
                }
            }
        }
        return listaRevers;
    }

    public Object imparesArreglos(ArregloHaskell arre) {
        ArregloHaskell listaImpares = new ArregloHaskell(arre.getDimension(), arre.getTipo());
        if (arre.getDimension() == 1) {//para una dimension
            for (int i = 0; i < (arre.getDimension1().size()); i++) {
                if (arre.getTipo().equals("caracter")) {
                    if (!(((Character) arre.getDimension1().get(i)) % 2 == 0)) {//es impar
                        listaImpares.getDimension1().add(arre.getDimension1().get(i));
                    }
                } else if (!(((Double) arre.getDimension1().get(i)) % 2 == 0)) {//es par
                    listaImpares.getDimension1().add(arre.getDimension1().get(i));
                }
            }
        } else {
            for (int i = 0; i < (arre.getDimension1().size()); i++) {
                ArregloHaskell arregloFor = (ArregloHaskell) (arre.getDimension1().get(i));//se convierte de object a ARREGLO HASKELL para operar
                for (int j = 0; j < arregloFor.getDimension1().size(); j++) {
                    if (arre.getTipo().equals("caracter")) {
                        if (!(((Character) arregloFor.getDimension1().get(j)) % 2 == 0)) {//es impar
                            listaImpares.getDimension1().add(arregloFor.getDimension1().get(j));
                        }
                    } else if (!(((Double) arregloFor.getDimension1().get(j)) % 2 == 0)) {//es par
                        listaImpares.getDimension1().add(arregloFor.getDimension1().get(j));

                    }

                }
            }
        }
        return listaImpares;
    }

    public Object paresArreglos(ArregloHaskell arre) {
        ArregloHaskell listaPares = new ArregloHaskell(arre.getDimension(), arre.getTipo());
        if (arre.getDimension() == 1) {//para una dimension
            for (int i = 0; i < (arre.getDimension1().size()); i++) {
                if (arre.getTipo().equals("caracter")) {
                    if ((((Character) arre.getDimension1().get(i)) % 2 == 0)) {//es par
                        listaPares.getDimension1().add(arre.getDimension1().get(i));
                    }
                } else if ((((Double) arre.getDimension1().get(i)) % 2 == 0)) {//es par
                    listaPares.getDimension1().add(arre.getDimension1().get(i));
                }
            }
        } else {//para mas dimensiones
            for (int i = 0; i < (arre.getDimension1().size()); i++) {
                ArregloHaskell arregloFor = (ArregloHaskell) (arre.getDimension1().get(i));//se convierte de object a ARREGLO HASKELL para operar
                for (int j = 0; j < arregloFor.getDimension1().size(); j++) {
                    if (arre.getTipo().equals("caracter")) {
                        if ((((Character) arregloFor.getDimension1().get(j)) % 2 == 0)) {//es par
                            listaPares.getDimension1().add(arregloFor.getDimension1().get(j));
                        }
                    } else if ((((Double) arregloFor.getDimension1().get(j)) % 2 == 0)) {//es par
                        listaPares.getDimension1().add(arregloFor.getDimension1().get(j));

                    }
                }
            }
        }
        return listaPares;
    }

    public Object ascArreglos(ArregloHaskell arre) {
        ArregloHaskell listaAsc = new ArregloHaskell(arre.getDimension(), arre.getTipo());

        ArrayList<Double> listaAuxNum = new ArrayList<Double>();//listas auxiliares
        ArrayList<Character> listaAuxCar = new ArrayList<Character>();//listas auxiliares de caracteres

        /*
            PARA UNA DIMENSION
         */
        if (arre.getDimension() == 1) {//para una dimension
            /*se llenan las listas para cuando es de una dimension*/
            if (arre.getTipo().equals("caracter")) {//esto va servir para mandar como lista double o caracter porque el consturctor de sort no acepta objetoss
                for (Object object : arre.getDimension1()) {
                    listaAuxCar.add((Character) object);
                }
            } else {

                for (Object object : arre.getDimension1()) {
                    listaAuxNum.add((Double) object);
                }
            }
            /*----------*/
            if (arre.getTipo().equals("caracter")) {

                Collections.sort(listaAuxCar);
                for (Character character : listaAuxCar) {
                    listaAsc.getDimension1().add(character);
                }
            } else {
                Collections.sort(listaAuxNum);
                for (Double double1 : listaAuxNum) {
                    listaAsc.getDimension1().add(double1);//se vuelve a copiar a la lista orinal de lista asc
                }
            }
        } else {//para mas dimensiones
            for (Object object : arre.getDimension1()) {//en esta parte se llenara de primero el arraylist ya sea de caracteres o numeros
                ArregloHaskell arregloFor = (ArregloHaskell) object;
                for (Object object1 : arregloFor.getDimension1()) {
                    if (arregloFor.getTipo().equals("caracter")) {
                        listaAuxCar.add((Character) object1);
                    } else {
                        listaAuxNum.add((Double) object1);
                    }
                }
            }
            //se copian los valores de las listas auxiliares a la orginal
            if (arre.getTipo().equals("caracter")) {
                Collections.sort(listaAuxCar);
                for (Character character : listaAuxCar) {
                    listaAsc.getDimension1().add(character);
                }
            } else {
                Collections.sort(listaAuxNum);
                for (Double double1 : listaAuxNum) {
                    listaAsc.getDimension1().add(double1);
                }
            }
        }
        return listaAsc;
    }

    public Object descArreglos(ArregloHaskell arre) {
        ArregloHaskell listaDesc = new ArregloHaskell(arre.getDimension(), arre.getTipo());

        ArrayList<Double> listaAuxNum = new ArrayList<Double>();//listas auxiliares
        ArrayList<Character> listaAuxCar = new ArrayList<Character>();//listas auxiliares de caracteres

        /*
            PARA UNA DIMENSION
         */
        if (arre.getDimension() == 1) {//para una dimension
            /*en esta parte se copian los valoress a las llistas auxliares*/

            if (arre.getTipo().equals("caracter")) {//esto va servir para mandar como lista double o caracter porque el consturctor de sort no acepta objetoss
                for (Object object : arre.getDimension1()) {
                    listaAuxCar.add((Character) object);
                }
            } else {

                for (Object object : arre.getDimension1()) {
                    listaAuxNum.add((Double) object);
                }
            }
            /*--------------*/
            if (arre.getTipo().equals("caracter")) {

                Collections.sort(listaAuxCar, Collections.reverseOrder());
                for (Character character : listaAuxCar) {
                    listaDesc.getDimension1().add(character);
                }
            } else {
                Collections.sort(listaAuxNum, Collections.reverseOrder());
                for (Double double1 : listaAuxNum) {
                    listaDesc.getDimension1().add(double1);
                }
            }
        } else {//para mas dimensiones
            for (Object object : arre.getDimension1()) {//en esta parte se llenara de primero el arraylist ya sea de caracteres o numeros
                ArregloHaskell arregloFor = (ArregloHaskell) object;
                for (Object object1 : arregloFor.getDimension1()) {
                    if (arregloFor.getTipo().equals("caracter")) {
                        listaAuxCar.add((Character) object1);
                    } else {
                        listaAuxNum.add((Double) object1);
                    }
                }
            }
            //se copian los valores de las listas auxiliares a la orginal
            if (arre.getTipo().equals("caracter")) {
                Collections.sort(listaAuxCar, Collections.reverseOrder());
                for (Character character : listaAuxCar) {
                    listaDesc.getDimension1().add(character);
                }
            } else {
                Collections.sort(listaAuxNum, Collections.reverseOrder());
                for (Double double1 : listaAuxNum) {
                    listaDesc.getDimension1().add(double1);
                }
            }
        }
        return listaDesc;
    }

    public Object obtenerIndice(ArregloHaskell arre, int indice, int indice2) {
        if (indice >= arre.getDimension1().size() || indice < 0) {
            Errores error = new Errores("El indice excede el tamaño del lista ", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
            return error;
        }
        if (arre.getDimension() == 1) {
                return arre.getDimension1().get(indice);
        } else {
            for (Object object : arre.getDimension1()) {
                if (indice2 > ((ArregloHaskell) object).getDimension1().size() || indice < 0) {
                    Errores error = new Errores("El indice2 excede el tamaño del lista ", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
                    return error;
                }
            }
            return ((ArregloHaskell) arre.getDimension1().get(indice)).getDimension1().get(indice2);

        }
    }

    public Object concatenar(ArregloHaskell arre1, ArregloHaskell arre2) {
        //se verifica que los dos arreglos sean del mismo tipo

        if (arre1.getDimension() == arre1.getDimension()) {
            //se verifica que los dos arreglos tengan la misma dimension
            ArregloHaskell listaConca = new ArregloHaskell(arre1.getDimension(), arre1.getTipo());
            if (arre1.getDimension() == 1) {
                if (arre1.getTipo().equals(arre2.getTipo())) {
                    //se agrega de primero los del arreglo 1
                    arre1.getDimension1().stream().forEach((object) -> {
                        listaConca.getDimension1().add(object);
                    });
                    //se agrega los del arreglo2
                    arre2.getDimension1().stream().forEach((object) -> {
                        listaConca.getDimension1().add(object);
                    });
                } else {
                    Errores error = new Errores("Los tipos de las listas son diferentes, imposible de operar. ", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
                    return error;

                }
            } else//mas de una dimension
            {
                if (((ArregloHaskell) arre1.getDimension1().get(0)).getTipo().equals(((ArregloHaskell) arre2.getDimension1().get(0)).getTipo())) {

                    /*PARA EL PRIMER ARREGLO*/
                    arre1.getDimension1().stream().forEach((object) -> {
                        listaConca.getDimension1().add(object);
                    });
                    /*PARA EL SEGUNDO ARREGLO SE AGREGAN ALA LISTA*/
                    arre2.getDimension1().stream().forEach((object) -> {
                        listaConca.getDimension1().add(object);
                    });

                }
            }
            return listaConca;
        } else {
            Errores error = new Errores("La dimension de los arreglos son diferentes ", "Semantico", 0, 0, TablaSimbolosHaskell.getInstancia().getArchivo());
            return error;
        }
    }

    public Object buscarValorVariable(Nodo id) {
        Variable var = TablaSimbolosHaskell.getInstancia().buscarLocalmente(id.getValor());
        if (var == null) {
            var = TablaSimbolosHaskell.getInstancia().buscarGlobalmente(id.getValor());
            if (var != null) {
                return var;
            }
        } else {
            return var;
        }
        Errores error = new Errores("La variable" + id.getNombre() + " no ha sido definida.", "Semantico", id.getColumna(), id.getFila(), TablaSimbolosHaskell.getInstancia().getArchivo());
        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
        return error;
    }

    public String imprimirLista(ArregloHaskell arre) {
        String lista = "";
        if (arre.getDimension() == 1) {
            lista = arre.getDimension1().stream().map((object) -> object.toString()).reduce(lista, String::concat);
        } else if (arre.getDimension() == 2) {
            for (Object object : arre.getDimension1()) {
                ArregloHaskell arreFor = (ArregloHaskell) object;
                for (Object object1 : arreFor.getDimension1()) {
                    lista += object1.toString();
                }
            }
        }
        return lista;
    }
}
