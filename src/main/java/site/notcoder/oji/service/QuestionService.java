package site.notcoder.oji.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import site.notcoder.oji.model.dto.question.QuestionAddRequest;
import site.notcoder.oji.model.dto.question.QuestionQueryRequest;
import site.notcoder.oji.model.dto.question.QuestionUpdateRequest;
import site.notcoder.oji.model.entity.Question;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.vo.QuestionAdminVO;
import site.notcoder.oji.model.vo.QuestionVO;

import java.util.List;

/**
* 针对表【question(题目)】的数据库操作Service
*/
public interface QuestionService extends IService<Question> {
    Long addQuestion(QuestionAddRequest questionAddRequest, User loginUser);
    Long updateQuestion(QuestionUpdateRequest questionUpdateRequest);
    Boolean deleteQuestionByIds(List<Long> ids);
    QuestionVO getQuestionVOById(Long id);
    QuestionAdminVO getQuestionAdminVOById(Long id);
    Page<QuestionVO> getQuestionVOPage(QuestionQueryRequest questionQueryRequest);
    Page<QuestionAdminVO> getQuestionAdminVOPage(QuestionQueryRequest questionQueryRequest);
}
