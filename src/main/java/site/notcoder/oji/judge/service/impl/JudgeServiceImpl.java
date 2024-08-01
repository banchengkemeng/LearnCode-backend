package site.notcoder.oji.judge.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import site.notcoder.oji.judge.model.JudgeInfo;
import site.notcoder.oji.judge.model.JudgeInfoMessage;
import site.notcoder.oji.judge.model.JudgeRequest;
import site.notcoder.oji.judge.service.JudgeService;
import site.notcoder.oji.model.dto.question.JudgeCase;
import site.notcoder.oji.model.entity.QuestionSubmit;
import site.notcoder.oji.ojicodesandbox.model.exec.SandboxExecResponse;
import site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorRequest;
import site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorResponse;
import site.notcoder.oji.ojicodesandbox.model.sandbox.InputArg;
import site.notcoder.oji.ojicodesandbox.starter.CodeSandboxExecutor;
import site.notcoder.oji.service.QuestionSubmitService;
import site.notcoder.oji.utils.JudgeUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@EnableAsync
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private CodeSandboxExecutor codeSandboxExecutor;

    @Resource
    @Lazy
    private QuestionSubmitService questionSubmitService;

    @Async("judgeTaskExecutor")
    @Override
    public Future<JudgeInfo> judgeQuestion(JudgeRequest judgeRequest) {
        Long questionSubmitId = judgeRequest.getQuestionSubmitId();
        String questionId = judgeRequest.getQuestionId() == null ? "" : judgeRequest.getQuestionId().toString();

        try {
            // 开始判题
            questionSubmitService.saveOrUpdate(
                    QuestionSubmit.judging(
                            questionSubmitId,
                            JudgeInfo.waitting(questionId)
                    )
            );

            List<JudgeCase> judgeCases = judgeRequest.getJudgeCase();
            ArrayList<InputArg> inputArgs = new ArrayList<>();

            // 构造程序输入参数 和(输出文件HashMap: 为后续判题做准备)
            HashMap<String, String> outputFileMap = new HashMap<>();
            for (JudgeCase judgeCase : judgeCases) {
                String inputCaseFile = judgeCase.getInputCaseFile();
                inputArgs.add(
                        new InputArg(
                                inputCaseFile,
                                JudgeUtils.readJudgeFile(questionId, inputCaseFile)
                        )
                );

                outputFileMap.put(inputCaseFile, judgeCase.getOutputCaseFile());
            }

            ExecutorRequest executorRequest = ExecutorRequest.builder()
                    .lang(judgeRequest.getLang())
                    .code(judgeRequest.getCode())
                    .inputs(inputArgs)
                    .build();

            // 执行程序
            ExecutorResponse executorResponse = codeSandboxExecutor.exec(executorRequest);

            Boolean success = executorResponse.getSuccess();

            // 编译执行失败
            if (!success) {
                JudgeInfo judgeInfo = JudgeInfo.finished(
                        questionId,
                        JudgeInfoMessage.EXEC_ERROR, executorResponse.getMessage()
                );

                this.updateJudgeInfoToDatabase(
                        QuestionSubmit.failed(questionSubmitId, judgeInfo)
                );

                return AsyncResult.forValue(judgeInfo);
            }

            // 判题
            List<SandboxExecResponse> responses = executorResponse.getResponses();
            String message = executorResponse.getMessage();

            for (SandboxExecResponse sandboxExecResponse : responses) {
                String id = sandboxExecResponse.getInput().getId();
                String output = sandboxExecResponse.getOutput().trim();
                // TODO 开放一个服务可以看输出
                String answer = JudgeUtils.readJudgeFile(questionId, outputFileMap.get(id).trim());

                // TODO 内存超限

                // TODO 时间超限

                // TODO 输出超限

                log.info("output: {}", output);
                log.info("answer: {}", answer);

                // 答案错误
                if (!output.equals(answer)) {
                    JudgeInfo judgeInfo = JudgeInfo.finished(questionId, JudgeInfoMessage.WRONG_ANSWER, message);

                    this.updateJudgeInfoToDatabase(
                            QuestionSubmit.success(questionSubmitId, judgeInfo)
                    );

                    return AsyncResult.forValue(judgeInfo);
                }
            }

            JudgeInfo judgeInfo = JudgeInfo.finished(questionId, JudgeInfoMessage.ACCEPTED, message);

            this.updateJudgeInfoToDatabase(
                    QuestionSubmit.success(questionSubmitId, judgeInfo)
            );

            return AsyncResult.forValue(judgeInfo);
        } catch (Exception e) {
            log.error(e.toString());

            JudgeInfo judgeInfo = JudgeInfo.finished(questionId, JudgeInfoMessage.SYSTEM_ERROR, e.toString());


            this.updateJudgeInfoToDatabase(
                    QuestionSubmit.failed(questionSubmitId, judgeInfo)
            );

            return AsyncResult.forValue(judgeInfo);
        }
    }

    private void updateJudgeInfoToDatabase(QuestionSubmit questionSubmit) {
        questionSubmitService.saveOrUpdate(questionSubmit);
    }
}
