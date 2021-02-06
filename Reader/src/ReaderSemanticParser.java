import ru.spbstu.pipeline.RC;

import java.util.HashMap;
import java.util.logging.Logger;

public class ReaderSemanticParser {
    private Logger logger;
    private int buff_size;
    ReaderSemanticParser(Logger logger) {this.logger = logger;}
    public RC FullSemanticAnalysis(ReaderGrammar grammar, HashMap<String, String[]> SyntacticResult)
    {
        for(String semantic: SyntacticResult.keySet())
        {
            String[] elements = SyntacticResult.get(semantic);
            if(grammar.token(ReaderGrammar.Vocabulary.B_SIZE.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                try {
                    buff_size = Integer.parseInt(elements[0]);
                }
                catch (Exception ex)
                {
                    logger.severe(Log.INCORRECT_DATA_FORMAT.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
            }
        }
        return RC.CODE_SUCCESS;
    }

    public int getBuff_size() {
        return buff_size;
    }
}
