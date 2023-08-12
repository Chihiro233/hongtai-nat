package com.hongtai.nat.common.core.constant;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public interface AttrConstant {

    AttributeKey<Channel> ref_proxy_channel = AttributeKey.valueOf("ref_proxy_channel");


    AttributeKey<Channel> ref_access_channel = AttributeKey.valueOf("ref_access_channel");

    AttributeKey<Channel> ref_agent_channel = AttributeKey.valueOf("ref_agent_channel");


    AttributeKey<Channel> ref_cmd_channel = AttributeKey.valueOf("ref_cmd_channel");

}
