import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class Writer implements IWriter {
    private Logger logger; //логгер
    private IExecutable producer;   //продюсер
    private FileOutputStream outputStream; //выходной поток
    private WriterGrammar grammar;  //грамматика райтера
    private int buff_size;  //размер выводимого буффера за раз

    Writer(Logger logger) {this.logger = logger;}

    @Override
    public RC setOutputStream(FileOutputStream fileOutputStream) {
        if(fileOutputStream == null)
            return RC.CODE_INVALID_OUTPUT_STREAM;
        else
            outputStream = fileOutputStream;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String configName) {
        RC errCode;
        grammar = new WriterGrammar();
        SyntacticParser syntacticParser = new SyntacticParser(logger, grammar);
        if ((errCode = syntacticParser.syntacticParsing(configName)) != RC.CODE_SUCCESS)
            return errCode;
        WriterSemanticParser semanticParser = new WriterSemanticParser(logger);
        if((errCode = semanticParser.FullSemanticAnalysis(grammar, syntacticParser.getSyntacticResult()) )!= RC.CODE_SUCCESS)
            return errCode;
        buff_size = semanticParser.getBuff_size();
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable producer) {
        if (producer == null) {
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        } else {
            this.producer = producer;
            return RC.CODE_SUCCESS;
        }
    }

    @Override
    public RC setConsumer(IExecutable consumer) {
        return RC.CODE_SUCCESS;
    }

    //выводит принятый буфер bytes в выходной поток
    @Override
    public RC execute(byte[] bytes) {
        if(buff_size <= 0)
        {
            logger.severe(Log.NO_BUFFER_SIZE_SPECIFIED.message);
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
        if(outputStream == null)
            return RC.CODE_INVALID_INPUT_STREAM;
        for(int i = 0, bytesLeft = bytes.length; bytesLeft > 0; i++, bytesLeft -= buff_size)
        {
            try {
                outputStream.write(bytes, i * buff_size, Integer.min(bytesLeft, buff_size));
            }catch (IOException ex)
            {
                logger.severe(Log.OUTPUT_STREAM.message);
                return RC.CODE_INVALID_INPUT_STREAM;
            }

        }
        return RC.CODE_SUCCESS;
    }
}
