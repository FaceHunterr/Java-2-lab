public class WriterGrammar extends Grammar{
    private final static String[] WriterGrammarTokens;
    static {
        WriterGrammar.Vocabulary[] vocabulary = WriterGrammar.Vocabulary.values();
        WriterGrammarTokens = new String[vocabulary.length];
        for (WriterGrammar.Vocabulary word : vocabulary)
            WriterGrammarTokens[word.ordinal()] = word.toString().toLowerCase();
    }
    enum Vocabulary
    {
        B_SIZE
    }

    WriterGrammar() {super(WriterGrammarTokens);}

}
