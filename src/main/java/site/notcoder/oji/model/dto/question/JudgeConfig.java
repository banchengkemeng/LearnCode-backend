package site.notcoder.oji.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class JudgeConfig implements Serializable {
    /**
     * 时间限制
     */
    String timeLimit;

    /**
     * 空间限制
     */
    String memoryLimit;

    private static final long serialVersionUID = 1L;
}
