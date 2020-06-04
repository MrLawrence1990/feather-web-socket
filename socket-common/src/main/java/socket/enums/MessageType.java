package socket.enums;


public enum MessageType {
    //TEXT 文本消息 PIC 贴图消息 SUCCESS 成功通知 FAILED 失败通知 WARNING 警告通知
    TEXT("TEXT", "文本消息"),
    PIC("PIC", "贴图消息"),
    SUCCESS("SUCCESS", "成功通知"),
    FAILED("FAILED", "失败通知"),
    WARNING("WARNING", "警告通知");

    private String type;
    private String typeName;

    MessageType(String type, String typeName){
        this.type = type;
        this.typeName = typeName;
    }
Ó
    public String getType() {
        return type;
    }
˙
    public String getTypeName() {
        return typeName;
    }
}

