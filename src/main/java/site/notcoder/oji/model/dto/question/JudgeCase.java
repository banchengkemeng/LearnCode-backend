package site.notcoder.oji.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
