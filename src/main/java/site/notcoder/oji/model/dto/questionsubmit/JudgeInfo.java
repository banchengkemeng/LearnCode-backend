package site.notcoder.oji.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class JudgeInfo implements Serializable {

    /**
     * 判题消息
     */
    String message;

    /**
     * 时间消耗
     */
    String time;

    /**
     * 内存占用
     */
    String memory;

    private static final long serialVersionUID = 1L;
}
