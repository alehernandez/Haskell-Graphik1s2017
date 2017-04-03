/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.manejadores;

import java.io.IOException;
import java.io.StringReader;
import org.alejandrohernandez.analizadorHaskell.ParserV2;
import org.alejandrohernandez.analizadorHaskell.Scanner;

/**
 *
 * @author oscar
 */
public class ManejadorHaskellLenguaje {

    public static void analizar(String texto) throws IOException, Exception {
        texto = texto.toLowerCase();
        StringReader miReader = new StringReader(texto);
        Scanner scn = new Scanner(miReader);
        ParserV2 par = new ParserV2(scn);
        par.parse();

//        Symbol s = (Symbol) scn.next_token();
//        while (s.sym != 0) {
//            System.out.println("Lexema: " + s.value + "         Token: " + s.sym);
//            s = (Symbol) scn.next_token();
//
//        }
    }
}
