package socket.utils;

import org.yeauty.pojo.Session;
import socket.model.ChatUser;
import socket.entity.Client;

import java.util.*;

/**
 * @Author: zcz
 * @Date: 2019/5/9 9:35
 * @Version 1.0
 */
public class ClientManager {
    private static volatile ClientManager singleton;

    private Map<String, List<String>> userGroupMap = new HashMap<String, List<String>>();
    private Map<String, Session> clientMap = new HashMap<String, Session>();

    private static Map<String, ChatUser> kcUserChannelIdMap = new HashMap<String, ChatUser>();
    private static Map<String, Session> phoneSessionMap = new HashMap<String, Session>();

    public ClientManager() {

    }

    public void remove(String userKey){
        this.clientMap.remove(userKey);
    }

    public void add(String group,String userKey,Session session){
        List userGroup = userGroupMap.get(group);
        if(null==userGroup){
            userGroup = new ArrayList();
        }
        userGroup.add(userKey);
        clientMap.put(userKey,session);
    }

    public void cacheIdUser(String sessionId, ChatUser user){
        kcUserChannelIdMap.put(sessionId, user);
    }

    public static ClientManager getInstance() {
        if (singleton == null) {
            synchronized (ClientManager.class) {
                if (singleton == null) {
                    singleton = new ClientManager();
                }
            }
        }
        return singleton;
    }

    public void removeById(String sessionId) {
        kcUserChannelIdMap.remove(sessionId);
    }

    public void cachePhoneSession(String phone, Session session) {
        phoneSessionMap.put(phone, session);
    }

    public Session getSessionByPhone(String phone) {
       return phoneSessionMap.get(phone);
    }

    public List<Client> getSessionList(){
        List<Client> list = new ArrayList<Client>();
        final Iterator<Map.Entry<String, Session>> entries = phoneSessionMap.entrySet().iterator();
        while (entries.hasNext()) {
            final Map.Entry<String, Session> entry = entries.next();
            list.add(new Client(){{
                setPhone(entry.getKey());
                setSession(entry.getValue());
            }});
        }
        return list;
    }

}
