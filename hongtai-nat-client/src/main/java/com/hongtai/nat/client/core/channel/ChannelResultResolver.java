package com.hongtai.nat.client.core.channel;

import io.netty.channel.ChannelFuture;

public interface ChannelResultResolver {

    void success(ChannelFuture channelFuture);

    void error(ChannelFuture channelFuture);

}
