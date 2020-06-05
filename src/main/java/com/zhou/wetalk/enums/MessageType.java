package com.zhou.wetalk.enums;

/**
 * @author zhouzzz
 */

public enum MessageType {
    TEXT(1,"文本"),
    IMAGE(2,"图片"),
    AUDIO(3,"语音"),
    VEDIO(4,"视频");

    MessageType(Integer type, String desc)
    {
        this.type = type;
        this.desc = desc;
    }

    public String getTypeName(Integer type)
    {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getType() == type)
            {
                return messageType.desc;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer type;
    public String desc;
}
