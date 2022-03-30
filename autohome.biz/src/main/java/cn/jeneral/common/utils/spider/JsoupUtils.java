package cn.jeneral.common.utils.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupUtils {

    private static void validateRule(SpiderRule spiderRule) {
        String url = spiderRule.getUrl();
        if (url.isEmpty()) {
            throw new SpiderRuleException("url不能为空！");
        }
        if (!url.startsWith("http") && !url.startsWith("Http")) {
            throw new SpiderRuleException("url的格式不正确！");
        }

        if (spiderRule.getParams() != null && spiderRule.getValues() != null) {
            if (spiderRule.getParams().length != spiderRule.getValues().length) {
                throw new SpiderRuleException("参数的键值对个数不匹配！");
            }
        }
    }

    public static Document extract(SpiderRule spiderRule) {
        // 进行对rule的必要校验
        validateRule(spiderRule);
        Document doc = null;
        try {
            String url = spiderRule.getUrl();
            String[] params = spiderRule.getParams();
            String[] values = spiderRule.getValues();
            String cookie = spiderRule.getCookie();
            int requestType = spiderRule.getRequestMethod();

            Connection conn = Jsoup.connect(url);
            conn.header("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
            // 设置查询参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    conn.data(params[i], values[i]);
                }
            }
            if (cookie != null) {
                conn.header("cookie", cookie);
            }
            // 设置请求类型
            switch (requestType) {
                case SpiderRule.GET:
                    doc = conn.timeout(SpiderConstants.SPIDER_JSOUP_TIME_OUT).get();
                    break;
                case SpiderRule.POST:
                    doc = conn.timeout(SpiderConstants.SPIDER_JSOUP_TIME_OUT).post();
                    break;
            }
        } catch (IOException e) {

        }
        return doc;
    }

}
