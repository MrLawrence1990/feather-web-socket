package socket.config;

import socket.utils.ClientManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zcz
 * @Date: 2019/5/9 9:28
 * @Version 1.0
 */
@Configuration
public class ClientManagement {
    @Bean
    public ClientManager clientManager() {
        return new ClientManager();
    }
}
