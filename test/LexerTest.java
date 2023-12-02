import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LexerTest {

  @Test
  public void whiteSpacesAndNewLinesAreIgnored() {
    assertEquals(Lexer.getInstance().tokenize("    \n\n").size(), 0);
  }

  @Test
  public void basicLiteralsMapDirectlyToTokens() {
    List<Token> tokens = Lexer.getInstance().tokenize(" +-*/\n()=<> ");
    assertEquals(tokens.get(0).token(), Token.TokenType.ADD);
    assertEquals(tokens.get(1).token(), Token.TokenType.SUB);
    assertEquals(tokens.get(2).token(), Token.TokenType.MUL);
    assertEquals(tokens.get(3).token(), Token.TokenType.DIV);
    assertEquals(tokens.get(4).token(), Token.TokenType.LBRACKET);
    assertEquals(tokens.get(5).token(), Token.TokenType.RBRACKET);
    assertEquals(tokens.get(6).token(), Token.TokenType.EQ);
    assertEquals(tokens.get(7).token(), Token.TokenType.LT);
    assertEquals(tokens.get(8).token(), Token.TokenType.GT);
  }

  @Test
  public void stringsAreConvertedToAppropriateToken() {
    List<Token> tokens = Lexer.getInstance().tokenize("while end if if a i");
    assertEquals(tokens.get(0).token(), Token.TokenType.WHILE);
    assertEquals(tokens.get(1).token(), Token.TokenType.END);
    assertEquals(tokens.get(2).token(), Token.TokenType.IF);
    assertEquals(tokens.get(3).token(), Token.TokenType.IF);
    assertEquals(tokens.get(4).token(), Token.TokenType.IDENT);
    assertEquals(tokens.get(4).value(), "a");
    assertEquals(tokens.get(5).token(), Token.TokenType.IDENT);
    assertEquals(tokens.get(5).value(), "i");
  }

  @Test
  public void decimalNumbersAreConvertedToAppropriateToken() {
    List<Token> tokens = Lexer.getInstance().tokenize("while 123 < 123.45 15");
    assertEquals(tokens.get(0).token(), Token.TokenType.WHILE);
    assertEquals(tokens.get(1).token(), Token.TokenType.NUM);
    assertEquals(tokens.get(1).value(), "123");
    assertEquals(tokens.get(2).token(), Token.TokenType.LT);
    assertEquals(tokens.get(3).token(), Token.TokenType.NUM);
    assertEquals(tokens.get(3).value(), "123.45");
    assertEquals(tokens.get(4).token(), Token.TokenType.NUM);
    assertEquals(tokens.get(4).value(), "15");
  }

  @Test
  public void detectsSimpleSyntaxErrors() {
    assertThrows(NoSuchElementException.class, () ->
      Lexer.getInstance().tokenize("whilesif123"));
    assertThrows(NoSuchElementException.class, () ->
      Lexer.getInstance().tokenize("while if 12@3"));
    assertThrows(NoSuchElementException.class, () ->
      Lexer.getInstance().tokenize("abcdergh"));
    assertThrows(NoSuchElementException.class, () ->
      Lexer.getInstance().tokenize("abc"));
    assertThrows(NoSuchElementException.class, () ->
      Lexer.getInstance().tokenize("while if 123 whil"));
  }
}
