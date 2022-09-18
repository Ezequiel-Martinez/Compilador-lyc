package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
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
Comment = {InicioComentario} ({AnyCharacter})* {FinComentario}
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
  {Not}                                     { return symbol(ParserSym.NOT); }
  {Init}                                    { return symbol(ParserSym.INIT); }
  {Float}                                   { return symbol(ParserSym.FLOAT); }
  {StringDataType}                          { return symbol(ParserSym.STRING_DATA_TYPE); }
  {Int}                                     { return symbol(ParserSym.INT); }
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
          else
            { return symbol(ParserSym.IDENTIFIER, yytext()); }
      }

  /* Constants */
  {IntegerConstant}
      {
          if(Long.valueOf(yytext()) > Integer.MAX_VALUE || (Long.valueOf(yytext()) < Integer.MIN_VALUE))
            {
                throw new InvalidIntegerException(yytext()); }
          else
            { return symbol(ParserSym.INTEGER_CONSTANT, yytext()); }
      }

  {FloatConstant}                           { return symbol(ParserSym.FLOAT_CONSTANT, yytext()); }



  /* operators */
  {Plus}                                    { return symbol(ParserSym.PLUS); }
  {Sub}                                     { return symbol(ParserSym.SUB); }
  {Mult}                                    { return symbol(ParserSym.MULT); }
  {Div}                                     { return symbol(ParserSym.DIV); }
  {Assig}                                   { return symbol(ParserSym.ASSIG); }
  {Greater}                                 { return symbol(ParserSym.GREATER); }
  {Less}                                    { return symbol(ParserSym.LESS); }
  {GreaterOrEqual}                          { return symbol(ParserSym.GREATER_OR_EQUAL); }
  {LessOrEqual}                             { return symbol(ParserSym.LESS_OR_EQUAL); }
  {Distinct}                                { return symbol(ParserSym.DISTINCT); }
  {Equal}                                   { return symbol(ParserSym.EQUAL); }
  {And}                                     { return symbol(ParserSym.AND); }
  {Or}                                      { return symbol(ParserSym.OR); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {OpenCurlyBrace}                          { return symbol(ParserSym.OPEN_CURLY_BRACE); }
  {CloseCurlyBrace}                         { return symbol(ParserSym.CLOSE_CURLY_BRACE); }
  {DoubleDot}                               { return symbol(ParserSym.DOUBLE_DOT); }
  {Comma}                                   { return symbol(ParserSym.COMMA); }
  {Comment}                                 { /* ignore */ }

  {StringConstant}
      {
          if (yytext().length() > 40)
            { throw new InvalidLengthException(yytext()); }
          else
            { return symbol(ParserSym.STRING_CONSTANT); }
      }



  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
