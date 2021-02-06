import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class ManagerSemanticParser {
    private FileInputStream inputStream;
    private FileOutputStream outputStream;
    private String readerConfig;
    private String writerConfig;
    private String[] executorConfigs;
    private String readerName;
    private String writerName;
    private String[] executorNames;
    private Logger logger;

    ManagerSemanticParser(Logger logger) {this.logger = logger;}
    public RC FullSemanticAnalysis(ManagerGrammar grammar, HashMap<String, String[]> SyntacticResult)
    {
        for(String semantic: SyntacticResult.keySet())
        {
            String[] elements = SyntacticResult.get(semantic);
            if(grammar.token(ManagerGrammar.Vocabulary.INPUT_FILE.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                try {inputStream = new FileInputStream(elements[0]);}
                catch (FileNotFoundException ex)
                {
                    logger.severe(Log.OPENING_FILE.message + elements[0]);
                    return RC.CODE_INVALID_INPUT_STREAM;
                }
            } else if (grammar.token(ManagerGrammar.Vocabulary.OUTPUT_FILE.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                try {outputStream = new FileOutputStream(elements[0]);}
                catch (FileNotFoundException ex)
                {
                    logger.severe(Log.OPENING_FILE.message + elements[0]);
                    return RC.CODE_INVALID_OUTPUT_STREAM;
                }
            } else if (grammar.token(ManagerGrammar.Vocabulary.READER_CONFIG.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                readerConfig = elements[0];
            }
            else if (grammar.token(ManagerGrammar.Vocabulary.WRITER_CONFIG.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                writerConfig = elements[0];
            }
            else if (grammar.token(ManagerGrammar.Vocabulary.READER_NAME.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + grammar);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                readerName = elements[0];
            }
            else if (grammar.token(ManagerGrammar.Vocabulary.WRITER_NAME.ordinal()).equals(semantic))
            {
                if(elements.length != 1)
                {
                    logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + semantic);
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
                }
                writerName = elements[0];
            }
            else if (grammar.token(ManagerGrammar.Vocabulary.EXECUTOR_CONFIGS.ordinal()).equals(semantic))
            {
                executorConfigs = elements;
            }
            else if (grammar.token(ManagerGrammar.Vocabulary.EXECUTORS.ordinal()).equals(semantic))
            {
                executorNames = elements;
            }
        }
        if(executorNames.length != executorConfigs.length)
        {
            logger.severe(Log.EXECUTORS_AND_CONFIGS.message);
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public FileInputStream getInputStream() {
        return inputStream;
    }

    public FileOutputStream getOutputStream() {
        return outputStream;
    }

    public String getReaderConfig() {
        return readerConfig;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getWriterConfig() {
        return writerConfig;
    }

    public String getWriterName() {
        return writerName;
    }

    public String[] getExecutorConfigs() {
        return executorConfigs;
    }

    public String[] getExecutorNames() {
        return executorNames;
    }
}
