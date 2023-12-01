import java.security.InvalidParameterException;

public record Token(TokenType token, String value) {
  public enum TokenType {
    LBRACKET,
    RBRACKET,
    EQ,
    LT,
    GT,
    ADD,
    SUB,
    MUL,
    DIV,
    IF,
    WHILE,
    END,
    IDENT,
    NUM
  }

  /* Binds a valid value to NUM or IDENT token */
  public Token(TokenType token, String value) {
    this.token = token;
    boolean isValidValue = switch (token) {
      case IDENT -> validateIdentifier(value);
      case NUM   -> validateNumber(value);
      default    -> value == null;
    };

    if (!isValidValue) {
      throw new InvalidParameterException(
        value + " is not a valid value for " + token.name());
    }

    this.value = value;
  }

  /* Constructs a token with no value */
  public Token(TokenType token) {
    this(token, null);
  }

  /* Validates value to be a decimal number */
  private boolean validateNumber(String value) {
    if (value == null) {
      return false;
    }

    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /* Validates value to be a single character */
  private boolean validateIdentifier(String value) {
    if (value == null) {
      return false;
    }

    return value.length() == 1 && Character.isLetter(value.charAt(0));
  }
}
