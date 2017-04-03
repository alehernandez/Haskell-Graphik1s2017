/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haskell_graphik;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java_cup.runtime.Symbol;
import static jdk.nashorn.internal.objects.NativeArray.map;
import jflex.SilentExit;
import org.alejandrohernandez.analizadorHaskell.Scanner;

/**
 *
 * @author oscar
 */
public class Haskell_Graphik {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //generarLexer("C:\\Users\\oscar\\Desktop\\Disk\\USAC\\2017\\Primer Semestre\\Compi 2\\Proyecto1\\Haskell_Graphik\\src\\org\\alejandrohernandez\\analizadorHaskell\\Scanner.jflex");
        //  generarLexer("C:\\Users\\oscar\\Desktop\\Disk\\USAC\\2017\\Primer Semestre\\Compi 2\\Proyecto1\\Haskell_Graphik\\src\\org\\alejandrohernandez\\analizadorgraphik\\LexicoGk.jflex");
        //  generarLexicoGraphik();
        //System.out.println(fac(40));
    char a = (char) 40.0;
    }

    public static long fac(long n) {
        if (n == 0) {
            return 1;
        } else {
            return n * fac(n - 1);
        }
    }

    public static void generarLexer(String path) {
        File file = new File(path);
        jflex.Main.generate(file);

    }

    public static void generarLexicoGraphik() throws SilentExit {
        String ruta = "src/org/alejandrohernandez/analizadorgraphik/";
        String opcFlex[] = {ruta + "LexicoGk.jflex", "-d", ruta};
        jflex.Main.generate(opcFlex);
    }

    public static void analizar(String texto) throws IOException, Exception {

        StringReader miReader = new StringReader(texto);
        Scanner scn = new Scanner(miReader);
        /*ParserV2 par = new ParserV2(scn);
        par.parse();*/

        Symbol s = (Symbol) scn.next_token();
        while (s.sym != 0) {
            System.out.println("Lexema: " + s.value + "         Token: " + s.sym);
            s = (Symbol) scn.next_token();

        }
    }

}
