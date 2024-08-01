package site.notcoder.oji.judge.service;

import site.notcoder.oji.judge.model.JudgeInfo;
import site.notcoder.oji.judge.model.JudgeRequest;
import site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorResponse;

import java.util.concurrent.Future;

public interface JudgeService {
    Future<JudgeInfo> judgeQuestion(JudgeRequest judgeRequest);
}
