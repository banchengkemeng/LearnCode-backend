package site.notcoder.oji.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.notcoder.oji.common.ErrorCode;
import site.notcoder.oji.exception.ThrowUtils;
import site.notcoder.oji.mapper.QuestionMapper;
import site.notcoder.oji.model.dto.question.*;
import site.notcoder.oji.model.entity.Question;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.vo.QuestionAdminVO;
import site.notcoder.oji.model.vo.QuestionVO;
import site.notcoder.oji.service.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* 针对表【question(题目)】的数据库操作Service实现
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Override
    public Long addQuestion(QuestionAddRequest questionAddRequest, User loginUser) {
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);

        // 设置tag
        question.setTags(JSONUtil.toJsonStr(
                questionAddRequest.getTags()
        ));

        // 设置judgeConfig
        question.setJudgeConfig(JSONUtil.toJsonStr(
                questionAddRequest.getJudgeConfig()
        ));

        // 设置judgeCase
        question.setJudgeCase(JSONUtil.toJsonStr(
                questionAddRequest.getJudgeCase()
        ));

        // 设置UserId
        question.setUserId(loginUser.getId());

        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return question.getId();
    }

    @Override
    public Long updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        // 只有管理员可以编辑题目，所以可以编辑所有题目
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);

        question.setTags(JSONUtil.toJsonStr(
                questionUpdateRequest.getTags()
        ));
        question.setJudgeConfig(JSONUtil.toJsonStr(
                questionUpdateRequest.getJudgeConfig()
        ));
        question.setJudgeCase(JSONUtil.toJsonStr(
                questionUpdateRequest.getJudgeCase()
        ));

        boolean result = this.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return question.getId();
    }

    @Override
    @Transactional
    public Boolean deleteQuestionByIds(List<Long> ids) {
        boolean result = this.removeByIds(ids);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QuestionVO getQuestionVOById(Long id) {
        Question questionById = getQuestionById(id);
        ThrowUtils.throwIf(questionById==null, ErrorCode.NOT_FOUND_ERROR);

        return toQuestionVO(questionById);
    }

    @Override
    public QuestionAdminVO getQuestionAdminVOById(Long id) {
        Question questionById = getQuestionById(id);
        ThrowUtils.throwIf(questionById==null, ErrorCode.NOT_FOUND_ERROR);

        return toQuestionAdminVO(questionById);
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(QuestionQueryRequest questionQueryRequest) {
        Page<Question> page = getQuestionPage(questionQueryRequest);
        List<QuestionVO> questionVOList = page.getRecords().stream().map(this::toQuestionVO).collect(Collectors.toList());
        Page<QuestionVO> questionVOPage = new Page<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal()
        );
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public Page<QuestionAdminVO> getQuestionAdminVOPage(QuestionQueryRequest questionQueryRequest) {
        Page<Question> page = getQuestionPage(questionQueryRequest);
        List<QuestionAdminVO> questionAdminVOList = page.getRecords().stream().map(this::toQuestionAdminVO).collect(Collectors.toList());
        Page<QuestionAdminVO> questionVOPage = new Page<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal()
        );
        questionVOPage.setRecords(questionAdminVOList);
        return questionVOPage;
    }

    /**
     * 分页查询Question
     */
    private Page<Question> getQuestionPage(QuestionQueryRequest questionQueryRequest) {
        Page<Question> questionPage = this.page(
                new Page<>(
                        questionQueryRequest.getCurrent(),
                        questionQueryRequest.getPageSize()
                ),
                getQueryWrapper(questionQueryRequest)
        );
        ThrowUtils.throwIf(questionPage==null, ErrorCode.OPERATION_ERROR);
        return questionPage;
    }

    /**
     * 获取QueryWrapper
     */
    private QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        Long userId = questionQueryRequest.getUserId();
        String title = questionQueryRequest.getTitle();
        List<String> tags = questionQueryRequest.getTags();

        questionQueryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        questionQueryWrapper.eq(userId!=null, "userId", userId);
        if (tags != null) {
            tags.forEach(tag -> {
                questionQueryWrapper.like(StringUtils.isNotBlank(tag), "tags", String.format("\"%s\"", tag));
            });
        }

        return questionQueryWrapper;
    }


    /**
     * 根据ID查询Question
     */
    private Question getQuestionById(Long id) {
        return this.getById(id);
    }

    /**
     * 转换为QuestionVO
     */
    private QuestionVO toQuestionVO(Question question) {
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);

        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionVO.setJudgeConfig(
                JSONUtil.toBean(JSONUtil.parse(question.getJudgeConfig()),
                        JudgeConfig.class,
                        false)
        );

        return questionVO;
    }

    /**
     * 转换为QuestionAdminVO
     */
    private QuestionAdminVO toQuestionAdminVO(Question question) {
        QuestionAdminVO questionAdminVO = new QuestionAdminVO();
        BeanUtils.copyProperties(question, questionAdminVO);

        questionAdminVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionAdminVO.setJudgeConfig(
                JSONUtil.toBean(JSONUtil.parse(question.getJudgeConfig()),
                        JudgeConfig.class,
                        false)
        );

        ArrayList<JudgeCase> judgeCases = new ArrayList<>();
        List<JSONObject> tempJudgeCases = JSONUtil.toBean(
                JSONUtil.parse(question.getJudgeCase()),
                List.class,
                false
        );
        tempJudgeCases.forEach(judgeCase -> {
            judgeCases.add(
                    JSONUtil.toBean(judgeCase, JudgeCase.class, false)
            );
        });
        questionAdminVO.setJudgeCase(judgeCases);
        return questionAdminVO;
    }
}




