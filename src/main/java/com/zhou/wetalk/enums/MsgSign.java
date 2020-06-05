package com.zhou.wetalk.enums;

/**
 * @author zhouzzz
 */

public enum MsgSign {
    UNSIGN(0,"未签收"),
    SIGNED(1,"已签收");

    public Integer getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public final Integer type;
    public final String content;
    MsgSign(Integer type, String content)
    {
        this.type = type;
        this.content = content;
    }

}
