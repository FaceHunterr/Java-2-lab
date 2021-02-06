
public class ReaderGrammar extends Grammar {
    private final static String[] ReaderGrammarTokens;
    static {
        ReaderGrammar.Vocabulary[] vocabulary = ReaderGrammar.Vocabulary.values();
        ReaderGrammarTokens = new String[vocabulary.length];
        for (ReaderGrammar.Vocabulary word : vocabulary)
            ReaderGrammarTokens[word.ordinal()] = word.toString().toLowerCase();
    }
    enum Vocabulary
    {
        B_SIZE
    }

    ReaderGrammar() {super(ReaderGrammarTokens);}
}
