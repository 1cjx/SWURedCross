package com.jx.handler.netty;

import com.jx.domain.bean.LoginUser;
import com.jx.utils.JwtUtil;
import com.jx.utils.RedisCache;
import io.jsonwebtoken.Claims;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Autowired
    RedisCache redisCache;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 这里进行鉴权逻辑，可以从请求头中获取 token 进行校验
        String subProtocol = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
        if (subProtocol != null && subProtocol.contains(",")) {
            String[] protocols = subProtocol.split(",");
            String token = protocols[0].trim();  // 提取第一个子协议作为 token
            String websocket = protocols[1].trim();
            if (!isValidToken(ctx,token)) {
                sendUnauthorizedResponse(ctx);
                return;
            }

            // 设置响应头中的子协议
            HttpHeaders headers = req.headers();
            headers.set(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, websocket);
            ctx.fireChannelRead(req.retain());
        }
        else{
            sendUnauthorizedResponse(ctx);
        }
    }
    private boolean isValidToken(ChannelHandlerContext ctx,String token) {
        //解析获取userid
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("adminLogin:" + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            return false;
        }
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        return true;
    }

    private void sendUnauthorizedResponse(ChannelHandlerContext ctx) {
        HttpResponseStatus status = HttpResponseStatus.UNAUTHORIZED;
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status);
        // addListener(ChannelFutureListener.CLOSE)：
        // 添加一个监听器，在响应发送完成后关闭连接。这确保了未经授权的请求不会保持连接，防止后续请求。
        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}