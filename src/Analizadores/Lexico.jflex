//Paquete e importaciones 
package Analizadores;

import java_cup.runtime.Symbol;
import java.util.ArrayList;
import java.util.LinkedList;
import Util.Utilidades;

%%
//Directivas

%class Lexico          // Nombre de la clase que genera JFlex
%public                // Para tener acceso desde otros paquetes
%line                  // Para registrar las líneas
%column                // Para registrar las columnas
%char                  // Llevar un conteo de caracteres
%cup                   // Habilita la integración con Cup
%unicode               // Reconocimiento de caracteres unicode
//%ignorecase 

%init{                 // Constructor del analizador
    yyline = 1; 
    yycolumn = 1; 
%init}  

// Expresiones regulares

BLANCOS = [ \t \r \n]+
COMENTARIO_LINEA = "#" .*
COMENTARIO_MULTILINEA = "<!"[^!]*"!>"

DIGITO = [0-9]
LETRA = [a-zA-ZÑñ]
ID = {LETRA}({LETRA}|{DIGITO}|"_")*
CARACTER = ([\x21-\x7E]) // Caracteres ASCII imprimibles (33-126) o (!-~)
INTERV = {CARACTER}("~"|" ~ "|"  ~  "){CARACTER} // a~z
LISTA = {CARACTER}(({BLANCOS})*","({BLANCOS})*{CARACTER})* // 1,2,3,4,5
LISTA_CONJ = ("{"{ID}"}") // {conjuntoA}
LISTA_ELEMENTOS = "{"({LISTA}|{INTERV})"}" // {1,2,3,4,5} {a,b,c,d,e} {a~z}

// Palabras Reservadas
CONJ = "CONJ"
OPERA = "OPERA"
EVALUAR = "EVALUAR"

// Signos (para expresiones)
LLAVE_IZQ = "{"
LLAVE_DER = "}"
PAREN_IZQ = "("
PAREN_DER = ")"
FLECHA = "->"
COMA = ","
PUNTO_Y_COMA = ";"
DOS_PUNTOS = ":"

// operaciones de conjuntos
UNION = "U"
INTERSECCION = "&"
COMPLEMENTO = "^"
DIFERENCIA = "-"


%eofval{
    return new Symbol(ParserSym.EOF);
%eofval}

%%

// Reglas Lexicas
<YYINITIAL> {

    // Comentarios
    {BLANCOS} {}
    {COMENTARIO_LINEA} {}
    {COMENTARIO_MULTILINEA} {}

    // Palabras reservadas
    {CONJ} { Util.Utilidades.agregarToken(yytext(),"CONJ" , yyline, yycolumn); return new Symbol(ParserSym.CONJ,yyline,yycolumn,yytext()); }
    {OPERA} { Util.Utilidades.agregarToken(yytext(),"OPERA" , yyline, yycolumn); return new Symbol(ParserSym.OPERA,yyline,yycolumn,yytext()); }
    {EVALUAR} { Util.Utilidades.agregarToken(yytext(),"EVALUAR" , yyline, yycolumn); return new Symbol(ParserSym.EVALUAR,yyline,yycolumn,yytext()); }

    // Signos
    {PAREN_IZQ} { Util.Utilidades.agregarToken(yytext(),"PAREN_IZQ" , yyline, yycolumn); return new Symbol(ParserSym.PAREN_IZQ,yyline,yycolumn,yytext()); }
    {PAREN_DER} { Util.Utilidades.agregarToken(yytext(),"PAREN_DER" , yyline, yycolumn); return new Symbol(ParserSym.PAREN_DER,yyline,yycolumn,yytext()); }
    {FLECHA} { Util.Utilidades.agregarToken(yytext(),"FLECHA" , yyline, yycolumn); return new Symbol(ParserSym.FLECHA,yyline,yycolumn,yytext()); }
    {COMA} { Util.Utilidades.agregarToken(yytext(),"COMA" , yyline, yycolumn); return new Symbol(ParserSym.COMA,yyline,yycolumn,yytext()); }
    {PUNTO_Y_COMA} { Util.Utilidades.agregarToken(yytext(),"PUNTO_Y_COMA" , yyline, yycolumn); return new Symbol(ParserSym.PUNTO_Y_COMA,yyline,yycolumn,yytext()); }
    {DOS_PUNTOS} { Util.Utilidades.agregarToken(yytext(),"DOS_PUNTOS" , yyline, yycolumn); return new Symbol(ParserSym.DOS_PUNTOS,yyline,yycolumn,yytext()); }
    {LLAVE_IZQ} { Util.Utilidades.agregarToken(yytext(),"LLAVE_IZQ" , yyline, yycolumn); return new Symbol(ParserSym.LLAVE_IZQ,yyline,yycolumn,yytext()); }
    {LLAVE_DER} { Util.Utilidades.agregarToken(yytext(),"LLAVE_DER" , yyline, yycolumn); return new Symbol(ParserSym.LLAVE_DER,yyline,yycolumn,yytext()); }

    // Operaciones de conjuntos
    {UNION} { Util.Utilidades.agregarToken(yytext(),"UNION" , yyline, yycolumn); return new Symbol(ParserSym.UNION,yyline,yycolumn,yytext()); }
    {INTERSECCION} { Util.Utilidades.agregarToken(yytext(),"INTERSECCION" , yyline, yycolumn); return new Symbol(ParserSym.INTERSECCION,yyline,yycolumn,yytext()); }
    {COMPLEMENTO} { Util.Utilidades.agregarToken(yytext(),"COMPLEMENTO" , yyline, yycolumn); return new Symbol(ParserSym.COMPLEMENTO,yyline,yycolumn,yytext()); }
    {DIFERENCIA} { Util.Utilidades.agregarToken(yytext(),"DIFERENCIA" , yyline, yycolumn); return new Symbol(ParserSym.DIFERENCIA,yyline,yycolumn,yytext()); }

    // Componentes lexicos
    {ID} { Util.Utilidades.agregarToken(yytext(),"ID" , yyline, yycolumn); return new Symbol(ParserSym.ID,yyline,yycolumn,yytext()); }
    {INTERV} { Util.Utilidades.agregarToken(yytext(),"INTERV" , yyline, yycolumn); return new Symbol(ParserSym.INTERV,yyline,yycolumn,yytext()); }
    {LISTA} { Util.Utilidades.agregarToken(yytext(),"LISTA" , yyline, yycolumn); return new Symbol(ParserSym.LISTA,yyline,yycolumn,yytext()); }
    {LISTA_CONJ} { Util.Utilidades.agregarToken(yytext(),"LISTA_CONJ" , yyline, yycolumn); return new Symbol(ParserSym.LISTA_CONJ,yyline,yycolumn,yytext()); }
    {LISTA_ELEMENTOS} { Util.Utilidades.agregarToken(yytext(),"LISTA_ELEMENTOS" , yyline, yycolumn); return new Symbol(ParserSym.LISTA_ELEMENTOS,yyline,yycolumn,yytext()); }

    // Manejo de errores
    . { Util.Utilidades.agregarError(yytext(), "ERROR LEXICO", yyline, yycolumn); System.err.println("Error léxico: Caracter inválido <" + yytext() + "> en la línea " + yyline + ", columna " + yycolumn); }
}