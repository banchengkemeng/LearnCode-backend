package site.notcoder.oji.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户提交编程语言枚举
 */
@Getter
public enum QuestionSubmitLangEnum {

    JAVA("java", "java"),
    JAVASCRIPT("javascript", "javascript"),
    CPP("cpp", "cpp"),
    GOLANG("go", "go");

    private final String text;

    private final String value;

    QuestionSubmitLangEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitLangEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitLangEnum anEnum : QuestionSubmitLangEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
