public class ExecutorGrammar extends Grammar{
    private final static String[] ExecutorGrammarTokens;
    static {
        ExecutorGrammar.Vocabulary[] vocabulary = ExecutorGrammar.Vocabulary.values();
        ExecutorGrammarTokens = new String[vocabulary.length];
        for (ExecutorGrammar.Vocabulary word : vocabulary)
            ExecutorGrammarTokens[word.ordinal()] = word.toString().toLowerCase();
    }

    enum Vocabulary
    {
        B_SHIFT
    }

    ExecutorGrammar() {super(ExecutorGrammarTokens);}
}
