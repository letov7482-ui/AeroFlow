package com.unredpass.aeroflow.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "channelActive", at = @At("HEAD"))
    private void aeroflow$enableTcpNoDelay(ChannelHandlerContext context, CallbackInfo ci) {
        context.channel().config().setOption(ChannelOption.TCP_NODELAY, true);
    }
}
