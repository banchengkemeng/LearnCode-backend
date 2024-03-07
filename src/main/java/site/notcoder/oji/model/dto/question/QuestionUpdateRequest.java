package site.notcoder.oji.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionUpdateRequest implements Serializable {
    /**
     * id
     */
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

    private static final long serialVersionUID = 1L;
}
