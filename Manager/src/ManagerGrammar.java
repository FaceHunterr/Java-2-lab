import ru.spbstu.pipeline.BaseGrammar;

import java.util.Arrays;
import java.util.List;

class ManagerGrammar extends Grammar {
    private final static String[] managerGrammarTokens;
    //private final static String enumerationDelimiter = ",";
    static {
        Vocabulary[] vocabulary = Vocabulary.values();
        managerGrammarTokens = new String[vocabulary.length];
        for (Vocabulary word : vocabulary)
            managerGrammarTokens[word.ordinal()] = word.toString().toLowerCase();
    }
    enum Vocabulary
    {
        INPUT_FILE,
        OUTPUT_FILE,
        READER_CONFIG,
        WRITER_CONFIG,
        EXECUTOR_CONFIGS,
        READER_NAME,
        WRITER_NAME,
        EXECUTORS
    }

    ManagerGrammar()
    {
        super(managerGrammarTokens);
    }
    //static List<String> GetTokenList() {return Arrays.asList(grammarTokens);}
    //String getDelimiter() {return " *[" + delimiter() + "]+ *";}
    //String getWordDelimiter() {return " *[" + enumerationDelimiter + "]+ *";}
}
