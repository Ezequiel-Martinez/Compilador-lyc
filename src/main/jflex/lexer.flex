package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.table.SymbolTableManager;
import static lyc.compiler.constants.Constants.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
  StringBuffer stringBuffer;
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

Plus = "+"
Mult = "*"
Sub = "-"
Div = "/"
Assig = "="
Greater = ">"
Less = "<"
GreaterOrEqual = ">="
LessOrEqual = "<="
Distinct = "!="
Equal = "=="
And = "&"
Or = "||"
DoubleDot = ":"
Comma = ","
Dot = "."
OpenBracket = "("
CloseBracket = ")"
OpenCurlyBrace = "{"
CloseCurlyBrace = "}"
Letter = [a-zA-Z]
Digit = [0-9]
InicioComentario = "/*"
FinComentario = "*/"
AnyCharacter = [^]
AnyCharacterExceptDoubleQuotes = [^\"]
StringDelimiter = "\""

While = while|WHILE
Conditional = if|IF
Else = else|ELSE
Write = write|WRITE
Read = read|READ
Not = not|NOT
Init = init|INIT
Float = Float
Int = Int
StringDataType = String
Do = do|DO
Case = case|CASE
Default = default|DEFAULT
EndDo = enddo|ENDDO
Repeat = repeat|REPEAT

WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Sub}?{Digit}+

Comment = {InicioComentario} [^*] ~{FinComentario} | {InicioComentario} "*"+ "/"

StringConstant = {StringDelimiter} ({AnyCharacterExceptDoubleQuotes})* {StringDelimiter}
FloatConstant = ({Dot}{Digit}+) | ({Digit}+{Dot}) | ({Digit}+{Dot}{Digit}+)

%%


/* keywords */

<YYINITIAL> {
  /* reserved words */
  {While}                                   { return symbol(ParserSym.WHILE); }
  {Conditional}                             { return symbol(ParserSym.CONDITIONAL); }
  {Else}                                    { return symbol(ParserSym.ELSE); }
  {Write}                                   { return symbol(ParserSym.WRITE); }
  {Read}                                    { return symbol(ParserSym.READ); }
  {Not}                                     { return symbol(ParserSym.NOT, yytext()); }
  {Init}                                    { return symbol(ParserSym.INIT); }
  {Float}                                   { return symbol(ParserSym.FLOAT, yytext()); }
  {StringDataType}                          { return symbol(ParserSym.STRING_DATA_TYPE, yytext()); }
  {Int}                                     { return symbol(ParserSym.INT, yytext()); }
  {Do}                                      { return symbol(ParserSym.DO); }
  {Case}                                    { return symbol(ParserSym.CASE); }
  {Default}                                 { return symbol(ParserSym.DEFAULT); }
  {EndDo}                                   { return symbol(ParserSym.ENDDO); }
  {Repeat}                                  { return symbol(ParserSym.REPEAT); }

  /* identifiers */
  {Identifier}
      {
          if (yytext().length() > MAX_LENGTH)
            { throw new InvalidLengthException(yytext()); }

          if(!SymbolTableManager.existsInTable(yytext()))
          {
            SymbolEntry entry = new SymbolEntry(yytext());
            SymbolTableManager.insertInTable(entry);
          }

          return symbol(ParserSym.IDENTIFIER, yytext());
      }

  /* Constants */
  {IntegerConstant}
      {
          if(Long.valueOf(yytext()) > Integer.MAX_VALUE || (Long.valueOf(yytext()) < Integer.MIN_VALUE))
            { throw new InvalidIntegerException(yytext()); }

          if(!SymbolTableManager.existsInTable(yytext()))
          {
            SymbolEntry entry = new SymbolEntry("_"+yytext(), DataType.INTEGER_CONS, yytext());
            SymbolTableManager.insertInTable(entry);
          }

          return symbol(ParserSym.INTEGER_CONSTANT, yytext());
      }

  {FloatConstant}
      {
          String[] num = yytext().split("\\.");
          String mantissa = null;
          String exp = null;

          if (yytext().charAt(0) == '.')
          {
              mantissa = num[0];
          }
          else if (yytext().charAt(yytext().length() - 1) == '.')
          {
              exp = num[0];
          }
          else
          {
            exp = num[0];
            mantissa = num[1];
          }

        if (exp != null)
        {
            if (exp.length() > 0)
            {
               if (Integer.parseInt(exp) > 9999)
                   throw new InvalidLengthException("Exponent out of range: " + yytext());
            }
        }

        if (mantissa != null)
        {
            if (mantissa.length() > 0)
            {
                if (Integer.parseInt(mantissa) > 16777216)
                    throw new InvalidLengthException("Mantissa out of range: " + yytext());
            }
        }

        if (!SymbolTableManager.existsInTable(yytext()))
        {
            SymbolEntry entry = new SymbolEntry("_" + yytext(), DataType.FLOAT_CONS, yytext());
            SymbolTableManager.insertInTable(entry);
        }

        return symbol(ParserSym.FLOAT_CONSTANT, yytext());

      }



  /* operators */
  {Plus}                                    { return symbol(ParserSym.PLUS, yytext()); }
  {Sub}                                     { return symbol(ParserSym.SUB, yytext()); }
  {Mult}                                    { return symbol(ParserSym.MULT, yytext()); }
  {Div}                                     { return symbol(ParserSym.DIV, yytext()); }
  {Assig}                                   { return symbol(ParserSym.ASSIG, yytext()); }
  {Greater}                                 { return symbol(ParserSym.GREATER, yytext()); }
  {Less}                                    { return symbol(ParserSym.LESS, yytext()); }
  {GreaterOrEqual}                          { return symbol(ParserSym.GREATER_OR_EQUAL, yytext()); }
  {LessOrEqual}                             { return symbol(ParserSym.LESS_OR_EQUAL, yytext()); }
  {Distinct}                                { return symbol(ParserSym.DISTINCT, yytext()); }
  {Equal}                                   { return symbol(ParserSym.EQUAL, yytext()); }
  {And}                                     { return symbol(ParserSym.AND, yytext()); }
  {Or}                                      { return symbol(ParserSym.OR, yytext()); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET, yytext()); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET, yytext()); }
  {OpenCurlyBrace}                          { return symbol(ParserSym.OPEN_CURLY_BRACE, yytext()); }
  {CloseCurlyBrace}                         { return symbol(ParserSym.CLOSE_CURLY_BRACE, yytext()); }
  {DoubleDot}                               { return symbol(ParserSym.DOUBLE_DOT, yytext()); }
  {Comma}                                   { return symbol(ParserSym.COMMA, yytext()); }

  {StringConstant}
      {
          stringBuffer = new StringBuffer(yytext());

          if (yytext().length() > STRING_MAX_LENGTH + 2) // las comillas cuentan como dos caracteres
            { throw new InvalidLengthException("String " + yytext() + " supera la longitud máxima permitida\nLongitud de string = " + yytext().length() + "\nLongitud máxima permitida = " + STRING_MAX_LENGTH ); }

          stringBuffer.replace(0, 1, "");
          stringBuffer.replace(stringBuffer.length()-1,stringBuffer.length(), ""); //trim extra quotes

          if(!SymbolTableManager.existsInTable(yytext()))
          {
            SymbolEntry entry = new SymbolEntry("_" + stringBuffer.toString(), DataType.STRING_CONS, stringBuffer.toString(), Integer.toString(stringBuffer.length()));
            SymbolTableManager.insertInTable(entry);
          }

          return symbol(ParserSym.STRING_CONSTANT, yytext());


      }



  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

    {Comment}                                 { /* ignore */ }


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
