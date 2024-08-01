package site.notcoder.oji.judge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.notcoder.oji.model.dto.question.JudgeCase;
import site.notcoder.oji.model.dto.question.JudgeConfig;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JudgeRequest implements Serializable {
    private Long questionId;

    private Long questionSubmitId;

    private String lang;

    private String code;

    private JudgeConfig judgeConfig;

    private List<JudgeCase> judgeCase;

    private static final long serialVersionUID = 1L;
}
