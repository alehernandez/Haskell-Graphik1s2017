/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.reportes;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.manejadores.ManejadorErrores;

/**
 *
 * @author oscar
 */
public class ReporteHaskell {

    public void crearHtml(int numero) throws IOException {
        String cad = " ";

        FileWriter filewriter = null;
        PrintWriter printw = null;

        try {
            filewriter = new FileWriter("C:\\Users\\oscar\\Desktop\\Disk\\USAC\\2017\\Primer Semestre\\Compi 2\\Proyecto1\\Reporte de error haskell.html");//declarar el archivo
            printw = new PrintWriter(filewriter);//declarar un impresor

            printw.println("<html>");
            printw.println("<head><title>Reporte Errores</title></head>"
                    + "<hr style=\"color:white;\">");
            //si queremos escribir una comilla " en el 
            //archivo uzamos la diagonal invertida \"
            printw.println("<body style=\"background-color:orange\">");

            //si quisieramos escribir una cadena que vide de una lista o 
            //de una variable lo concatenamos
            printw.println("<div style=\"font-family: Helvetica, Arial;\" >");
            printw.println("<h3 ><p> Lenguaje HASKELL - Reporte Errores</p></h3>");

            //podemos añadir imagenes con codigo html
            printw.println("<hr style=\"color:white;\">");
            int contador = 0;
            for (Errores error : ManejadorErrores.getInstancia().getListaErroresHaskell()) {
                String descripcion = "";

                printw.println("<table border=\"1\" >\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Descripcion</th>\n"
                        + "		<th>" + error.getDescripcion() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Linea</th>\n"
                        + "		<th>" + Integer.toString(error.getFila()) + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Columna</th>\n"
                        + "		<th>" + Integer.toString(error.getColumna()) + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Tipo</th>\n"
                        + "		<th>" + error.getTipo() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Archivo</th>\n"
                        + "		<th>" + error.getArchivo() + "</th>\n"
                        + "	</tr>\n"
                        + "</table>");
                printw.println("<hr style=\"color:white;\">");

            }
            printw.println("</body>");
            printw.println("</html>");

            //no devemos olvidar cerrar el archivo para que su lectura sea correcta
            printw.close();//cerramos el archivo

        } catch (Exception e) {

        }
    }
}
