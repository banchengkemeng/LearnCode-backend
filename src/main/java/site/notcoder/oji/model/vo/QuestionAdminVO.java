package site.notcoder.oji.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import site.notcoder.oji.model.dto.question.JudgeCase;
import site.notcoder.oji.model.dto.question.JudgeConfig;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionAdminVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 答案
     */
    private String answer;

    /**
     * 输入描述
     */
    private String inputDescription;

    /**
     * 输出描述
     */
    private String outputDescription;

    /**
     * 输入样例
     */
    private String inputSample;

    /**
     * 输出样例
     */
    private String outputSample;

    /**
     * 提示
     */
    private String hint;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题样例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
