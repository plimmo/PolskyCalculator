package im.plimmo.tokenizer;

import im.plimmo.token.Token;
import java.util.List;

public interface Tokenizer {
    List<Token> tokenize(String expression);
}