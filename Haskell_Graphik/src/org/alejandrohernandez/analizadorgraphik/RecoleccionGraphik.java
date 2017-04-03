/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.analizadorgraphik;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.alejandrohernandez.beans.Als;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.MetodoG;
import org.alejandrohernandez.beans.Nodo;
import org.alejandrohernandez.beans.VariableG;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.ManejadorMetodos;
import org.alejandrohernandez.manejadores.TablaSimbolosGraphik;

/**
 *
 * @author oscar
 */
public class RecoleccionGraphik {

    public Object recoleccion(Nodo node) {
        Object result = new Errores("Error de compilacion", "semantico", 0, 0, "");
        switch (node.getTipo()) {
            case "INICIO": {
                recoleccion(node.getHijos2().get(0));
                //se hace el primer recorrido de los als para llenar la lista de variables y metodos de los ALS guardados
                for (String key : TablaSimbolosGraphik.getInstancia().getListaAls().keySet()) {
                    Als als = TablaSimbolosGraphik.getInstancia().getListaAls().get(key);
                    TablaSimbolosGraphik.getInstancia().setAlsActivo(als);
                    recoleccion(als.getCuerpoAls());

                }
                TablaSimbolosGraphik tabla = TablaSimbolosGraphik.getInstancia();
                TablaSimbolosGraphik.getInstancia().setAlsActivo(null);
                //se procede hacer el segundo recorrido para buscar la herencia de cada als y copiar sus valores 
                for (String key : TablaSimbolosGraphik.getInstancia().getListaAls().keySet()) {
                    Als als = TablaSimbolosGraphik.getInstancia().getListaAls().get(key);
                    TablaSimbolosGraphik.getInstancia().setAlsActivo(als);
                    resolverHerencia(als);
                }
                //se procede hacer el tercer recorrido para buscar las variables y asignarles su valor
                for (String key : TablaSimbolosGraphik.getInstancia().getListaAls().keySet()) {
                    Als als = TablaSimbolosGraphik.getInstancia().getListaAls().get(key);
                    TablaSimbolosGraphik.getInstancia().setAlsActivo(als);
                    asignarValores(als);
                }

                //se procede hacer un cuarto recorrido para las instancias que aun no se han hecho
                for (String key : TablaSimbolosGraphik.getInstancia().getListaAls().keySet()) {
                    Als als = TablaSimbolosGraphik.getInstancia().getListaAls().get(key);
                    TablaSimbolosGraphik.getInstancia().setAlsActivo(als);
                    hacerInstancias(als);
                }
                break;
            }
            case "I": {
                if (node.numeroHijos == 1) {//SIN IMPORTACIONES
                    recoleccion(node.hijos2.get(0)).toString();
                } else if (node.numeroHijos == 2) {
                    //Importaciones 
                    recoleccion(node.hijos2.get(0));
                    node.getHijos2().get(0).hijos2.stream().forEach((nodo1) -> {
                        nodo1.hijos2.stream().forEach((nodo2) -> {
                            if (nodo2.getNombre().equals("importar")) {//importar archivo de graphik

                            } else if (nodo2.getNombre().equals("incluir")) {//incluir archivos HASKELL

                            }
                        });
                    });

                    recoleccion(node.hijos2.get(1));
                }
                break;
            }
            case "CUERPOGK": {
                if (node.numeroHijos == 1) {
                    recoleccion(node.hijos2.get(0));
                } else if (node.numeroHijos == 2) {
                    recoleccion(node.hijos2.get(0));
                    recoleccion(node.hijos2.get(1));
                }
                break;
            }
            case "CUERPOALSRECUR": {
                if (node.numeroHijos == 1) {
                    recoleccion(node.hijos2.get(0));
                } else if (node.numeroHijos == 2) {
                    recoleccion(node.hijos2.get(0));
                    recoleccion(node.hijos2.get(1));
                }
                break;
            }
            case "CUERPOALS": {
                recoleccion(node.hijos2.get(0));
                break;
            }
            case "VARIABLESGLO": {
                boolean sePudo = true;
                String id = "";
                if (node.numeroHijos == 2) {//declaracion SIN visibilidad
                    String tipo = node.hijos2.get(0).getNombre();
                    id = node.hijos2.get(1).getNombre();
                    VariableG varGlobalNueva = new VariableG(id, tipo, "sin valor");
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                } else if (node.numeroHijos == 3) {
                    if (node.getHijos2().get(2).getNombre().equals("E")) {//declaracion y asignacion
                        String tipo = node.hijos2.get(0).getNombre();
                        id = node.hijos2.get(1).getNombre();
                        Object valor = node.hijos2.get(2);
                        VariableG varGlobalNueva = new VariableG(id, tipo, valor);
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                        return valor;
                    } else {//declaracion CON visibilidad
                        String tipo = node.hijos2.get(0).getNombre();
                        id = node.hijos2.get(1).getNombre();
                        String visibilidad = node.hijos2.get(2).hijos2.get(0).getNombre();
                        VariableG varGlobalNueva = new VariableG(id, tipo, "sin valor", visibilidad);
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                    }

                } else if (node.numeroHijos == 4) {//declaracion con asignacion y visibilidad

                    String tipo = node.hijos2.get(0).getNombre();
                    id = node.hijos2.get(1).getNombre();
                    String visibilidad = node.hijos2.get(2).hijos2.get(0).getNombre();
                    Object valor = node.hijos2.get(3);
                    VariableG varGlobalNueva = new VariableG(id, tipo, valor, visibilidad);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                    return valor;
                }
                if (!sePudo) {
                    Errores error = new Errores("La variable " + id + " no se pudo guardar. Ya esta creada.", "Semantico", node.getHijos2().get(1).getColumna(), node.getHijos2().get(1).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                }
                result = true;
                break;
            }
            case "DEFALS": {
                boolean sePudo;
                String id = "";
                if (node.numeroHijos == 2) {//als y su cuerpo
                    id = node.getHijos2().get(0).getValor();
                    Object cuerpoAls = node.getHijos2().get(1);
                    Als nuevoAls = new Als(id, "publico", (Nodo) cuerpoAls, "", true);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarAls(nuevoAls);
                } else if (node.numeroHijos == 3) {
                    if (node.getHijos2().get(1).getNombre().equals("VISIBILIDAD")) {//asl con visibilidad y sin herencia
                        id = node.getHijos2().get(0).getValor();
                        String visiblidad = node.getHijos2().get(1).getHijos2().get(0).getValor();
                        Object cuerpoAls = node.getHijos2().get(2);
                        Als nuevoAls = new Als(id, visiblidad, (Nodo) cuerpoAls, "", true);
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarAls(nuevoAls);
                    } else {//als con herencia SIN visibilidad
                        id = node.getHijos2().get(0).getValor();
                        String hereda = node.getHijos2().get(1).getValor();
                        Object cuerpoAls = node.getHijos2().get(2);
                        Als nuevoAls = new Als(id, "publico", (Nodo) cuerpoAls, hereda, false);
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarAls(nuevoAls);
                    }

                } else if (node.numeroHijos == 4) {//als con visibilidad y con herencia
                    id = node.getHijos2().get(0).getValor();
                    String visiblidad = node.getHijos2().get(1).getHijos2().get(0).getValor();
                    String hereda = node.getHijos2().get(2).getValor();
                    Object cuerpoAls = node.getHijos2().get(3);
                    Als nuevoAls = new Als(id, visiblidad, (Nodo) cuerpoAls, hereda, false);
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarAls(nuevoAls);
                }

                break;
            }
            case "FUNCIONES": {
                Errores error = null;
                if (node.numeroHijos == 2) {
                    if (node.getHijos2().get(0).getValor().equals("inicio")) {//METODO PRINCIPAL
                        // String tipo = node.getHijos2().get(0).getValor();//vacio
                        String id = node.getHijos2().get(0).getValor();
                        Nodo instrucciones = node.getHijos2().get(1);
                        MetodoG funNueva = new MetodoG(id, "vacio", null, instrucciones);
                        // error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                        if (error == null) {
                            TablaSimbolosGraphik.getInstancia().getAlsActivo().setPrincipal(funNueva.getInstrucciones());
                        }
                    }

                } else if (node.numeroHijos == 3) {
                    if (node.getHijos2().get(0).getNombre().equals("TIPO")) {//TIPO sin visibilidad y sin parametros
                        String tipo = node.getHijos2().get(0).getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        Nodo instrucciones = node.getHijos2().get(2);
                        MetodoG funNueva = new MetodoG(id, tipo, null, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                    } else {//vacio sin visibilidad y sin parametros
                        String tipo = node.getHijos2().get(0).getValor();//vacio
                        String id = node.getHijos2().get(1).getValor();
                        Nodo instrucciones = node.getHijos2().get(2);
                        MetodoG funNueva = new MetodoG(id, tipo, null, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                    }

                } else if (node.numeroHijos == 4) {
                    if (node.getHijos2().get(0).getNombre().equals("TIPO")) {//TIPO sin visibilidad 
                        String tipo = node.getHijos2().get(0).getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        ArrayList<VariableG> listaPar = (ArrayList<VariableG>) recoleccion(node.getHijos2().get(2));
                        Nodo instrucciones = node.getHijos2().get(3);
                        MetodoG funNueva = new MetodoG(id, tipo, listaPar, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));

                    } else if (node.getHijos2().get(0).getValor().equals("vacio")) {// vacio Con visibilidad
                        String tipo = node.getHijos2().get(0).getValor();//vacio
                        String id = node.getHijos2().get(1).getValor();
                        String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                        Nodo instrucciones = node.getHijos2().get(3);
                        MetodoG funNueva = new MetodoG(id, tipo, null, visibilidad, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                    } else if (node.getHijos2().get(2).getValor().equals("VISIBILIDAD")) {//METODO sin parametros, pero con visibilidad
                        if (node.getHijos2().get(0).getNombre().equals("TIPO")) {//tipo
                            String tipo = node.getHijos2().get(0).getHijos2().get(0).getValor();
                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            Nodo instrucciones = node.getHijos2().get(3);
                            MetodoG funNueva = new MetodoG(id, tipo, null, visibilidad, instrucciones);
                            error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                        } else {//VACIO
                            String tipo = node.getHijos2().get(0).getValor();//vacio
                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            Nodo instrucciones = node.getHijos2().get(3);
                            MetodoG funNueva = new MetodoG(id, tipo, null, visibilidad, instrucciones);
                            error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                        }
                    }

                } else if (node.numeroHijos == 5) {//tipo vacion con visibilidad y con parametros
                    if (node.getHijos2().get(0).getNombre().equals("TIPO")) {//TIPO con visibilidad
                        String tipo = node.getHijos2().get(0).getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        ArrayList<VariableG> listaPar = (ArrayList<VariableG>) recoleccion(node.getHijos2().get(2));
                        String visibilidad = node.getHijos2().get(3).getHijos2().get(0).getValor();
                        Nodo instrucciones = node.getHijos2().get(4);
                        MetodoG funNueva = new MetodoG(id, tipo, listaPar, visibilidad, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                    } else {// vacio con visibilidad
                        String tipo = node.getHijos2().get(0).getValor();//vacio
                        String id = node.getHijos2().get(1).getValor();
                        ArrayList<VariableG> listaPar = (ArrayList<VariableG>) recoleccion(node.getHijos2().get(2));
                        String visibilidad = node.getHijos2().get(3).getHijos2().get(0).getValor();
                        Nodo instrucciones = node.getHijos2().get(4);
                        MetodoG funNueva = new MetodoG(id, tipo, listaPar, visibilidad, instrucciones);
                        error = TablaSimbolosGraphik.getInstancia().agregarMetodoG(funNueva, node.getHijos2().get(1));
                    }
                }
                if (!(error == null)) {
                    return error;
                }
                result = true;
                break;
            }
            case "LISTAPAR": {
                ArrayList<VariableG> lista = new ArrayList<VariableG>();
                for (Nodo nodo : node.getHijos2()) {
                    VariableG nuevaVar = new VariableG(nodo.getHijos2().get(1).getValor(), nodo.getHijos2().get(0).getValor(), "sin valor");
                    lista.add(nuevaVar);
                }
                return lista;
            }
            case "ARREGLOS": {
                boolean sePudo = false;
                if (node.numeroHijos == 3) {//solo declaracion sin visiblidad
                    String tipo = node.getHijos2().get(0).getValor();
                    String id = node.getHijos2().get(1).getValor();
                    VariableG varGlobalNueva = new VariableG(id, tipo, "arreglo");
                    varGlobalNueva.setDimensiones(node.getHijos2().get(2));
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                } else if (node.numeroHijos == 4) {
                    if (node.getHijos2().get(3).getNombre().equals("VISIBILIDAD")) {//Solo declaracion con  visibilidad
                        String tipo = node.getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        String visibilidad = node.getHijos2().get(3).getHijos2().get(0).getValor();
                        VariableG varGlobalNueva = new VariableG(id, tipo, "arreglo", visibilidad);
                        varGlobalNueva.setDimensiones(node.getHijos2().get(2));
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                    } else {//declaracion mas asignacion sin visibilidad
                        String tipo = node.getHijos2().get(0).getValor();
                        String id = node.getHijos2().get(1).getValor();
                        VariableG varGlobalNueva = new VariableG(id, tipo, node.getHijos2().get(3));
                        varGlobalNueva.setDimensiones(node.getHijos2().get(2));
                        /*Object asignacion = asignarValoresArreglo(varGlobalNueva, node.getHijos2().get(3)); //en esta parte se asignara los valores
                        if (asignacion instanceof Boolean) {
                            if ((Boolean) asignacion) {
                                sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                            }
                        }*/
                        sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                    }

                } else if (node.numeroHijos == 5) {//asignacion mas asignacion y visibilidad
                    String tipo = node.getHijos2().get(0).getValor();
                    String id = node.getHijos2().get(1).getValor();
                    String visibilidad = node.getHijos2().get(3).getHijos2().get(0).getValor();
                    VariableG varGlobalNueva = new VariableG(id, tipo, node.getHijos2().get(4), visibilidad);
                    varGlobalNueva.setDimensiones(node.getHijos2().get(2));
                    sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(varGlobalNueva);
                }
                break;
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
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(nuevaVar);

                        } else {

                            sePudoObj = false;
                        }

                    } else {
                        sePudoObj = false;
                    }
                } else if (node.numeroHijos == 3) {//se declara el objeto con visibilidad
                    String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {

                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, "sin valor", visibilidad);
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(nuevaVar);
                        } else {

                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }

                } else if (node.numeroHijos == 4) {//instancia SIN visibilidad
                    String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {
                            String id = node.getHijos2().get(1).getValor();
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, "instancia");
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(nuevaVar);
                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }
                } else if (node.numeroHijos == 5) {//instancia con visibilidad
                    String tipoObjeto = node.getHijos2().get(0).getValor();
                    Als tipoObjAls = TablaSimbolosGraphik.getInstancia().buscarAls(tipoObjeto);
                    if (tipoObjAls != null) {
                        if (tipoObjAls.getVisibilidad().equals("publico")) {
                            String id = node.getHijos2().get(1).getValor();
                            String visibilidad = node.getHijos2().get(2).getHijos2().get(0).getValor();
                            VariableG nuevaVar = new VariableG(id, tipoObjeto, "instancia", visibilidad);
                            sePudo = TablaSimbolosGraphik.getInstancia().agregarVarGlobal(nuevaVar);
                            sePudoObj = false;
                        }
                    } else {
                        sePudoObj = false;
                    }
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
                return operaciones(node);
            }
            default:
                result = node;

        }
        return result;
    }

    public Object operaciones(Nodo node) {
        Object result = EjecucionGraphik.errorG;
        if (node.numeroHijos == 1) {
            Object re = recoleccion(node.getHijos2().get(0));
            if (re instanceof Nodo) {
                Nodo nodo = (Nodo) re;

                if (nodo.getTipo().equals("id")) {
                    //se busca el id
                    Object var = buscarValorVariable((Nodo) re);//se busca la variable
                    if (var instanceof VariableG) {//se verifica si se encuentra
                        VariableG var2 = ((VariableG) var);
                        Object valor = var2.getValor();
                        if (!(valor instanceof Errores)) {//se verifica que sea de tipo lista
                            return valor;
                        } else {
                            Errores error = new Errores("La variable" + var2.getNombre() + " no es de tipo lista", "Semantico",
                                    ((Nodo) re).getColumna(), ((Nodo) re).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                            ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                            return error;
                        }
                    }
                } else if (nodo.getTipo().equals("LLAMAROBJ")) {
                    Object var = ManejadorMetodos.getInstancia().accederObj(node.getHijos2().get(0));//se busca la variable
                    if (var instanceof VariableG) {//se verifica si se encuentra
                        VariableG var2 = ((VariableG) var);
                        Object valor = var2.getValor();
                        if (!(valor instanceof Errores)) {//se verifica que sea de tipo lista
                            return valor;
                        } else {
                            Errores error = new Errores("La variable" + var2.getNombre() + " no existe", "Semantico",
                                    ((Nodo) re).getColumna(), ((Nodo) re).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                            ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                            return error;
                        }
                    }
                }
                return devolverValor(nodo.tipo, nodo);
            }
            result = re;
        } else if (node.numeroHijos == 2) {
            if (node.getHijos2().get(0).getNombre().equals("-")) {//el resultado se multiplica por -1
                result = operaciones(node.getHijos2().get(1));
                if (!(result instanceof Errores)) {
                    if (result instanceof Boolean) {
                        if ((Boolean) result) {
                            result = (long) (-1);
                        }
                        result = (long) 0;
                    } else if (result instanceof Character) {
                        char caracterOperando1 = result.toString().charAt(0);
                        long codigoAscii = caracterOperando1;
                        result = (-1) * codigoAscii;
                    } else if (result instanceof Double) {
                        result = (-1.0) * (double) result;
                    } else if (result instanceof Long) {
                        result = (long) result * (-1);
                    } else if (result instanceof String) {
                        Errores error = new Errores("No se puede multiplicar por -1 una cadena de caracteres", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                        return error;
                    }
                }
            } else if (node.getHijos2().get(0).getNombre().equals("!")) {
                result = operaciones(node.getHijos2().get(1));
                if (!(result instanceof Errores)) {
                    if (result instanceof Boolean) {
                        result = !((Boolean) result);
                    } else {
                        Errores error = new Errores("La negacion solo acepta valores booleanos", "Semantico", node.getHijos2().get(0).getColumna(), node.getHijos2().get(0).getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
                        return error;
                    }
                }
            } else if (node.getHijos2().get(1).getValor().equals("++")) {//incremento de una variable
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
            } else if (node.getHijos2().get(1).getValor().equals("DIME")) {
                Object var1 = ManejadorMetodos.getInstancia().accederObj(node.getHijos2().get(0)); //va a buscar la variable, y la va devolver
                if (var1 instanceof VariableG)//comparo si la variable existe o si el valor es diferente a error
                {
                    if (((VariableG) var1).getValores() != null) {
                        ArrayList<Integer> dimensiones = tamaniosArreglo(node.getHijos2().get(1));
                        int posicion = ManejadorMetodos.getInstancia().linealizar(dimensiones, ((VariableG) var1).getTamanios());
                        if (posicion == -1) {
                            return agregarError("Fuera de rango", "Semantico", 0, 0);
                        }
                        result = ((VariableG) var1).getValores().get(posicion);
                    }
                }
            }
        } else if (node.numeroHijos == 3) {
            Object operando1;
            Object operador;
            Object operando2;
            if (node.hijos2.get(0).getNombre().equals("E")) {//operacion normal
                operando1 = operaciones(node.getHijos2().get(0));
                operador = recoleccion(node.getHijos2().get(1));
                operando2 = operaciones(node.getHijos2().get(2));
                if (!(operando1 instanceof Errores) && !(operando2 instanceof Errores)) {
                    result = expresiones(operando1, ((Nodo) operador).getValor(), operando2);
                }
            } else if (node.getHijos2().get(0).getNombre().equals("(")) {
                result = operaciones(node.getHijos2().get(1));
            }
        }
        return result;
    }

    public Object devolverValor(String tipo, Nodo nodo) {
        if (tipo.equals("decimal")) {
            return Double.parseDouble(nodo.getValor());
        } else if (tipo.equals("caracter")) {
            char cha = nodo.getValor().replaceAll("'", "").charAt(0);
            return cha;
        } else if (tipo.equals("entero")) {
            return Long.parseLong(nodo.getValor());
        } else if (tipo.equals("bool")) {
            if (nodo.getNombre().equals("verdadero")) {
                return true;
            }
            return false;
        }
        String valor = nodo.getValor().replaceAll("\"", "").replaceAll("'", "");
        return valor;
    }

    public Object buscarValorVariable(Nodo id) {
        VariableG var = TablaSimbolosGraphik.getInstancia().buscarLocalmente(id.getValor());
        if (var == null) {
            var = TablaSimbolosGraphik.getInstancia().buscarGlobalmente(id.getValor());
            if (var != null) {
                return var;
            }
        } else {
            return var;
        }
        Errores error = new Errores("La variable" + id.getNombre() + " no ha sido definida.", "Semantico", id.getColumna(), id.getFila(), TablaSimbolosGraphik.getInstancia().getArchivo());
        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
        return error;
    }

    /*
    
    METODOS DEL NODO E
    
     */
    public Object expresiones(Object operando1, Object operador, Object operando2) {
        Object result = null;
        /*
                -------------------------------PARA STRINGS-------------------------
         */
        if (operando1 instanceof String || operando2 instanceof String)//Se hace la verificacion si es una concatenacion, se pregunta si alguno de los operandos es STRINGs
        {
            if (operador.toString().equals("+")) {
                //concatenacion
                String resultado = operando1.toString() + operando2.toString();
                return resultado;
            } else {

                /*
                        ************************ERRROR******************
                        *solo se puede usar el signo '+'
                 */
                // return null;
            }
        }
        if (operador.equals("<") || operador.equals(">") || operador.equals("<=") || operador.equals(">=") || operador.equals("==") || operador.equals("!=")) {
            result = operacionRelacional(operando1, operador, operando2);
        } else if (operador.equals("&&") || operador.equals("||") || operador.equals("|&") || operador.equals("!")) {
            result = operacionesLogicas(operando1, operador, operando2);
        } else {
            result = operacionAritmetica(operando1, operador, operando2);
        }

        return result;
    }

    //########################operacion ARITMETICA
    private Object operacionAritmetica(Object ope1, Object ope, Object ope2) {
        Object resultado = null;

        //Todos se castean a double o bool(1 o 0) dependiendo si es suma o multi si se hace una operacion bool
        if (ope != null) {
            /*
              * 
              * 
              * 
              * -----------------PARA SUMA--------   
              *
              *
              *
             */
            if (ope.equals("+"))/* -----------------PARA SUMA--------   */ {
                if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool se hace un OR
                {
                    resultado = (Boolean) ope1 || (Boolean) ope2;

                } else if (ope1 instanceof Boolean && ope2 instanceof Double) { //Si op1 es bool (1 o 0) y op2 es double se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope1 == true) {
                        resultado = (double) (1 + (double) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (double) ope2;
                    }

                } else if (ope1 instanceof Double && ope2 instanceof Boolean) {//Si op2 es bool (1 o 0) y op1 es double se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope2 == true) {
                        resultado = (double) (1 + (double) ope1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (double) ope1;
                    }
                } else if (ope1 instanceof Boolean && ope2 instanceof Long) { //Si op1 es bool (1 o 0) y op2 es long se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope1 == true) {
                        resultado = (long) (1 + (long) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (long) ope2;
                    }

                } else if (ope1 instanceof Long && ope2 instanceof Boolean) {//Si op2 es bool (1 o 0) y op1 es long se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope2 == true) {
                        resultado = (long) (1 + (long) ope1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (long) ope1;
                    }
                } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Double || ope1 instanceof Double && ope2 instanceof Long) {//si los dos operadores son decimales
                    if (ope1 instanceof Long) {
                        long a = (long) ope1;
                        ope1 = (double) a;
                    }
                    if (ope2 instanceof Long) {
                        long a = (long) ope2;
                        ope2 = (double) a;
                    }
                    resultado = (double) ope1 + (double) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Long) {//si los dos operadores son enteros
                    resultado = (long) ope1 + (long) ope2;
                } else if (ope1 instanceof Character && ope2 instanceof Character) {//si los dos operadores son CARACTERES se devuelve una concatenacion
                    resultado = ope1.toString() + ope2.toString();
                } else if (ope1 instanceof Character && ope2 instanceof Double) {//si ope2 es un double el ope1 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope1.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = codigoAscii + (double) ope2;
                } else if (ope1 instanceof Double && ope2 instanceof Character) {//si ope1 es un double el ope2 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope2.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado =  (double) ope1+ codigoAscii;
                } else if (ope1 instanceof Character && ope2 instanceof Long) {//si ope2 es un long el ope1 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope1.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = codigoAscii + (long) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Character) {//si ope1 es un long el ope2 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope2.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado =  (long) ope1+codigoAscii ;
                } else {
                    /*
                            -----------------------------ERROR-------------------
                         * LOS TIPOS DE DATOS NO SON COMPATIBLES, IMPOSIBLE HACER LA OPERACION
                     */
                }

            } /*
                 * 
                 * 
                 * 
                 * -----------------PARA RESTA--------   
                 *
                 *
                 *
             */ else if (ope.equals("-")) {
                if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool es un error
                {
                    /*
                            -----------------------ERROR----------------
                         * CON EL SIGNO MENOS NO SE PUEDE HACER OPERACIONES BOOLEANEAS
                     */
                    Errores nuevoError = new Errores("Con la resta no se puede hacer op. booleanas", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                } else if (ope1 instanceof Boolean && ope2 instanceof Double) //Si op1 es bool (1 o 0) y op2 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope1 == true) {
                        resultado = (double) (1 - (double) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (double) (0 - (double) ope2);
                    }

                } else if (ope1 instanceof Double && ope2 instanceof Boolean)//Si op2 es bool (1 o 0) y op1 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope2 == true) {
                        resultado = (double) ((double) ope1 - 1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (double) ((double) ope1 - 0);
                    }
                } else if (ope1 instanceof Boolean && ope2 instanceof Long) { //Si op1 es bool (1 o 0) y op2 es long se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope1 == true) {
                        resultado = (long) (1 - (long) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (long) (0 - (long) ope2);
                    }

                } else if (ope1 instanceof Long && ope2 instanceof Boolean) {//Si op2 es bool (1 o 0) y op1 es long se hace un suma ((1 o 0)+num)

                    if ((Boolean) ope2 == true) {
                        resultado = (long) (1 - (long) ope1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (long) ((long) ope1 - 0);
                    }
                } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Double || ope1 instanceof Double && ope2 instanceof Long) {//si los dos operadores son decimales
                    if (ope1 instanceof Long) {
                        long a = (long) ope1;
                        ope1 = (double) a;
                    }
                    if (ope2 instanceof Long) {
                        long a = (long) ope2;
                        ope2 = (double) a;
                    }
                    resultado = (double) ope1 - (double) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Long) {//si los dos operadores son enteros
                    resultado = (long) ope1 - (long) ope2;
                } else if (ope1 instanceof Character && ope2 instanceof Character) {//si los dos operadores son CARACTERES se devuelve una concatenacion
                    Errores error = new Errores("La operacion RESTA entre dos caracteres no es posible", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    //ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                } else if (ope1 instanceof Character && ope2 instanceof Double) {//si ope2 es un double el ope1 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope1.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = codigoAscii - (double) ope2;
                } else if (ope1 instanceof Double && ope2 instanceof Character) {//si ope1 es un double el ope2 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope2.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = (double) ope1 - codigoAscii;
                } else if (ope1 instanceof Character && ope2 instanceof Long) {//si ope2 es un long el ope1 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope1.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = codigoAscii - (long) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Character) {//si ope1 es un long el ope2 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope2.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = (long) ope1 - codigoAscii;
                } else {
                    /*
                            -----------------------------ERROR-------------------
                         * LOS TIPOS DE DATOS NO SON COMPATIBLES, IMPOSIBLE HACER LA OPERACION
                     */
                    Errores nuevoError = new Errores("Tipos de datos incompatibles, imposible hacer la resta", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                }
            } /*
               * 
               * 
               * 
               * -----------------PARA MULTIPLICACION--------   
               *
               *
               *
             */ else if (ope.equals("*")) {
                if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool es un error
                {
                    resultado = (Boolean) ope1 && (Boolean) ope2;
                } else if (ope1 instanceof Boolean && ope2 instanceof Double) //Si op1 es bool (1 o 0) y op2 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope1 == true) {
                        resultado = (double) (1 * (double) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (double) (0 * (double) ope2);
                    }

                } else if (ope1 instanceof Double && ope2 instanceof Boolean)//Si op2 es bool (1 o 0) y op1 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope2 == true) {
                        resultado = (double) ((double) ope1 * 1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (double) ((double) ope1 * 0);
                    }
                } else if (ope1 instanceof Boolean && ope2 instanceof Long) { //Si op1 es bool (1 o 0) y op2 es long se hace una multi ((1 o 0)*num)

                    if ((Boolean) ope1 == true) {
                        resultado = (long) (1 * (long) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (long) (0 * (long) ope2);
                    }

                } else if (ope1 instanceof Long && ope2 instanceof Boolean) {//Si op2 es bool (1 o 0) y op1 es long se hace un multi ((1 o 0)*num)

                    if ((Boolean) ope2 == true) {
                        resultado = (long) (1 * (long) ope1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = (long) ((long) ope1 * 0);
                    }
                } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Double || ope1 instanceof Double && ope2 instanceof Long) {//si los dos operadores son decimales
                    if (ope1 instanceof Long) {
                        long a = (long) ope1;
                        ope1 = (double) a;
                    }
                    if (ope2 instanceof Long) {
                        long a = (long) ope2;
                        ope2 = (double) a;
                    }
                    resultado = (double) ope1 * (double) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Long) {//si los dos operadores son enteros
                    resultado = (long) ope1 * (long) ope2;
                } else if (ope1 instanceof Character && ope2 instanceof Character) {//si los dos operadores son CARACTERES se devuelve una concatenacion
                    Errores error = new Errores("La operacion MULTIPLICACION entre dos caracteres no es posible", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    //ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                } else if (ope1 instanceof Character && ope2 instanceof Double) {//si ope2 es un double el ope1 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope1.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = codigoAscii * (double) ope2;
                } else if (ope1 instanceof Double && ope2 instanceof Character) {//si ope1 es un double el ope2 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope2.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = codigoAscii * (double) ope1;
                } else if (ope1 instanceof Character && ope2 instanceof Long) {//si ope2 es un long el ope1 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope1.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = codigoAscii * (long) ope2;
                } else if (ope1 instanceof Long && ope2 instanceof Character) {//si ope1 es un long el ope2 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope2.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = codigoAscii * (long) ope1;
                } else {
                    /*
                            -----------------------------ERROR-------------------
                         * LOS TIPOS DE DATOS NO SON COMPATIBLES, IMPOSIBLE HACER LA OPERACION
                     */
                    Errores nuevoError = new Errores("Tipos de datos incompatibles, imposible hacer la multiplicacion", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                }
            } /*
              * 
              * 
              * 
              * -----------------PARA DIVISION-----------------(Tomar en cuenta cuando se divide por 0)   
              *
              *
              *
             */ else if (ope.equals("/")) {
                if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool es un error
                {
                    /*
                           -----------------------ERROR----------------
                        * CON EL SIGNO MENOS NO SE PUEDE HACER OPERACIONES BOOLEANEAS
                     */
                    Errores nuevoError = new Errores("Con la division no se puede hacer op. booleanas ", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                } else if (ope1 instanceof Boolean && ope2 instanceof Double || ope1 instanceof Boolean && ope2 instanceof Long) //Si op1 es bool (1 o 0) y op2 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope1 == true) {
                        if ((double) ope2 == 0)//ERROR PORQUE NO SE PUEDE DIVIDIR DENTRO DE 0
                        {
                            Errores nuevoError = new Errores("No se puede dividir dentro de cero.", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                            return nuevoError;
                        }
                        resultado = (double) (1 / (double) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = (double) (0 / (double) ope2);
                    }

                } else if (ope1 instanceof Double && ope2 instanceof Boolean || ope1 instanceof Long && ope2 instanceof Boolean)//Si op2 es bool (1 o 0) y op1 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope2 == true) {
                        resultado = (double) ((double) ope1 / 1);
                    } else if ((Boolean) ope2 == false) {

                        Errores nuevoError = new Errores("No se puede dividir dentro de cero.", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                        return nuevoError;

                    }
                } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Long || ope1 instanceof Long && ope2 instanceof Double || ope1 instanceof Double && ope2 instanceof Long) {
                    if (ope1 instanceof Long) {
                        long a = (long) ope1;
                        ope1 = (double) a;
                    }
                    if (ope2 instanceof Long) {
                        long a = (long) ope2;
                        ope2 = (double) a;
                    }
                    if ((double) ope2 == 0)//ERROR PORQUE NO SE PUEDE DIVIDIR DENTRO DE 0
                    {
                        Errores nuevoError = new Errores("No se puede dividir dentro de cero.", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                        return nuevoError;
                    }
                    resultado = (double) ope1 / (double) ope2;
                } else if (ope1 instanceof Character && ope2 instanceof Character) {//si los dos operadores son CARACTERES se devuelve una concatenacion
                    Errores error = new Errores("La operacion DIVISION entre dos caracteres no es posible", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    //ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                } else if (ope1 instanceof Character && ope2 instanceof Double || ope1 instanceof Character && ope2 instanceof Long) {//si ope2 es un double el ope1 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope1.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    if ((double) ope2 == 0) {
                        Errores nuevoError = new Errores("No se puede dividir dentro de cero.", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                        return nuevoError;
                    }
                    resultado = codigoAscii / (double) ope2;
                } else if (ope1 instanceof Double && ope2 instanceof Character || ope1 instanceof Long && ope2 instanceof Character) {//si ope1 es un double el ope2 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope2.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    if (codigoAscii == 0) {
                        Errores nuevoError = new Errores("No se puede dividir dentro de cero.", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                        return nuevoError;
                    }
                    resultado = (double) ope1 / codigoAscii;
                } else {
                    /*
                            -----------------------------ERROR-------------------
                         * LOS TIPOS DE DATOS NO SON COMPATIBLES, IMPOSIBLE HACER LA OPERACION
                     */
                    Errores nuevoError = new Errores("El tipo de dato con que queire hacer la operacion no se puede.(Cadena o caracter).", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                }
            } /*
                  * 
                  * 
                  * 
                  * -----------------PARA POTENCIA-----------------
                  *
                  *
                  *
             */ else if (ope.equals("^")) {
                if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool es un error
                {
                    /*
                           -----------------------ERROR----------------
                        * CON EL SIGNO MENOS NO SE PUEDE HACER OPERACIONES BOOLEANEAS
                     */
                    Errores nuevoError = new Errores("Con la potencia no se puede hacer op. booleanas ", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                } else if (ope1 instanceof Boolean && ope2 instanceof Double) //Si op1 es bool (1 o 0) y op2 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope1 == true) {
                        resultado = Math.pow(1, (double) ope2);
                    } else if ((Boolean) ope1 == false) {
                        resultado = Math.pow(0, (double) ope2);
                    }

                } else if (ope1 instanceof Double && ope2 instanceof Boolean)//Si op2 es bool (1 o 0) y op1 es double se hace un RESTA ((1 o 0)+num)
                {
                    if ((Boolean) ope2 == true) {
                        resultado = Math.pow((double) ope1, 1);
                    } else if ((Boolean) ope2 == false) {
                        resultado = Math.pow((double) ope1, 0);
                    }
                } else if (ope1 instanceof Boolean && ope2 instanceof Long) { //Si op1 es bool (1 o 0) y op2 es long se hace una multi ((1 o 0)*num)

                    if ((Boolean) ope1 == true) {
                        resultado = Math.pow(1, (long) ope2);
                        double a = (double)resultado;
                        return (long) a;
                    } else if ((Boolean) ope1 == false) {
                        resultado = Math.pow(0, (long) ope2);
                        double a = (double)resultado;
                        return (long) a;
                    }

                } else if (ope1 instanceof Long && ope2 instanceof Boolean) {//Si op2 es bool (1 o 0) y op1 es long se hace un multi ((1 o 0)*num)

                    if ((Boolean) ope2 == true) {
                        resultado = Math.pow(1, (long) ope1);
                        double a = (double)resultado;
                        return (long) a;
                    } else if ((Boolean) ope2 == false) {
                        resultado = Math.pow((long) ope1, 0);
                        double a = (double)resultado;
                        return (long) a;
                    }
                } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Double || ope1 instanceof Double && ope2 instanceof Long) {//si los dos operadores son decimales
                    if (ope1 instanceof Long) {
                        long a = (long) ope1;
                        ope1 = (double) a;
                    }
                    if (ope2 instanceof Long) {
                        long a = (long) ope2;
                        ope2 = (double) a;
                    }
                    resultado = Math.pow((double) ope1, (double) ope2);
                } else if (ope1 instanceof Long && ope2 instanceof Long) {//si los dos operadores son enteros
                    resultado = Math.pow((long) ope1, (long) ope2);
                    double a = (double)resultado;
                    return (long) a;
                } else if (ope1 instanceof Character && ope2 instanceof Character) {//si los dos operadores son CARACTERES se devuelve una concatenacion
                    Errores error = new Errores("La operacion MULTIPLICACION entre dos caracteres no es posible", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    //ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    return error;
                } else if (ope1 instanceof Character && ope2 instanceof Double) {//si ope2 es un double el ope1 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope1.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = Math.pow(codigoAscii, (double) ope2);
                } else if (ope1 instanceof Double && ope2 instanceof Character) {//si ope1 es un double el ope2 se convierte a ascii y devuelve un double
                    char caracterOperando1 = ope2.toString().charAt(0);
                    double codigoAscii = caracterOperando1;
                    resultado = Math.pow((double) ope1, codigoAscii);
                } else if (ope1 instanceof Character && ope2 instanceof Long) {//si ope2 es un long el ope1 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope1.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = Math.pow(codigoAscii, (long) ope2);
                    double a = (double)resultado;
                    return (long) a;
                } else if (ope1 instanceof Long && ope2 instanceof Character) {//si ope1 es un long el ope2 se convierte a ascii y devuelve un long
                    char caracterOperando1 = ope2.toString().charAt(0);
                    long codigoAscii = caracterOperando1;
                    resultado = Math.pow((long) ope1, codigoAscii);
                    double a = (double)resultado;
                    return (long) a;
                } else {
                    /*
                            -----------------------------ERROR-------------------
                         * LOS TIPOS DE DATOS NO SON COMPATIBLES, IMPOSIBLE HACER LA OPERACION
                     */
                    Errores nuevoError = new Errores("El tipo de dato con que queire hacer la operacion no se puede.(Cadena o caracter).", "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    return nuevoError;
                }

            }
        }
        return resultado;
    }

    //################# operacion RELACIONAL
    public Object operacionRelacional(Object ope1, Object operador, Object ope2) {
        /*
                El valor que se va devolver sera un tipo booleano dentro del object, para que se pueda evaluar mas facil
         */
        Object resultado = null;
        boolean bandera = false;

        double numeroCasteado1 = 0.0;
        double numeroCasteado2 = 0.0;
        char[] s = null;
        char[] s2 = null;
        if (ope1 != null && ope2 != null) {

            /*
                 Verificaciones que tipo de operando es: Number, String o bool
             */
            if (ope1 instanceof String && ope2 instanceof String || ope1 instanceof Character && ope2 instanceof Character || ope1 instanceof Character && ope2 instanceof String || ope1 instanceof String && ope2 instanceof Character)//Se verifica si vienen strings para operar sus acsiis
            {
                bandera = true;//se activa la bandera de strings
                s = ope1.toString().toCharArray();
                s2 = ope2.toString().toCharArray();
            } else if (ope1 instanceof Double && ope2 instanceof Double || ope1 instanceof Long && ope2 instanceof Long)//si viene numeros, se hace la verificacion
            {
                if (ope1 instanceof Long) {
                    long a = (long) ope1;
                    ope1 = (double) a;
                }
                if (ope2 instanceof Long) {
                    long a = (long) ope2;
                    ope2 = (double) a;
                }
                numeroCasteado1 = (double) ope1;
                numeroCasteado2 = (double) ope2;
            } else if (ope1 instanceof Double && ope2 instanceof Boolean || ope1 instanceof Long && ope2 instanceof Boolean)//si viene double y boolean(1 o 0)
            {
                if (ope1 instanceof Long) {
                    long a = (long) ope1;
                    ope1 = (double) a;
                }
                if (ope2 instanceof Long) {
                    long a = (long) ope2;
                    ope2 = (double) a;
                }
                numeroCasteado1 = (double) ope1;
                if ((Boolean) ope2 == true) {
                    numeroCasteado2 = 1.0;
                } else {
                    numeroCasteado2 = 0;
                }
            } else if (ope1 instanceof Boolean && ope2 instanceof Double || ope1 instanceof Boolean && ope2 instanceof Long)//si viene boolean(se convierte a 1 o 0) y double
            {
                if (ope1 instanceof Long) {
                    long a = (long) ope1;
                    ope1 = (double) a;
                }
                if (ope2 instanceof Long) {
                    long a = (long) ope2;
                    ope2 = (double) a;
                }
                numeroCasteado2 = (double) ope2;
                if ((Boolean) ope1 == true) {
                    numeroCasteado1 = 1.0;
                } else {
                    numeroCasteado1 = 0;
                }
            } else if (ope1 instanceof Boolean && ope2 instanceof Boolean)//si vienen los dos operandos como boolean
            {
                if ((Boolean) ope1 == true) {
                    numeroCasteado1 = 1.0;
                } else {
                    numeroCasteado1 = 0;
                }
                if ((Boolean) ope2 == true) {
                    numeroCasteado2 = 1.0;
                } else {
                    numeroCasteado2 = 0;
                }
            } else if (ope1 instanceof Character && ope2 instanceof Double || ope1 instanceof Character && ope2 instanceof Long) {
                if (ope1 instanceof Long) {
                    long a = (long) ope1;
                    ope1 = (double) a;
                }
                if (ope2 instanceof Long) {
                    long a = (long) ope2;
                    ope2 = (double) a;
                }
                char caracterOperando1 = ope1.toString().charAt(0);
                numeroCasteado1 = (double) caracterOperando1;
                numeroCasteado2 = (double) ope2;
            } else if (ope1 instanceof Double && ope2 instanceof Character || ope1 instanceof Long && ope2 instanceof Character) {
                if (ope1 instanceof Long) {
                    long a = (long) ope1;
                    ope1 = (double) a;
                }
                if (ope2 instanceof Long) {
                    long a = (long) ope2;
                    ope2 = (double) a;
                }
                numeroCasteado1 = (double) ope1;
                char caracterOperando1 = ope2.toString().charAt(0);
                numeroCasteado2 = (double) caracterOperando1;
            } else {
                /*
                     Errores ES INCOMPATIBILIDAD DE TIPOS
                 */
                Errores nuevoErrores = new Errores("Incompatibilidad de tipos en operacion relacional", "Sementico", 0, 0, "Incompatibilidad de tipos en operacion relacional");
                return nuevoErrores;
            }
            /*
                    empieza las operaciones relaciones 
             */
            String ope = operador.toString();
            if (null != ope) {
                switch (ope) {
                    case ">":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) > 0;
                        }
                        return numeroCasteado1 > numeroCasteado2;//hace la operacion con numeros
                    case "<":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) < 0;

                        }
                        return numeroCasteado1 < numeroCasteado2;//hace la operacion con numeros
                    case ">=":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) >= 0;

                        }
                        return numeroCasteado1 >= numeroCasteado2;//hace la operacion con numeros
                    case "<=":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) <= 0;

                        }
                        return numeroCasteado1 <= numeroCasteado2;//hace la operacion con numeros
                    case "==":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) == 0;
                        }
                        return numeroCasteado1 == numeroCasteado2;//hace la operacion con numeros
                    case "!=":
                        if (bandera)//bandera string activada, se hace la operacion con los strings sino con numeros
                        {
                            return ope1.toString().compareTo(ope2.toString()) != 0;

                        }
                        return numeroCasteado1 != numeroCasteado2;//hace la operacion con numeros

                    default:
                        break;
                }
            }
        }

        return resultado;

    }

    //#################operacion LOGICA
    public Object operacionesLogicas(Object ope1, Object ope, Object ope2) {
        Object resultado = null;
        String operador2 = ope.toString();

        if (ope1 instanceof Boolean && ope2 instanceof Boolean)//Si los dos operandos son bool se hace la operacion logica
        {
            Boolean operando1 = (Boolean) ope1;
            Boolean operando2 = (Boolean) ope2;
            if (operador2.equals("!")) //solo se hace el NOT porque solo viene un operando, sino dara error o devolveria en el operando 2 un null
            {
                if (ope1 instanceof Boolean) {
                    return !(operando1);
                } else {
                    /*---------------------ERROR----------------------
                        SOLO SE PUEDE OPERAR TIPOS BOOLEANOS
                     */
                    Errores nuevoError = new Errores("Solo se pueden operar tipos booleanos", "Sementico", 0, 0, "Solo se pueden operar tipos booleanos");
                    return nuevoError;
                }
            }
            if (operador2.equals("&&")) {
                return operando1 && operando2;
            } else if (operador2.equals("||")) {
                return operando1 || operando2;
            } else if (operador2.equals("|&")) {
                return operando1 ^ operando2;
            }

        } else {
            /*---------------------ERROR----------------------
                    SOLO SE PUEDE OPERAR TIPOS BOOLEANOS
             */
            Errores nuevoError = new Errores("Solo se pueden operar tipos booleanos", "Sementico", 0, 0, "Solo se pueden operar tipos booleanos");
            return nuevoError;
        }
        return resultado.toString().compareTo(operador2);
    }

    public void resolverHerencia(Als als) {
        LinkedHashMap<String, VariableG> listaAux = new LinkedHashMap<String, VariableG>();
        LinkedHashMap<String, VariableG> listaAuxHeredados = new LinkedHashMap<String, VariableG>();
        Als heredado = TablaSimbolosGraphik.getInstancia().buscarAls(als.getNombrePadre());//se busca del que hereda
        if (heredado != null) {//se verifica que exista
            if (heredado.getVisibilidad().equals("publico")) {

                if (heredado.getSeHizoHerencia()) {//se verifica si ya se hizo la herencia
                    //se llama a resolverHerencia para que haga la herencia de esa clase
                    resolverHerencia(heredado);
                }

                /*SE VUELVE A VERIFICAR SI SE HIZO CORRECTAMENTE*/
                //se copia las variables del padre al hijo
                for (String key : heredado.getListaAtributos().keySet()) {
                    VariableG var1 = (VariableG) heredado.getListaAtributos().get(key).clone();
                    VariableG var = new VariableG(var1);
                    // var.setValor("sin valor");
                    if (als.getListaAtributos().containsKey(var.getNombre())) {
                        Errores nuevoError = new Errores("La variable " + var.getNombre() + " que se heredo de " + heredado.getNombre() + " ya existe en " + als.getNombre(), "Sementico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                        ManejadorErrores.getInstancia().agregarErrorGraphik(nuevoError);
                        continue;
                    }
                    if (var.getVisibilidad().equals("publico")) {
                        listaAuxHeredados.put(var.getNombre(), var);
                        //als.getListaAtributos().put(var.getNombre(), var);
                    } else if (var.getVisibilidad().equals("protegido")) {
                        //als.getListaAtributos().put(var.getNombre(), var);
                        listaAuxHeredados.put(var.getNombre(), var);
                    } else {//error porque es privada

                    }

                }
                //se copia los metodos del padre al hijo
                for (MetodoG listaMetodo : heredado.getListaMetodos()) {
                    MetodoG nuevoMetodo = (MetodoG) listaMetodo.clone();
                    if (nuevoMetodo.getVisibilidad().equals("publico")) {

                        als.getListaMetodos().add(nuevoMetodo);
                    } else if (nuevoMetodo.getVisibilidad().equals("protegido")) {
                        als.getListaMetodos().add(nuevoMetodo);
                    } else {//error porque es privada

                    }

                }
                listaAux = (LinkedHashMap<String, VariableG>) als.getListaAtributos().clone();
                listaAuxHeredados.putAll(listaAux);
                als.setListaAtributos((LinkedHashMap<String, VariableG>) listaAuxHeredados.clone());
            } else {//no se puede porque es privada

            }
        }

    }

    public void asignarValores(Als als) {
        for (String key : als.getListaAtributos().keySet()) {
            VariableG var = als.getListaAtributos().get(key);
            //se ejecutura su nodo para resolver la expresion
            if (var.getValor() instanceof Nodo) {
                if (((Nodo) var.getValor()).getNombre().equals("E")) {
                    Object valor = operaciones((Nodo) var.getValor());
                    valor = verifacionTipos(var.getTipo(), valor);
                    var.setValor(valor);
                } else {
                    int tamanio = tamanioArreglo(var.getDimensiones());
                    if (tamanio != -1) {
                        var.setTamanioArreglo(tamanio);
                        var.inicializarArray(tamanio);
                        ArrayList<Integer> tamanios = tamaniosArreglo(var.getDimensiones());
                        var.setTamanios(tamanios);
                        asignarValoresArreglo(var, (Nodo) var.getValor());
                        var.setValor("sin valores");
                    } else {
                        als.getListaAtributos().remove(key);
                        agregarError("Las dimensiones del arreglo " + var.getNombre() + " no son de tipo entero, no se pudo almanecenar", "Semantico", 0, 0);
                    }
                }
            } else if (!compararTipo(var.getTipo())) {//si es falso es porque es tipo objeto y se debe de instanciar 
                Als objeto = TablaSimbolosGraphik.getInstancia().buscarAls(var.getTipo());
                if (objeto != null) {
                    Als nuevaInstancia = clonarAls(objeto);
                    var.setValor(nuevaInstancia);
                } else {
                    Errores errorObj = new Errores("El objeto  " + var.getTipo() + " no existe, ese ALs no ha sido creado.", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());

                }
            }

        }
    }

    public void hacerInstancias(Als als) {
        for (String key : als.getListaAtributos().keySet()) {
            VariableG var = als.getListaAtributos().get(key);
            if (compararTipo(var.getTipo())) {//
                if (var.getValor() instanceof Nodo) {//se preugnta si es un nodo, quiere decir que no se a resuelto nada
                    Nodo valor = (Nodo) var.getValor();//se asigna a valor el (valor de la variable)
                    if (valor.getNombre().equals("E")) {//es una operacion normal
                        Object valor2 = operaciones((Nodo) var.getValor());
                        valor2 = verifacionTipos(var.getTipo(), valor2);
                        var.setValor(valor2);
                    } else if (valor.getNombre().equals("VALORES")) {
                        int tamanio = tamanioArreglo(var.getDimensiones());
                        if (tamanio != -1) {
                            var.setTamanioArreglo(tamanio);
                            var.inicializarArray(tamanio);
                            ArrayList<Integer> tamanios = tamaniosArreglo(var.getDimensiones());
                            var.setTamanios(tamanios);
                            asignarValoresArreglo(var, (Nodo) var.getValor());
                            var.setValor("sin valores");
                        } else {
                            als.getListaAtributos().remove(key);
                            agregarError("Las dimensiones del arreglo " + var.getNombre() + " no son de tipo entero, no se pudo almanecenar", "Semantico", 0, 0);
                        }
                    }
                }
            } else//es un objeto
            if (var.getValor().toString().equals("instancia")) {
                Als objeto = TablaSimbolosGraphik.getInstancia().buscarAls(var.getTipo());
                if (objeto != null) {
                    Als nuevaInstancia = clonarAls(objeto);
                    var.setValor(nuevaInstancia);
                    hacerInstancias((Als) var.getValor());
                } else {
                    Errores errorObj = new Errores("El objeto  " + var.getTipo() + " no existe, ese ALs no ha sido creado.", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());

                }
            } else {
                hacerInstancias((Als) var.getValor());
            }
        }
    }


    public boolean compararTipo(String tipo) {
        return (tipo.equals("entero") || tipo.equals("decimal") || tipo.equals("bool") || tipo.equals("cadena") || tipo.equals("caracter"));

    }

    public Object verifacionTipos(String tipo, Object valor) {
        if (valor instanceof Errores) {
            Errores error = (Errores) valor;
            error.setColumna(0);
            error.setFila(0);
            error.setArchivo(TablaSimbolosGraphik.getInstancia().getArchivo());
            ManejadorErrores.getInstancia().agregarErrorGraphik(error);
            return "sin valor";
        }
        if (tipo.equals("decimal"))//SE VERIFICA EL NUMBER
        {
            if (valor instanceof Boolean) {
                if ((Boolean) valor) {
                    valor = (double) 1.0;
                } else {
                    valor = (double) 0;
                }
            } else if (valor instanceof Character) {
                char caracterOperando1 = valor.toString().charAt(0);
                double codigoAscii = caracterOperando1;

                valor = codigoAscii;
            } else if (valor instanceof Long) {
                long a = (Long) valor;
                valor = (double) a;
            } else if (valor instanceof String) {

                Errores error = new Errores("String incompatible con Decimal", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else {
                //number se le va asignar un double, esta bien
            }
        } else if (tipo.equals("entero")) {
            if (valor instanceof Boolean) {
                if ((Boolean) valor) {
                    valor = (long) 1;
                } else {
                    valor = (long) 0;
                }
            } else if (valor instanceof Character) {
                char caracterOperando1 = valor.toString().charAt(0);
                long codigoAscii = caracterOperando1;

                valor = codigoAscii;
            } else if (valor instanceof Double) {
                double a = (Double) valor;
                valor = (long) a;
            } else if (valor instanceof String) {

                Errores error = new Errores("String incompatible con Entero", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else {
                //number se le va asignar un double, esta bien
            }
        } else if (tipo.equals("caracter")) {
            if (valor instanceof Boolean) {
                Errores error = new Errores("Boolean incompatible con Caracter", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else if (valor instanceof Long) {
                long a = (Long) valor;
                char caracterOperando1 = (char) a;

                valor = caracterOperando1;
            } else if (valor instanceof Double) {
                double a = (Double) valor;
                char caracterOperando1 = (char) a;
                valor = caracterOperando1;
            } else if (valor instanceof String) {

                Errores error = new Errores("String incompatible con Caracter", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else {
                //number se le va asignar un double, esta bien
            }
        } else if (tipo.equals("cadena")) {//SE VERIFICA CON TIPO STRING

            valor = valor.toString();
        } else if (tipo.equals("bool"))//SE VERIFICA CON TIPO BOOLEAN
        {
            if (valor instanceof String) {

                Errores error = new Errores("String incompatible con Boolean", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else if ((valor instanceof Character)) {

                Errores error = new Errores("Caracter incompatible con Boolean", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                valor = "sin valor";
            } else if (valor instanceof Double) {
                if ((Double) valor == 1.0) {
                    valor = true;
                } else if ((Double) valor == 0) {
                    valor = false;
                } else {
                    Errores error = new Errores("El valor para asignarselo a un bool debe de ser de 1 o 0", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    valor = "sin valor";
                }
            } else if (valor instanceof Long) {
                if ((Long) valor == 1) {
                    valor = true;
                } else if ((Long) valor == 0) {
                    valor = false;
                } else {
                    Errores error = new Errores("El valor para asignarselo a un bool debe de ser de 1 o 0", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
                    ManejadorErrores.getInstancia().agregarErrorGraphik(error);
                    valor = "sin valor";
                }
            } else {
                //number se le va asignar un double, esta bien
            }
        }
        return valor;
    }

    public void verificarValores(Als als) {
        for (String key : als.getListaAtributos().keySet()) {
            VariableG var = als.getListaAtributos().get(key);
            if (!compararTipo(var.getTipo()) && !(var.getTipo().equals("arreglo"))) {
                Als instancia = (Als) var.getValor();
                for (String key2 : instancia.getListaAtributos().keySet()) {
                    VariableG var2 = instancia.getListaAtributos().get(key2);
                    if (var2.getValor().toString().equals("instancia")) {
                        Als objeto = TablaSimbolosGraphik.getInstancia().buscarAls(var2.getTipo());
                        if (objeto != null) {
                            Als nuevaInstancia = clonarAls(objeto);
                            var2.setValor(nuevaInstancia);
                        } else {
                            Errores errorObj = new Errores("El objeto  " + var2.getTipo() + " no existe, ese ALs no ha sido creado.", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());

                        }
                    }
                }

            } else if (compararTipo(var.getTipo())) {
                if (var.getValor() instanceof Nodo) {

                }
            }
        }

    }

    public Als clonarAls(Als als) {
        Als nuevaInstancia = new Als();
        nuevaInstancia.setNombre(als.getNombre());
        nuevaInstancia.setVisibilidad(als.getVisibilidad());
        nuevaInstancia.setNombrePadre(als.getNombrePadre());
        nuevaInstancia.setSeHizoHerencia(als.getSeHizoHerencia());
        nuevaInstancia.setCuerpoAls(als.getCuerpoAls());
        LinkedHashMap<String, VariableG> listaAtributosAux = new LinkedHashMap<String, VariableG>();//se crear una lista auxiliar de atributos
        for (String key : als.getListaAtributos().keySet()) {//se recorre la lista de atributos del als 
            VariableG var = (VariableG) als.getListaAtributos().get(key).clone();//clonamos la variable
            listaAtributosAux.put(var.getNombre(), var);
        }
        nuevaInstancia.setListaAtributos((LinkedHashMap<String, VariableG>) listaAtributosAux.clone());//la lista auxiliar se copia al lista de la nuevainstancia

        ArrayList<MetodoG> listaMetodoAux = new ArrayList<MetodoG>();
        for (MetodoG metod : als.getListaMetodos()) {
            MetodoG nuevoMetodo = (MetodoG) metod;
            listaMetodoAux.add((MetodoG) nuevoMetodo.clone());
        }
        nuevaInstancia.setListaMetodos((ArrayList<MetodoG>) listaMetodoAux.clone());

        return nuevaInstancia;
    }

    public Object buscarValorVariableSoloId(String id) {
        VariableG var = TablaSimbolosGraphik.getInstancia().buscarLocalmente(id);
        if (var == null) {
            var = TablaSimbolosGraphik.getInstancia().buscarGlobalmente(id);
            if (var != null) {
                return var;
            }
        } else {
            return var;
        }
        Errores error = new Errores("La variable" + id + " no ha sido definida.", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
        ManejadorErrores.getInstancia().agregarErrorHaskell(error);
        return error;
    }

    public Errores agregarError(String descripcion, String tipo, int columna, int fila) {
        Errores error = new Errores(descripcion, tipo, columna, fila, TablaSimbolosGraphik.getInstancia().getArchivo());
        ManejadorErrores.getInstancia().agregarErrorGraphik(error);
        return error;
    }

    public int tamanioArreglo(Nodo dime) {
        long tamanio = 1;
        for (Nodo nodo : dime.getHijos2()) {
            Object valorExp = operaciones(nodo);
            Object valor = verifacionTipos("entero", valorExp);
            if (valor instanceof Long) {
                tamanio = tamanio * (Long) operaciones(nodo);
            } else {
                tamanio = -1;
                break;
            }
        }
        return (int) tamanio;
    }

    public ArrayList<Integer> tamaniosArreglo(Nodo dime) {
        ArrayList<Integer> tamanios = new ArrayList<Integer>();
        for (Nodo nodo : dime.getHijos2()) {
            Object valorExp = operaciones(nodo);
            Object valor = verifacionTipos("entero", valorExp);
            if (valor instanceof Long) {
                long tamanio = (Long) operaciones(nodo);
                tamanios.add((int) tamanio);
            } else {
                break;
            }
        }
        return tamanios;
    }

    public Object asignarValoresArreglo(VariableG varGlobalNueva, Nodo valores) {
        if (varGlobalNueva.getTamanioArreglo() == valores.getNumeroHijos()) {

            for (Nodo nodo : valores.getHijos2()) {
                Object valor = recoleccion(nodo);
                if (!(valor instanceof Errores)) {
                    Object valorNuevo = verifacionTipos(varGlobalNueva.getTipo(), valor);
                    if (!valorNuevo.equals("sin valor")) {
                        varGlobalNueva.getValores().add(valorNuevo);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            }
        } else {

            Errores error = new Errores("Los elemetos que se le quieren asignar al arreglo " + varGlobalNueva.getNombre() + " exceden su tamanio.", "Semantico", 0, 0, TablaSimbolosGraphik.getInstancia().getArchivo());
            ManejadorErrores.getInstancia().agregarErrorHaskell(error);
            return error;
        }
        return true;
    }
}
