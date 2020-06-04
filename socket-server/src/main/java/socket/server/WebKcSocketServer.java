package socket.server;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import socket.model.ChatUser;
import socket.entity.Client;
import socket.model.WebMessage;
import socket.utils.ClientManager;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.*;

@Component
@ServerEndpoint(port = 19988)
public class WebKcSocketServer {

    @Autowired
    private ClientManager clientManager;
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    private static Map<String, Session> kcUserSessionMap = new HashMap<String, Session>();
    private static Map<String, List<ChatUser>> groupUserMap = new HashMap<String, List<ChatUser>>();
    private static Map<String, String> sessionGroupMap = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        System.out.println("new connection ");
        String phone = parameterMap.getParameter("phone");
        String group = parameterMap.getParameter("group");
        kcUserSessionMap.put(phone, session);
        clientManager.cachePhoneSession(phone, session);
        ChatUser user = new ChatUser();
        user.setPhone(phone);
        user.setGroupId(group);
        user.setSessionId(session.id().asShortText());
        clientManager.cacheIdUser(session.id().asShortText(), user);
        if (!StringUtils.isEmpty(group)) {
            //如果客户端存在分组 记录分组
            List groupUsers = groupUserMap.get(group);
            if (null == groupUsers) {
                groupUsers = new ArrayList<ChatUser>();
            }
            groupUsers.add(user);
            groupUserMap.put(group, groupUsers);
            sessionGroupMap.put(session.id().asShortText(), group);
        }
        addOnlineCount();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String sessionId = session.id().asShortText();
        clientManager.removeById(sessionId);
        if (null != sessionGroupMap.get(sessionId)) {
            List<ChatUser> groupUsers = groupUserMap.get(sessionGroupMap.get(sessionId));
            for (ChatUser user : groupUsers) {
                if (user.getSessionId().equals(sessionId)) {
                    groupUsers.remove(user);
                    break;
                }
            }
            if (groupUsers.size() == 0) {
                groupUserMap.remove(sessionGroupMap.get(sessionId));
                sessionGroupMap.remove(sessionId);
            } else {
                groupUserMap.put(sessionGroupMap.get(sessionId), groupUsers);
            }
        }
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        WebMessage msg = JSONObject.parseObject(message, WebMessage.class);
        if(msg.getType().equals("HEART_BEAT")){
            msg.setText("心跳回复");
            session.sendText(JSONObject.toJSONString(msg));
        }else{
            if (null != msg.getTo().getGroupId()) {
                //群发
                List<ChatUser> users = groupUserMap.get(msg.getTo().getGroupId());
                if(null==users||users.size()==0){
                    return;
                }
                for (ChatUser user : users) {
                    msg.setTo(user);
                    sendBroadCast(msg);
                }
            } else {
                sendMsg(msg);
            }
        }
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebKcSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebKcSocketServer.onlineCount--;
    }

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    private boolean sendMsg(WebMessage message) {
        if (null != message.getTo().getPhone()) {
            Session session = clientManager.getSessionByPhone(message.getTo().getPhone());
            if (null != session) {
                session.sendText(JSONObject.toJSONString(message));
                return true;
            }
        }
        return false;
    }

    /**
     * 群发消息
     *
     * @param message
     * @return
     */
    private void sendBroadCast(WebMessage message) {
        for(Client client:clientManager.getSessionList()){
            message.getTo().setPhone(client.getPhone());
            sendMsg(message);
        }
    }


}
