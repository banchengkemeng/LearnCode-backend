package site.notcoder.oji.model.dto.questionsubmit;

import lombok.Data;
import site.notcoder.oji.common.PageRequest;

import java.io.Serializable;

@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 编程语言
     */
    private String lang;

    /**
     * 状态(0-待判题，1-判题中，2-成功，3-失败)
     */
    private Integer status;

    /**
     * 判题信息消息
     */
    private String message;

    private static final long serialVersionUID = 1L;
}
