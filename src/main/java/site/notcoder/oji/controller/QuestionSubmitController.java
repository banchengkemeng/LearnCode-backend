package site.notcoder.oji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.notcoder.oji.annotation.AuthCheck;
import site.notcoder.oji.common.BaseResponse;
import site.notcoder.oji.common.ResultUtils;
import site.notcoder.oji.constant.UserConstant;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitRequest;
import site.notcoder.oji.model.entity.QuestionSubmit;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.vo.QuestionSubmitVO;
import site.notcoder.oji.service.QuestionSubmitService;
import site.notcoder.oji.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Long> doSubmit(
            @RequestBody QuestionSubmitRequest questionSubmitRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long submissionID = questionSubmitService.doSubmitQuestion(questionSubmitRequest, loginUser);
        return ResultUtils.success(submissionID);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<QuestionSubmitVO> getQuestionSubmitVO(Long id) {
        QuestionSubmitVO questionSubmitVOById = questionSubmitService.getQuestionSubmitVOById(id);
        return ResultUtils.success(questionSubmitVOById);
    }

    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Page<QuestionSubmitVO>> getQuestionSubmitVOPage(
            @RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmitVO> questionSubmitVOPage = questionSubmitService.getQuestionSubmitVOPage(questionSubmitQueryRequest);
        return ResultUtils.success(questionSubmitVOPage);
    }
}
