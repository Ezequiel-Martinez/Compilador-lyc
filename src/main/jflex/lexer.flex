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

Conditional = if|IF
Else = else|ELSE
Write = write|WRITE
Not = not|NOT
WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+
Comment = {InicioComentario} ({AnyCharacter})* {FinComentario}
StringConstant = {StringDelimiter} ({AnyCharacterExceptDoubleQuotes})* {StringDelimiter}

%%


/* keywords */

<YYINITIAL> {
  /* reserved words */
  {Conditional}                             { return symbol(ParserSym.CONDITIONAL); }
  {Else}                                    { return symbol(ParserSym.ELSE); }
  {Write}                                   { return symbol(ParserSym.WRITE); }
  {Not}                                     { return symbol(ParserSym.NOT); }

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
  {Comment}                                 { return symbol(ParserSym.EOF); }

  {StringConstant}
      {
          if (yytext().length() > MAX_LENGTH)
            { throw new InvalidLengthException(yytext()); }
          else
            { return symbol(ParserSym.STRING_CONSTANT); }
      }



  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
