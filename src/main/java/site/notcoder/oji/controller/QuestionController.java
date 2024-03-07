package site.notcoder.oji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.notcoder.oji.annotation.AuthCheck;
import site.notcoder.oji.common.BaseResponse;
import site.notcoder.oji.common.ResultUtils;
import site.notcoder.oji.constant.UserConstant;
import site.notcoder.oji.model.dto.question.QuestionAddRequest;
import site.notcoder.oji.model.dto.question.QuestionQueryRequest;
import site.notcoder.oji.model.dto.question.QuestionUpdateRequest;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.enums.QuestionSubmitLangEnum;
import site.notcoder.oji.model.vo.QuestionAdminVO;
import site.notcoder.oji.model.vo.QuestionVO;
import site.notcoder.oji.service.UserService;
import site.notcoder.oji.service.impl.QuestionServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionServiceImpl questionService;

    @Resource
    private UserService userService;

    @PostMapping("/admin/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long questionId = questionService.addQuestion(questionAddRequest, loginUser);
        return ResultUtils.success(questionId);
    }

    @PostMapping("/admin/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        Long questionId = questionService.updateQuestion(questionUpdateRequest);
        return ResultUtils.success(questionId);
    }

    @PostMapping("/admin/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestionByIds(@RequestBody List<Long> ids) {
        Boolean result = questionService.deleteQuestionByIds(ids);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<QuestionVO> getQuestionVOById(Long id) {
        QuestionVO questionVOById = questionService.getQuestionVOById(id);
        return ResultUtils.success(questionVOById);
    }

    @GetMapping("/admin/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<QuestionAdminVO> getQuestionAdminVOById(Long id) {
        QuestionAdminVO questionAdminVOById = questionService.getQuestionAdminVOById(id);
        return ResultUtils.success(questionAdminVOById);
    }

    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Page<QuestionVO>> getQuestionVOPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        Page<QuestionVO> questionVOPage = questionService.getQuestionVOPage(questionQueryRequest);
        return ResultUtils.success(questionVOPage);
    }

    @PostMapping("/admin/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionAdminVO>> getQuestionAdminVOPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        Page<QuestionAdminVO> questionAdminVOPage = questionService.getQuestionAdminVOPage(questionQueryRequest);
        return ResultUtils.success(questionAdminVOPage);
    }

    @GetMapping("/lang/list")
    public BaseResponse<List<String>> getLangList() {
        return ResultUtils.success(QuestionSubmitLangEnum.getValues());
    }
}
