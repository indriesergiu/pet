import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author sergiu.indrie
 */
public class FirstClass {

    static Logger logger = Logger.getLogger(FirstClass.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        logger.info("Hello Ant!");          // the old SysO-statement
    }
}
