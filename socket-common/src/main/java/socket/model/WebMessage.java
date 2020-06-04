package socket.model;

import java.util.Date;

public class WebMessage {
    private ChatUser from;
    private ChatUser to;
    /**
     * TEXT 文本消息 PIC 贴图消息 SUCCESS 成功通知 FAILED 失败通知 WARNING 警告通知 HEART_BEAT心跳
     */
    private String type;
    /**
     * 文本消息
     */
    private String text;
    /**
     * 图片 贴图 base64
     */
    private String url;
    /**
     * 扩展消息 json string
     */
    private String ext;
    private Date createTime;

    public ChatUser getFrom() {
        return from;
    }

    public void setFrom(ChatUser from) {
        this.from = from;
    }

    public ChatUser getTo() {
        return to;
    }

    public void setTo(ChatUser to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
