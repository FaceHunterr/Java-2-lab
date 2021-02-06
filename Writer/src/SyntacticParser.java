import ru.spbstu.pipeline.RC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

class SyntacticParser {
    private HashMap<String, String[]> SyntacticResult = new HashMap<String, String[]>();
    private Grammar grammar;
    private Logger logger;

    SyntacticParser(Logger logger, Grammar grammar)
    {
        this.logger = logger;
        this.grammar = grammar;
    }

    RC syntacticParsing(String configFileName)
    {
        BufferedReader buff = null;
        String[] splitLine;
        final List<String> grammarTokenList = grammar.GetTokenList();
        try {
            buff = new BufferedReader(new FileReader(configFileName));
            for(String line = buff.readLine(); line != null; line = buff.readLine())
            {
                if(line.length() == 0)
                    continue;

                splitLine = line.split(grammar.getDelimiter(), 2);

                if(grammarTokenList.contains(splitLine[0].toLowerCase()))
                {
                    if(splitLine.length < 2 || splitLine[1].isEmpty())
                    {
                        logger.severe(Log.WRONG_NUM_OF_ELEMENTS.message + splitLine[0]);
                        return RC.CODE_CONFIG_GRAMMAR_ERROR;
                    }
                    SyntacticResult.put(splitLine[0].toLowerCase(), splitLine[1].split(grammar.getWordDelimiter()));
                }
            }
        }catch (FileNotFoundException ex)
        {
            logger.severe(Log.OPENING_FILE.message + configFileName);
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        }
        catch (IOException ex)
        {
            logger.severe(Log.READING_FILE.message + configFileName);
        }
        finally {
            try
            {
                if(buff != null)
                    buff.close();
            }
            catch (IOException ex)
            {
                logger.info(Log.CLOSING_BUFFER.message);
            }
        }
        if(SyntacticResult.size() < grammarTokenList.size())
        {
            logger.severe(Log.NOT_ENOUGH_PARAMETERS.message);
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public HashMap<String, String[]> getSyntacticResult()
    {
        if(SyntacticResult.size() == 0)
            return null;
        return SyntacticResult;
    }
}
