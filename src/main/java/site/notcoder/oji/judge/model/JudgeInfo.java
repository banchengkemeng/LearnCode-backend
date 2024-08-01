package site.notcoder.oji.judge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeInfo {

    String questionId;
    Boolean finished;
    String status;
    String message;
    String detail;

    public static JudgeInfo waitting(String questionId) {
        return new JudgeInfo(
                questionId,
                false,
                JudgeInfoMessage.WAITING.getValue(),
                JudgeInfoMessage.WAITING.getText(),
                null
        );
    }

    public static JudgeInfo finished(String questionId, JudgeInfoMessage judgeInfoMessage, String detail) {
        return new JudgeInfo(
                questionId,
                true,
                judgeInfoMessage.getValue(),
                judgeInfoMessage.getText(),
                detail
        );
    }
}
