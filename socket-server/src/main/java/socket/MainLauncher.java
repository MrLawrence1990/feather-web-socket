package socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Created by zcz on 2019/05/07.
 */
@SpringBootApplication(scanBasePackages = "socket")
public class MainLauncher {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MainLauncher.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
