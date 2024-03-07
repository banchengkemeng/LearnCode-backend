package site.notcoder.oji.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.notcoder.oji.common.ErrorCode;
import site.notcoder.oji.exception.ThrowUtils;
import site.notcoder.oji.mapper.QuestionSubmitMapper;
import site.notcoder.oji.model.dto.questionsubmit.JudgeInfo;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitRequest;
import site.notcoder.oji.model.entity.QuestionSubmit;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.enums.JudgeInfoMessage;
import site.notcoder.oji.model.enums.QuestionSubmitLangEnum;
import site.notcoder.oji.model.vo.QuestionSubmitVO;
import site.notcoder.oji.service.QuestionSubmitService;

import java.util.List;
import java.util.stream.Collectors;

/**
* 针对表【question_submit(题目提交)】的数据库操作Service实现
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Override
    public Long doSubmitQuestion(QuestionSubmitRequest questionSubmitRequest, User loginUser) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(loginUser.getId());
        // TODO 下发提交任务

        // 状态数据库默认为0
        // 编程语言不存在则直接报错
        String lang = questionSubmitRequest.getLang();
        ThrowUtils.throwIf(!checkLang(lang), ErrorCode.OPERATION_ERROR, "编程语言不存在");

        BeanUtils.copyProperties(questionSubmitRequest, questionSubmit);
        // 设置JudgeInfo
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessage.WAITING.getValue());
        judgeInfo.setTime("0");
        judgeInfo.setMemory("0");
        String judgeInfoStr = JSONUtil.toJsonStr(judgeInfo);
        questionSubmit.setJudgeInfo(judgeInfoStr);

        // 修改数据库
        boolean save = this.save(questionSubmit);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return questionSubmit.getId();
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVOById(Long id) {
        QuestionSubmit questionSubmitById = getQuestionSubmitById(id);
        ThrowUtils.throwIf(questionSubmitById==null, ErrorCode.NOT_FOUND_ERROR);
        return toQuestionSubmitVO(questionSubmitById);
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmit> querySubmitPage = getQuerySubmitPage(questionSubmitQueryRequest);
        List<QuestionSubmitVO> questionSubmitVOList = querySubmitPage.getRecords().stream().map(this::toQuestionSubmitVO).collect(Collectors.toList());
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(
                querySubmitPage.getCurrent(),
                querySubmitPage.getSize(),
                querySubmitPage.getTotal()
        );
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


    /**
     * 分页查询QuestionSubmit
     */
    private Page<QuestionSubmit> getQuerySubmitPage(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmit> questionSubmitPage = this.page(
                new Page<>(
                        questionSubmitQueryRequest.getCurrent(),
                        questionSubmitQueryRequest.getPageSize()
                ),
                getQueryWrapper(questionSubmitQueryRequest)
        );
        ThrowUtils.throwIf(questionSubmitPage==null, ErrorCode.OPERATION_ERROR);
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
        questionSubmitQueryWrapper.eq(questionId!=null, "questionId", questionId);
        questionSubmitQueryWrapper.eq(checkLang(lang), "lang", lang);
        questionSubmitQueryWrapper.eq(userId!=null, "userId", userId);
        questionSubmitQueryWrapper.eq(status!=null, "status", status);
        questionSubmitQueryWrapper.like(StringUtils.isNotBlank(message), "judgeInfo", message);

        return questionSubmitQueryWrapper;
    }

    /**
     * 转换为QuestionSubmitVO
     */
    private QuestionSubmitVO toQuestionSubmitVO(QuestionSubmit questionSubmit) {
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);

        questionSubmitVO.setJudgeInfo(
                JSONUtil.toBean(JSONUtil.parse(questionSubmit.getJudgeInfo()),
                        JudgeInfo.class,
                        false
                )
        );

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




