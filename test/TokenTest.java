import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class TokenTest {

  @Test
  public void createNonBoundTokensWithSingleParameter() {
    Token token = new Token(Token.TokenType.EQ);
    assertEquals(token.token(), Token.TokenType.EQ);
    assertNull(token.value());
  }

  @Test
  public void throwExceptionWhenNonBoundCreatedWithNumOrIdent() {
    assertThrows(InvalidParameterException.class, () ->
      new Token(Token.TokenType.NUM));
    assertThrows(InvalidParameterException.class, () ->
      new Token(Token.TokenType.IDENT));
  }

  @Test
  public void createBoundTokensWithTwoParameters() {
    Token token = new Token(Token.TokenType.NUM, "123");
    assertEquals(token.token(), Token.TokenType.NUM);
    assertEquals(token.value(), "123");

    token = new Token(Token.TokenType.IDENT, "a");
    assertEquals(token.token(), Token.TokenType.IDENT);
    assertEquals(token.value(), "a");
  }

  @Test
  public void validateNumAndIdentTokenAndThrowExceptionIfInvalid() {
    assertThrows(InvalidParameterException.class, () ->
      new Token(Token.TokenType.NUM, "abc"));
    assertThrows(InvalidParameterException.class, () ->
      new Token(Token.TokenType.IDENT, "abc"));
  }
}