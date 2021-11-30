package cn.jeneral.common.untils.spider;

import lombok.Data;

/**
 * 规则类
 *
 * @author zhy
 */
@Data
public class SpiderRule {
    public final static int GET = 0;
    public final static int POST = 1;
    /**
     * 链接
     */
    private String url;
    /**
     * 缓存
     */
    private String cookie;
    /**
     * 参数集合
     */
    private String[] params;
    /**
     * 参数对应的值
     */
    private String[] values;
    /**
     * GET / POST
     * 请求的类型，默认GET
     */
    private int requestMethod = GET;

    public SpiderRule() {
    }


    public SpiderRule(String url, String cookie, String[] params, String[] values, int requestMethod) {
        super();
        this.url = url;
        this.cookie = cookie;
        this.params = params;
        this.values = values;
        this.requestMethod = requestMethod;
    }

}
