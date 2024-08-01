package site.notcoder.oji;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import site.notcoder.oji.judge.model.JudgeInfo;
import site.notcoder.oji.judge.model.JudgeRequest;
import site.notcoder.oji.judge.service.JudgeService;
import site.notcoder.oji.model.dto.question.JudgeCase;
import site.notcoder.oji.model.dto.question.JudgeConfig;
import site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorRequest;
import site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorResponse;
import site.notcoder.oji.ojicodesandbox.model.sandbox.InputArg;
import site.notcoder.oji.ojicodesandbox.starter.CodeSandboxExecutor;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests {

    @Value("${codesandbox.type}")
    private String type;

    @Resource
    private CodeSandboxExecutor codeSandboxExecutor;

    @Resource
    private JudgeService judgeService;

    @Test
    void contextLoads() {
        System.out.println();
    }

    @Test
    void testCodeSandbox() throws Exception {
        String code = "import java.util.Scanner;\n" +
                "\n" +
                "public class Main{\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner scanner = new Scanner(System.in);\n" +
                "        int i = scanner.nextInt();\n" +
                "        int i1 = scanner.nextInt();\n" +
                "        System.out.println(i*i1);\n" +
                "    }\n" +
                "}";
        ExecutorRequest executorRequest = new site.notcoder.oji.ojicodesandbox.model.sandbox.ExecutorRequest(
                code,
                "java",
                Arrays.asList(
                        new InputArg(
                                "1.txt",
                                "6 6"
                        ),
                        new InputArg(
                                "2.txt",
                                "3 5"
                        )
                )
        );
        ExecutorResponse exec = codeSandboxExecutor.exec(executorRequest);
        System.out.println(exec);
    }

    @Test
    void testJudgeService() throws Exception {
        String code = "import java.util.Scanner;\n" +
                "\n" +
                "public class Main{\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner scanner = new Scanner(System.in);\n" +
                "        int i = scanner.nextInt();\n" +
                "        int i1 = scanner.nextInt();\n" +
                "        System.out.println(i*i1);\n" +
                "    }\n" +
                "}";

        JudgeRequest judgeRequest = JudgeRequest.builder()
                .questionId(1L)
                .questionSubmitId(1818660044601491457L)
                .lang("java")
                .code(code)
                .judgeConfig(new JudgeConfig(1000L, 1024L))
                .judgeCase(Arrays.asList(
                        new JudgeCase("1.in", "1.out"),
                        new JudgeCase("2.in", "2.out"),
                        new JudgeCase("3.in", "3.out")
                ))
                .build();

        JudgeInfo judgeInfo = judgeService.judgeQuestion(judgeRequest).get();

        System.out.println(judgeInfo);
    }
}
