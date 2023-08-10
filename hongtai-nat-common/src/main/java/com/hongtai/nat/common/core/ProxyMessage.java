package com.hongtai.nat.common.core;

import com.alibaba.fastjson2.JSON;
import com.hongtai.nat.common.core.model.ClientMetaInfo;
import com.hongtai.nat.common.core.util.IdGenerateUtil;
import lombok.Data;

@Data
public class ProxyMessage {

    /**
     * @see com.hongtai.nat.common.core.CommandConstant
     */
    private byte type;

    private Long msgId;

    private String message;


    public static class Builder {

        public static ProxyMessage buildCmdMessage(byte type, ClientMetaInfo metaInfo){
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setMsgId(IdGenerateUtil.generate());
            proxyMessage.setType(CommandConstant.CONNECT);
            proxyMessage.setMessage(JSON.toJSONString(metaInfo));
            return proxyMessage;
        }

    }

}
