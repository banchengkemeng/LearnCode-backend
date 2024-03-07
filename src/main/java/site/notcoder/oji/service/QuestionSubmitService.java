package site.notcoder.oji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import site.notcoder.oji.model.dto.questionsubmit.QuestionSubmitRequest;
import site.notcoder.oji.model.entity.QuestionSubmit;
import site.notcoder.oji.model.entity.User;
import site.notcoder.oji.model.vo.QuestionSubmitVO;

/**
* 针对表【question_submit(题目提交)】的数据库操作Service
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    Long doSubmitQuestion(QuestionSubmitRequest questionSubmitRequest, User loginUser);
    QuestionSubmitVO getQuestionSubmitVOById(Long id);
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(QuestionSubmitQueryRequest questionSubmitQueryRequest);
}
