package site.notcoder.oji.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.notcoder.oji.common.PageRequest;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
