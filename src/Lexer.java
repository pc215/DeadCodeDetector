import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Lexer {
  /* Should be a singleton */
  private static final Lexer instance = new Lexer();
  private static final int IF_JUMP = 1;
  private static final int END_JUMP = 2;
  private static final int WHILE_JUMP = 4;
  private static final int MAX_LITERAL_SIZE = 5;
  private static final int VAR_NAME_SIZE = 1;
  private static final char DECIMAL_POINT = '.';

  private Lexer() {}

  public static Lexer getInstance() {
    return instance;
  }

  /* A tokenize function that takes a string and returns a list of Tokens */
  public List<Token> tokenize(String code) {
    List<Token> tokens = new LinkedList<>();
    Token token;
    for (int i = 0; i < code.length(); i++) {
      token = switch (code.charAt(i)) {
        case '(' -> new Token(Token.TokenType.LBRACKET);
        case ')' -> new Token(Token.TokenType.RBRACKET);
        case '<' -> new Token(Token.TokenType.LT);
        case '>' -> new Token(Token.TokenType.GT);
        case '=' -> new Token(Token.TokenType.EQ);
        case '+' -> new Token(Token.TokenType.ADD);
        case '-' -> new Token(Token.TokenType.SUB);
        case '*' -> new Token(Token.TokenType.MUL);
        case '/' -> new Token(Token.TokenType.DIV);
        default  -> getNonTrivialToken(i, code);
      };

      if (token == null) {
        continue;
      }

      /* Jump characters based on size of token,
         grammar may be extended in the future */
      i += switch (token.token()) {
        case IF    -> IF_JUMP;
        case END   -> END_JUMP;
        case WHILE -> WHILE_JUMP;
        case NUM   -> token.value().length() - 1;
        default    -> 0;
      };

      tokens.add(token);
    }
    return tokens;
  }

  private Token getNonTrivialToken(int i, String code) {
    Token token = null;
    if (Character.isLetter(code.charAt(i))) {
      token = getStringToken(i, code);
    } else if (Character.isDigit(code.charAt(i))) {
      token = getNumberToken(i, code);
    } else if (!Character.isWhitespace(code.charAt(i))) {
      throwInvalidCodeException(i, code);
    }
    return token;
  }

  /* Responsible for tokenizing NUM */
  private Token getNumberToken(int i, String code) {
    StringBuilder numberBuilder = new StringBuilder();
    boolean isDigit = true;
    boolean isDecimalPoint = false;
    while (isDigit || isDecimalPoint) {
      numberBuilder.append(code.charAt(i));
      i++;

      if (i >= code.length()) {
        break;
      }

      isDigit = Character.isDigit(code.charAt(i));
      isDecimalPoint = (code.charAt(i) == DECIMAL_POINT);
    }
    return new Token(Token.TokenType.NUM, numberBuilder.toString());
  }

  /* Responsible for tokenizing VAR, IF, END, WHILE */
  private Token getStringToken(int i, String code) {
    Token token;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(code.charAt(i));
    i++;

    while(i < code.length() && Character.isLetter(code.charAt(i))) {
      stringBuilder.append(code.charAt(i));
      token = switch (stringBuilder.toString()) {
        case "if"    -> new Token(Token.TokenType.IF);
        case "end"   -> new Token(Token.TokenType.END);
        case "while" -> new Token(Token.TokenType.WHILE);
        default      -> null;
      };

      if (token != null) {
        return token;
      }

      if (stringBuilder.length() >= MAX_LITERAL_SIZE) {
        throwInvalidCodeException(i, code);
      }

      i++;
    }

    
    if (stringBuilder.length() > VAR_NAME_SIZE) {
      throwInvalidCodeException(i, code);
    }

    token = new Token(Token.TokenType.IDENT, stringBuilder.toString());
    return token;
  }

  private void throwInvalidCodeException(int i, String code) {
    throw new NoSuchElementException(
      "Invalid code at: \n" + " ".repeat(i) + "|" + '\n' + code);
  }
}
