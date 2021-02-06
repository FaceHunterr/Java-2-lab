import ru.spbstu.pipeline.BaseGrammar;

import java.util.Arrays;
import java.util.List;

public class Grammar extends BaseGrammar {
    private final static String enumerationDelimiter = ",";
    private final String[] grammarTokens;

    Grammar(String[] tokens)
    {
        super(tokens);
        grammarTokens = tokens;
    }
    List<String> GetTokenList() {return Arrays.asList(grammarTokens);}
    String getDelimiter() {return " *[" + delimiter() + "]+ *";}
    String getWordDelimiter() {return " *[" + enumerationDelimiter + "]+ *";}
}
