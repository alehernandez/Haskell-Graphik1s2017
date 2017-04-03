/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.reportes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.beans.Simbolo;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.TablaSimbolosGraphik;

/**
 *
 * @author oscar
 */
public class ReporteSimbolosGraphik {
     public void crearHtml() throws IOException {
        String cad = " ";

        FileWriter filewriter = null;
        PrintWriter printw = null;

        try {
            filewriter = new FileWriter("C:\\Users\\oscar\\Desktop\\Disk\\USAC\\2017\\Primer Semestre\\Compi 2\\Proyecto1\\Reporte de simbolos GRAPHIK .html");//declarar el archivo
            printw = new PrintWriter(filewriter);//declarar un impresor

            printw.println("<html>");
            printw.println("<head><title>Reporte Errores GRAPHIK</title></head>"
                    + "<hr style=\"color:white;\">");
            //si queremos escribir una comilla " en el 
            //archivo uzamos la diagonal invertida \"
            printw.println("<body style=\"background-color:#348680\">");

            //si quisieramos escribir una cadena que vide de una lista o 
            //de una variable lo concatenamos
            printw.println("<div style=\"font-family: Helvetica, Arial;\">");
            printw.println("<h3><p> Lenguaje GRAPHIK - Reporte de Simbolos</p></h3>");

            //podemos añadir imagenes con codigo html
            printw.println("<hr style=\"color:white;\">");
            int contador = 0;
            for (Simbolo sim :TablaSimbolosGraphik.getInstancia().getListaSimbolos()) {
                String descripcion = "";

                printw.println("<table border=\"1\" >\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Identificador</th>\n"
                        + "		<th>" + sim.getVarG().getNombre() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Tipo</th>\n"
                        + "		<th>" + sim.getVarG().getTipo() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Ambito</th>\n"
                        + "		<th>" + sim.getAmbito() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Rol</th>\n"
                        + "		<th>" + sim.getRol() + "</th>\n"
                        + "	</tr>\n"
                        + "	<tr>\n"
                        + "		<th style=\"color:white;\">Dimensiones</th>\n"
                        + "		<th>" + sim.getDimensiones() + "</th>\n"
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
