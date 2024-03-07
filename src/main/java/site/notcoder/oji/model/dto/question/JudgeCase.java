package site.notcoder.oji.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class JudgeCase implements Serializable {
    /**
     * 输入样例文件地址
     */
    String inputCaseFile;

    /**
     * 输出样例文件地址
     */
    String outputCaseFile;

    private static final long serialVersionUID = 1L;
}
