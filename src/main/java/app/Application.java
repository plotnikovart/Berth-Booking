package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // todo проверка перед сохранением фотки
    // todo поиск мест
    // todo оставление отзыва и рейтинга
    // todo почтовые уведомления
}
