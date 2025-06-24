package org.ruoyi.chat.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastGPTSSEEventSourceListener extends EventSourceListener {

    private SseEmitter emitter;

    private Long userId;

    private Long sessionId;

    private StringBuilder stringBuffer = new StringBuilder();

    @Autowired(required = false)
    public FastGPTSSEEventSourceListener(SseEmitter emitter,Long userId,Long sessionId) {
        this.emitter = emitter;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("FastGPT  sse连接成功");
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
        try {
            if ("[DONE]".equals(data)){
                emitter.complete();
                return;
            }
            log.info("事件类型为: {}", type);
            log.info("事件ID为: {}", id);
            log.info("事件数据为: {}", data);
            emitter.send(data);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("FastGPT  sse连接关闭");
    }

    @Override
    @SneakyThrows
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("FastGPT  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("FastGPT sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}
