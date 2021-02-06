import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class Reader implements IReader {
    private FileInputStream inputStream; //входящий поток
    private IExecutable consumer;   //консумер
    private ReaderGrammar grammar; //грамматика ридера
    private int buff_size;  //размер считываемого буффера за раз
    private Logger logger;  //логгер

    Reader(Logger logger)
    {
        super();
        this.logger = logger;
    }

    @Override
    public RC setInputStream(FileInputStream fileInputStream) {
        if(fileInputStream == null)
            return RC.CODE_INVALID_INPUT_STREAM;
        else
            inputStream = fileInputStream;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String configName) {
        RC errCode;
        grammar = new ReaderGrammar();
        SyntacticParser syntacticParser = new SyntacticParser(logger, grammar);
        if ((errCode = syntacticParser.syntacticParsing(configName)) != RC.CODE_SUCCESS)
            return errCode;
        ReaderSemanticParser semanticParser = new ReaderSemanticParser(logger);
        if((errCode = semanticParser.FullSemanticAnalysis(grammar, syntacticParser.getSyntacticResult()) )!= RC.CODE_SUCCESS)
            return errCode;
        buff_size = semanticParser.getBuff_size();
        return errCode;
    }

    @Override
    public RC setProducer(IExecutable iExecutable) {
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConsumer(IExecutable consumer) {
        if (consumer == null) {
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        } else {
            this.consumer = consumer;
            return RC.CODE_SUCCESS;
        }
    }

    //считывает данные с входящего потока и запускает конвейер
    @Override
    public RC execute(byte[] bytes) {
        if(buff_size <= 0)
        {
            logger.severe(Log.NO_BUFFER_SIZE_SPECIFIED.message);
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
        if(inputStream == null)
            return RC.CODE_INVALID_INPUT_STREAM;
        byte[] buffStream = new byte[buff_size];
        int readBytes;
        byte[] incompleteBuffer;
        for (int cycle = 0;; cycle++)
        {
            try {
                readBytes = inputStream.read(buffStream);
                if(readBytes == -1)
                    return RC.CODE_SUCCESS;
                if(readBytes < buff_size)
                {
                    incompleteBuffer = new byte[readBytes];
                    System.arraycopy(buffStream, 0, incompleteBuffer, 0, readBytes);
                    if(consumer.execute(incompleteBuffer) != RC.CODE_SUCCESS)
                        return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
                    break;
                }
                else
                 if(consumer.execute(buffStream) != RC.CODE_SUCCESS)
                     return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
            }
            catch (IOException ex)
            {
                logger.severe(Log.INPUT_STREAM.message);
                return RC.CODE_INVALID_INPUT_STREAM;
            }

        }
        return RC.CODE_SUCCESS;
    }

}
