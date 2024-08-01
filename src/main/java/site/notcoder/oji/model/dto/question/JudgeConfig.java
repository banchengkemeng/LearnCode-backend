package site.notcoder.oji.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeConfig implements Serializable {
    /**
     * 时间限制
     */
    Long timeLimit;

    /**
     * 空间限制
     */
    Long memoryLimit;

    private static final long serialVersionUID = 1L;
}
