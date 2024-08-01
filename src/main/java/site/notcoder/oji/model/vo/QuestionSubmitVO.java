package site.notcoder.oji.model.vo;

import lombok.Data;
import site.notcoder.oji.judge.model.JudgeInfo;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * id
     */
    private Long id;

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
     * 提交代码
     */
    private String code;

    /**
     * 状态(0-待判题，1-判题中，2-成功，3-失败)
     */
    private Integer status;

    /**
     * 判题信息(json)
     */
    private JudgeInfo judgeInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
