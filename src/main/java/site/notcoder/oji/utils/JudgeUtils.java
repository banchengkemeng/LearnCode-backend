package site.notcoder.oji.utils;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 判题工具类
 */
public class JudgeUtils {
    public static String readJudgeFile(String questionId, String fileName) throws RuntimeException {
        return FileUtil.readString(
                new File(String.format("case/%s/%s", questionId, fileName)),
                Charset.defaultCharset()
        );
    }
}
