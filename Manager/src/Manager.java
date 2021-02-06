import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.lang.reflect.Constructor;
import java.util.logging.*;

public class Manager {

    ManagerGrammar grammar;
    IReader reader;
    IWriter writer;
    IExecutor[] executors;
    void Run(String configName ,Logger logger)
    {
        grammar = new ManagerGrammar();
        SyntacticParser managerSyntacticParser = new SyntacticParser(logger, grammar);
        if (managerSyntacticParser.syntacticParsing(configName) != RC.CODE_SUCCESS)
            return;
        ManagerSemanticParser managerSemanticParser = new ManagerSemanticParser(logger);
        if(managerSemanticParser.FullSemanticAnalysis(grammar, managerSyntacticParser.getSyntacticResult())!= RC.CODE_SUCCESS)
            return;
        if (BuildExecutors(managerSemanticParser, logger)!= RC.CODE_SUCCESS)
            return;
        BuildPipeline();
        reader.execute(null);
    }

    RC BuildExecutors(ManagerSemanticParser managerSemanticParser, Logger logger)
    {
        Class<?> newClass;
        Constructor<?> constructor;
        RC errCode;
        String curClassName = null;
        try {
            curClassName = managerSemanticParser.getReaderName();
            newClass = Class.forName(curClassName);
            constructor = newClass.getDeclaredConstructor(Logger.class);
            reader =(IReader) constructor.newInstance(logger);
            reader.setInputStream(managerSemanticParser.getInputStream());
            if((errCode = reader.setConfig(managerSemanticParser.getReaderConfig()))!= RC.CODE_SUCCESS)
            {
                logger.severe(Log.OPENING_FILE.message + managerSemanticParser.getReaderConfig());
                return errCode;
            }
            curClassName = managerSemanticParser.getWriterName();
            newClass = Class.forName(curClassName);
            constructor = newClass.getDeclaredConstructor(Logger.class);
            writer =(IWriter)constructor.newInstance(logger);
            writer.setOutputStream(managerSemanticParser.getOutputStream());
            if((errCode = writer.setConfig(managerSemanticParser.getWriterConfig()))!= RC.CODE_SUCCESS)
            {
                logger.severe(Log.OPENING_FILE.message + managerSemanticParser.getWriterConfig());
                return errCode;
            }

            int executorNumber = managerSemanticParser.getExecutorNames().length;
            executors = new IExecutor[executorNumber];
            for(int i = 0; i < executorNumber; i++)
            {
                curClassName = managerSemanticParser.getExecutorNames()[i];
                newClass = Class.forName(curClassName);
                constructor = newClass.getDeclaredConstructor(Logger.class);
                executors[i] =(IExecutor)constructor.newInstance(logger);
                if((errCode = executors[i].setConfig(managerSemanticParser.getExecutorConfigs()[i]))!= RC.CODE_SUCCESS)
                {
                    logger.severe(Log.OPENING_FILE.message + managerSemanticParser.getExecutorConfigs()[i]);
                    return errCode;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            logger.severe(Log.WRONG_CLASS_NAME.message + curClassName);
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
        catch (Exception ex)
        {
            logger.severe(Log.PIPELINE.message);
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }

        return RC.CODE_SUCCESS;
    }

    void BuildPipeline()
    {
        int i = 0;
        reader.setConsumer(executors[0]);
        for(; i < executors.length - 1; i++)
        {
            executors[i].setConsumer(executors[i+1]);
        }
        executors[i].setConsumer(writer);
        writer.setProducer(executors[i]);
        for(; i > 0; i--)
        {
            executors[i].setProducer(executors[i-1]);
        }
        executors[0].setProducer(reader);
    }





}
