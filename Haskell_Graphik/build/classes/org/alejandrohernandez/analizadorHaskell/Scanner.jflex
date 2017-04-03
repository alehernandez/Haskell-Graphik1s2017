package org.alejandrohernandez.analizadorHaskell;
import java_cup.runtime.Symbol;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.TablaSimbolosHaskell;
%%
%{
//CODIGO EN JAVA
String cadena="";

%}

%state STRING STRING COMENLINEA CARACTER
%cupsym simbolo
%class Scanner
%cup
%public
%column
%line
%char
%ignorecase
%unicode


digito = [0-9]
numero = {digito}+
palabra = [a-zA-Z]+
id = {palabra}({palabra}|{numero}|"_")*
decimal = ({numero}{numero}*"."{numero}{numero}*)
entero = ({numero}{numero}*)

%%
<YYINITIAL>{

"$" {return new Symbol(simbolo.dollar, yychar, yyline, yytext()); }
"," {return new Symbol(simbolo.coma, yychar, yyline, yytext()); }
"=" {return new Symbol(simbolo.asignacion, yychar, yyline, yytext()); }
";" {return new Symbol(simbolo.pC, yychar, yyline, yytext()); }
"(" {return new Symbol(simbolo.aP, yychar, yyline, yytext()); }
")" {return new Symbol(simbolo.cP, yychar, yyline, yytext()); }
"[" {return new Symbol(simbolo.aC, yychar, yyline, yytext()); }
"]" {return new Symbol(simbolo.cC, yychar, yyline, yytext()); }
"{" {return new Symbol(simbolo.aL, yychar, yyline, yytext()); }
"}" {return new Symbol(simbolo.cL, yychar, yyline, yytext()); }
"+" {return new Symbol(simbolo.mas, yychar, yyline, yytext()); }
"-" {return new Symbol(simbolo.menos, yychar, yyline, yytext()); }
"*" {return new Symbol(simbolo.por, yychar, yyline, yytext()); }
"/" {return new Symbol(simbolo.div, yychar, yyline, yytext()); }
"'mod'" {return new Symbol(simbolo.mod, yychar, yyline, yytext()); }
"'sqrt'" {return new Symbol(simbolo.sqrt, yychar, yyline, yytext()); }
"'pot'" {return new Symbol(simbolo.pot, yychar, yyline, yytext()); }
"&&" {return new Symbol(simbolo.and, yychar, yyline, yytext()); }
"||" {return new Symbol(simbolo.or, yychar, yyline, yytext()); }
"==" {return new Symbol(simbolo.igualIgual, yychar, yyline, yytext()); }
"<" {return new Symbol(simbolo.menor, yychar, yyline, yytext()); }
">" {return new Symbol(simbolo.mayor, yychar, yyline, yytext()); }
"<=" {return new Symbol(simbolo.menorIgual, yychar, yyline, yytext()); }
">=" {return new Symbol(simbolo.mayorIgual, yychar, yyline, yytext()); }
"!=" {return new Symbol(simbolo.diferente, yychar, yyline, yytext()); }

"let" {return new Symbol(simbolo.let, yychar, yyline, yytext()); }
"++" {return new Symbol(simbolo.masMas, yychar, yyline, yytext()); }
"!!" {return new Symbol(simbolo.indiceLista, yychar, yyline, yytext()); }


"succ" {return new Symbol(simbolo.succ, yychar, yyline, yytext()); }
"calcular" {return new Symbol(simbolo.calcular, yychar, yyline, yytext()); }
"%" {return new Symbol(simbolo.porcentaje, yychar, yyline, yytext()); }
"decc" {return new Symbol(simbolo.decc, yychar, yyline, yytext()); }
"min" {return new Symbol(simbolo.min, yychar, yyline, yytext()); }
"max" {return new Symbol(simbolo.max, yychar, yyline, yytext()); }

"sum" {return new Symbol(simbolo.sum, yychar, yyline, yytext()); }
"product" {return new Symbol(simbolo.product, yychar, yyline, yytext()); }
"revers" {return new Symbol(simbolo.revers, yychar, yyline, yytext()); }
"impr" {return new Symbol(simbolo.impr, yychar, yyline, yytext()); }
"par" {return new Symbol(simbolo.par, yychar, yyline, yytext()); }
"asc" {return new Symbol(simbolo.asc, yychar, yyline, yytext()); }
"desc" {return new Symbol(simbolo.desc, yychar, yyline, yytext()); }
"length" {return new Symbol(simbolo.tamanio, yychar, yyline, yytext()); }
"if" {return new Symbol(simbolo.si, yychar, yyline, yytext()); }
"then" {return new Symbol(simbolo.then, yychar, yyline, yytext()); }
"else" {return new Symbol(simbolo.sino, yychar, yyline, yytext()); }
"case" {return new Symbol(simbolo.caso, yychar, yyline, yytext()); }
":" {return new Symbol(simbolo.dosPuntos, yychar, yyline, yytext()); }
"end" {return new Symbol(simbolo.fin, yychar, yyline, yytext()); }


\" {yybegin(STRING);}   
"'" {yybegin(CARACTER);}

{entero} {return new Symbol(simbolo.entero, yychar, yyline, yytext());}
{id} {return new Symbol(simbolo.id, yychar, yyline,yytext());}
{decimal} {return new Symbol(simbolo.decimal, yychar, yyline, yytext());}

/* BLANCOS*/
[ \t\r\f\n]+ {/*Se ignoran*/}

/*Cualquier otro*/
. {javax.swing.JOptionPane.showMessageDialog(null,"Error Lexico: " + yytext());
    //Errores error = new Errores(yytext(),"No pertenece al lenguaje",yycolumn+1, yyline+1, "Error lexico");
ManejadorErrores.getInstancia().agregarErrorHaskell(new Errores(yytext() +" no pertenece al lenguaje.", "Lexico", (yycolumn+1),(yyline+1),TablaSimbolosHaskell.getInstancia().getArchivo()));
}
}
<STRING>{
[\"] {String temporal=cadena; cadena=""; yybegin(YYINITIAL); return new Symbol(simbolo.cadena, yychar, yyline, "\"" + temporal + "\""); }
[^\"] {cadena +=yytext(); }
}


<CARACTER>{
[\'] {String temporal=cadena; cadena=""; yybegin(YYINITIAL); return new Symbol(simbolo.caracter, yychar, yyline, "'"+ temporal+"'"); }
[^\'] {cadena +=yytext(); }
}