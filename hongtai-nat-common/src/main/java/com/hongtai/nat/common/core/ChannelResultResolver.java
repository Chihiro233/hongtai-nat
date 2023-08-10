package com.hongtai.nat.common.core;

import io.netty.channel.Channel;

public interface ChannelResultResolver {

    void success(Channel channel);

    void error(Channel channel);

}
