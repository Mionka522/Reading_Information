import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        // Запись отладочного сообщения
        logger.debug("Это отладочное сообщение");

        // Запись информационного сообщения
        logger.info("Это информационное сообщение");

        // Запись предупреждающего сообщения
        logger.warn("Это предупреждающее сообщение");

        // Запись сообщения об ошибке
        try {
            int result = 10 / 0; // Генерируем исключение деления на ноль
        } catch (ArithmeticException e) {
            logger.error("Произошла ошибка: " + e.getMessage(), e);
        }
    }
}