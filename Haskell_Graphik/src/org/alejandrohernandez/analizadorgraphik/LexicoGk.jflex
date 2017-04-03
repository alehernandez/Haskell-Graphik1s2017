package org.alejandrohernandez.analizadorgraphik;
import java_cup.runtime.Symbol;
import org.alejandrohernandez.beans.Errores;
import org.alejandrohernandez.manejadores.ManejadorErrores;
import org.alejandrohernandez.manejadores.TablaSimbolosGraphik;
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
archivogk = {palabra}({palabra}|{numero}|"_")*".""gk"
decimal = ({numero}{numero}*"."{numero}{numero}*)
entero = ({numero}{numero}*)

%%
<YYINITIAL>{
"?" {return new Symbol(simbolo.interrogacion, yychar, yyline, yytext()); }
"," {return new Symbol(simbolo.coma, yychar, yyline, yytext()); }
"." {return new Symbol(simbolo.punto, yychar, yyline, yytext()); }
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
"^" {return new Symbol(simbolo.pot, yychar, yyline, yytext()); }
"&&" {return new Symbol(simbolo.and, yychar, yyline, yytext()); }
"||" {return new Symbol(simbolo.or, yychar, yyline, yytext()); }
"$|" {return new Symbol(simbolo.xor, yychar, yyline, yytext()); }
"!" {return new Symbol(simbolo.not, yychar, yyline, yytext()); }
"==" {return new Symbol(simbolo.igualIgual, yychar, yyline, yytext()); }
"<" {return new Symbol(simbolo.menor, yychar, yyline, yytext()); }
">" {return new Symbol(simbolo.mayor, yychar, yyline, yytext()); }
"<=" {return new Symbol(simbolo.menorIgual, yychar, yyline, yytext()); }
">=" {return new Symbol(simbolo.mayorIgual, yychar, yyline, yytext()); }
"!=" {return new Symbol(simbolo.diferente, yychar, yyline, yytext()); }

"publico" {return new Symbol(simbolo.publico, yychar, yyline, yytext()); }
"privado" {return new Symbol(simbolo.privado, yychar, yyline, yytext()); }
"protegido" {return new Symbol(simbolo.protegido, yychar, yyline, yytext()); }
"var" {return new Symbol(simbolo.var, yychar, yyline, yytext()); }
"importar" {return new Symbol(simbolo.importar, yychar, yyline, yytext()); }
"als" {return new Symbol(simbolo.als, yychar, yyline, yytext()); }
"var" {return new Symbol(simbolo.var, yychar, yyline, yytext()); }
"hereda" {return new Symbol(simbolo.hereda, yychar, yyline, yytext()); }
"nuevo" {return new Symbol(simbolo.nuevo, yychar, yyline, yytext()); }

"verdadero" {return new Symbol(simbolo.verdadero, yychar, yyline, yytext()); }
"falso" {return new Symbol(simbolo.falso, yychar, yyline, yytext()); }
"vacio" {return new Symbol(simbolo.vacio, yychar, yyline, yytext()); }
"entero" {return new Symbol(simbolo.tipoEntero, yychar, yyline, yytext()); }
"decimal" {return new Symbol(simbolo.tipoDecimal, yychar, yyline, yytext()); }
"caracter" {return new Symbol(simbolo.tipoCaracter, yychar, yyline, yytext()); }
"cadena" {return new Symbol(simbolo.tipoCadena, yychar, yyline, yytext()); }
"bool" {return new Symbol(simbolo.tipoBool, yychar, yyline, yytext()); }
"si" {return new Symbol(simbolo.si, yychar, yyline, yytext()); }
"sino" {return new Symbol(simbolo.sino, yychar, yyline, yytext()); }
"++" {return new Symbol(simbolo.masMas, yychar, yyline, yytext()); }
"--" {return new Symbol(simbolo.menosMenos, yychar, yyline, yytext()); }
"retornar" {return new Symbol(simbolo.retornar, yychar, yyline, yytext()); }
"llamar" {return new Symbol(simbolo.llamar, yychar, yyline, yytext()); }
"inicio" {return new Symbol(simbolo.inicio, yychar, yyline, yytext()); }
"incluir_hk" {return new Symbol(simbolo.incluirhk, yychar, yyline, yytext()); }
"llamarhk" {return new Symbol(simbolo.llamarhk, yychar, yyline, yytext()); }
"seleccion" {return new Symbol(simbolo.seleccion, yychar, yyline, yytext()); }
"caso" {return new Symbol(simbolo.caso, yychar, yyline, yytext()); }
"defecto" {return new Symbol(simbolo.defecto, yychar, yyline, yytext()); }
"para" {return new Symbol(simbolo.para, yychar, yyline, yytext()); }
"mientras" {return new Symbol(simbolo.mientras, yychar, yyline, yytext()); }
"hacer" {return new Symbol(simbolo.hacer, yychar, yyline, yytext()); }
"continuar" {return new Symbol(simbolo.continuar, yychar, yyline, yytext()); }
"terminar" {return new Symbol(simbolo.terminar, yychar, yyline, yytext()); }
"graphikar_funcion" {return new Symbol(simbolo.graphikar, yychar, yyline, yytext()); }
"imprimir" {return new Symbol(simbolo.imprimir, yychar, yyline, yytext()); }
"datos" {return new Symbol(simbolo.datos, yychar, yyline, yytext()); }
"donde" {return new Symbol(simbolo.donde, yychar, yyline, yytext()); }
"procesar" {return new Symbol(simbolo.procesar, yychar, yyline, yytext()); }
"dondecada" {return new Symbol(simbolo.dondecada, yychar, yyline, yytext()); }
"dondetodo" {return new Symbol(simbolo.dondetodo, yychar, yyline, yytext()); }
"llamarhk" {return new Symbol(simbolo.llamarhk, yychar, yyline, yytext()); }


":" {return new Symbol(simbolo.dosPuntos, yychar, yyline, yytext()); }


\" {yybegin(STRING);}   
"'" {yybegin(CARACTER);}

{entero} {return new Symbol(simbolo.entero, yychar, yyline, yytext());}
{id} {return new Symbol(simbolo.id, yychar, yyline,yytext());}
{decimal} {return new Symbol(simbolo.decimal, yychar, yyline, yytext());}
{archivogk} {return new Symbol(simbolo.archivogk, yychar, yyline, yytext());}

/* BLANCOS*/
[ \t\r\f\n]+ {/*Se ignoran*/}

/*Cualquier otro*/
. {javax.swing.JOptionPane.showMessageDialog(null,"Error Lexico: " + yytext());
    //Errores error = new Errores(yytext(),"No pertenece al lenguaje",yycolumn+1, yyline+1, "Error lexico");
ManejadorErrores.getInstancia().agregarErrorGraphik(new Errores(yytext() +" no pertenece al lenguaje.", "Lexico", (yycolumn+1),(yyline+1),TablaSimbolosGraphik.getInstancia().getArchivo()));
javax.swing.JOptionPane.showMessageDialog(null,yytext() + "no pertenece" + (yycolumn+1) + "fila "+(yyline+1) );
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