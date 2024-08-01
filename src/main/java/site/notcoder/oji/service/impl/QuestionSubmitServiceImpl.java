package site.notcoder.oji.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.notcoder.oji.common.ErrorCode;
import site.notcoder.oji.exception.BusinessException;
import site.notcoder.oji.exception.ThrowUtils;
import site.notcoder.oji.judge.model.JudgeInfo;
import site.notcoder.oji.judge.model.JudgeRequest;
import site.notcoder.oji.judge.service.JudgeService;
import site.notcoder.oji.mapper.QuestionSubmitMapper;
import site.notcoder.oji.model.dto.question.JudgeConfig;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitRequest;
import site.notcoder.oji.model.entity.QuestionSubmit;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.enums.QuestionSubmitLangEnum;
import site.notcoder.oji.model.enums.QuestionSubmitStatusEnum;
import site.notcoder.oji.model.vo.QuestionAdminVO;
import site.notcoder.oji.model.vo.QuestionSubmitVO;
import site.notcoder.oji.service.QuestionService;
import site.notcoder.oji.service.QuestionSubmitService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 针对表【question_submit(题目提交)】的数据库操作Service实现
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeService judgeService;

    @Override
    public Long doSubmitQuestion(QuestionSubmitRequest questionSubmitRequest, User loginUser) {

        // 状态数据库默认为0
        // 编程语言不存在则直接报错
        String lang = questionSubmitRequest.getLang();
        ThrowUtils.throwIf(!checkLang(lang), ErrorCode.OPERATION_ERROR, "编程语言不存在");


        Long questionId = questionSubmitRequest.getQuestionId();
        QuestionAdminVO questionAdminVOById = questionService.getQuestionAdminVOById(questionId);
        if (questionAdminVOById == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        JudgeRequest judgeRequest = JudgeRequest.builder()
                .questionId(questionId)
                .lang(lang)
                .code(questionSubmitRequest.getCode())
                .judgeConfig(new JudgeConfig(1000L, 1024L))
                .judgeCase(questionAdminVOById.getJudgeCase())
                .build();


        // 修改判题状态: 待判题
        QuestionSubmit questionSubmit = QuestionSubmit.waiting(
                questionSubmitRequest,
                loginUser.getId()
        );
        boolean save = this.save(questionSubmit);

        // 存入数据库后，生成一个QuestionSubmitId, 传入
        judgeRequest.setQuestionSubmitId(questionSubmit.getId());
        judgeService.judgeQuestion(judgeRequest);

        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return questionSubmit.getId();
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVOById(Long id) {
        QuestionSubmit questionSubmitById = getQuestionSubmitById(id);
        ThrowUtils.throwIf(questionSubmitById == null, ErrorCode.NOT_FOUND_ERROR);
        return toQuestionSubmitVO(questionSubmitById);
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmit> querySubmitPage = getQuerySubmitPage(questionSubmitQueryRequest);
        List<QuestionSubmitVO> questionSubmitVOList = querySubmitPage.getRecords().stream().map(this::toQuestionSubmitVO).collect(Collectors.toList());
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(querySubmitPage.getCurrent(), querySubmitPage.getSize(), querySubmitPage.getTotal());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


    /**
     * 分页查询QuestionSubmit
     */
    private Page<QuestionSubmit> getQuerySubmitPage(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmit> questionSubmitPage = this.page(new Page<>(questionSubmitQueryRequest.getCurrent(), questionSubmitQueryRequest.getPageSize()), getQueryWrapper(questionSubmitQueryRequest));
        ThrowUtils.throwIf(questionSubmitPage == null, ErrorCode.OPERATION_ERROR);
        return questionSubmitPage;
    }

    /**
     * 获取QueryWrapper
     */
    private QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String lang = questionSubmitQueryRequest.getLang();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String message = questionSubmitQueryRequest.getMessage();

        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq(questionId != null, "questionId", questionId);
        questionSubmitQueryWrapper.eq(checkLang(lang), "lang", lang);
        questionSubmitQueryWrapper.eq(userId != null, "userId", userId);
        questionSubmitQueryWrapper.eq(status != null, "status", status);
        questionSubmitQueryWrapper.like(StringUtils.isNotBlank(message), "judgeInfo", message);

        return questionSubmitQueryWrapper;
    }

    /**
     * 转换为QuestionSubmitVO
     */
    private QuestionSubmitVO toQuestionSubmitVO(QuestionSubmit questionSubmit) {
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);

        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(JSONUtil.parse(questionSubmit.getJudgeInfo()), JudgeInfo.class, false));

        return questionSubmitVO;
    }

    /**
     * 通过ID查询QuestionSubmit
     */
    private QuestionSubmit getQuestionSubmitById(Long id) {
        return this.getById(id);
    }

    /**
     * 检查编程语言是否存在
     */
    private Boolean checkLang(String lang) {
        return QuestionSubmitLangEnum.getEnumByValue(lang) != null;
    }
}




