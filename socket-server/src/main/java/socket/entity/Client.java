package socket.entity;

import org.yeauty.pojo.Session;

/**
 * @Author: zcz
 * @Date: 2019/5/16 16:56
 * @Version 1.0
 */
public class Client {
    private String phone;

    private Session session;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
