package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"break"  	{ return new_symbol(sym.BREAK, yytext()); }
"class"  	{ return new_symbol(sym.CLASS, yytext()); }
"else"  	{ return new_symbol(sym.ELSE, yytext()); }
"const"		{ return new_symbol(sym.CONST, yytext()); }
"if"		{ return new_symbol(sym.IF, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read" 		{ return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends" 	{ return new_symbol(sym.EXTENDS, yytext()); }
"continue" 	{ return new_symbol(sym.CONTINUE, yytext()); }
"for" 		{ return new_symbol(sym.FOR, yytext()); }
"static" 	{ return new_symbol(sym.STATIC, yytext()); }
"namespace" { return new_symbol(sym.NAMESPACE, yytext()); }
"using"     { return new_symbol(sym.USING, yytext()); }



// Tokens - with predefined values
'.'		 	{ return new_symbol(sym.CHAR, yytext().charAt(1)); }
"true" 		{ return new_symbol(sym.BOOL, true); }
"false" 	{ return new_symbol(sym.BOOL, false); }


// Operators
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }
"^"			{ return new_symbol(sym.MAX, yytext()); }

"==" 		{ return new_symbol(sym.EQ, yytext()); }
"!=" 		{ return new_symbol(sym.NOT_EQ, yytext()); }
">" 		{ return new_symbol(sym.GR, yytext()); }
">=" 		{ return new_symbol(sym.GR_EQ, yytext()); }
"<" 		{ return new_symbol(sym.LESS, yytext()); }
"<=" 		{ return new_symbol(sym.LESS_EQ, yytext()); }

"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }

"=" 		{ return new_symbol(sym.ASSIGN, yytext()); }

"++" 		{ return new_symbol(sym.INC, yytext()); }
"--" 		{ return new_symbol(sym.DEC, yytext()); }

";" 		{ return new_symbol(sym.SEMI, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"." 		{ return new_symbol(sym.POINT, yytext()); }

"(" 		{ return new_symbol(sym.LEFT_ROUND, yytext()); }
")" 		{ return new_symbol(sym.RIGHT_ROUND, yytext()); }
"[" 		{ return new_symbol(sym.LEFT_SQUARE, yytext()); }
"]" 		{ return new_symbol(sym.RIGHT_SQUARE, yytext()); }
"{" 		{ return new_symbol(sym.LEFT_CURLY, yytext()); }
"}"			{ return new_symbol(sym.RIGHT_CURLY, yytext()); }

// Comments
"//" 			 { yybegin(COMMENT); }
<COMMENT> .		 { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

// Tokens - regular expressions

[0-9]+  						{ return new_symbol(sym.NUMBER, new Integer (yytext())); }
[a-zA-Z][a-zA-Z0-9_]*  	{ return new_symbol(sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+ (yyline+1) + " na poziciji " + yycolumn); }






