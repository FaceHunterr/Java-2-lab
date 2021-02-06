import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.RC;

import java.util.logging.Logger;

public class Executor implements IExecutor {
    private Logger logger; //логгер
    private ExecutorGrammar grammar; //грамматика экзекутора
    private IExecutable consumer, producer; //консумер и продьюсер
    private int buff_shift; //размер, на который увеличивается каждый байт

    Executor(Logger logger) {this.logger = logger;}

    @Override
    public RC setConfig(String configName) {
        RC errCode;
        grammar = new ExecutorGrammar();
        SyntacticParser syntacticParser = new SyntacticParser(logger, grammar);
        if ((errCode = syntacticParser.syntacticParsing(configName)) != RC.CODE_SUCCESS)
            return errCode;
        ExecutorSemanticParser semanticParser = new ExecutorSemanticParser(logger);
        if((errCode = semanticParser.FullSemanticAnalysis(grammar, syntacticParser.getSyntacticResult()) )!= RC.CODE_SUCCESS)
            return errCode;
        buff_shift = semanticParser.getBuff_shift();
        return errCode;
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

    @Override
    public RC setProducer(IExecutable producer) {
        if (producer == null) {
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        } else {
            this.producer = producer;
            return RC.CODE_SUCCESS;
        }
    }

    //изменяет каждый байт из bytes на величину, равную buff_shift, и запускает следующий экзекутор
    @Override
    public RC execute(byte[] bytes) {
        for(int i = 0; i < bytes.length; i++)
            bytes[i] += (byte)buff_shift;
        consumer.execute(bytes);
        return RC.CODE_SUCCESS;
    }
}
