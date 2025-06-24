package org.ruoyi.common.chat.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FastGPTChatCompletion extends ChatCompletion implements Serializable {

    /**
     * 是否使用FastGPT提供的上下文
     */
    private String chatId;


    /**
     * 是否返回详细信息;stream模式下会通过event进行区分，非stream模式结果保存在responseData中.
     */
    private boolean detail;


}
