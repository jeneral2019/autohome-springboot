package cn.jeneral.common.utils.spider;

public class SpiderConstants {

    /**
     * 数据包大小
     */
    public final static int MAX_FILE_SIZE = 50;
    public final static int MAX_GOODS_NUM = 1000;

    /**
     * 产品线
     */
    public final static String SPIDER_1688_PRD_LINE = "配件";

    /**
     * 导出商品
     */
    public final static Boolean DOWNLOAD_PICTURE = true;

    /**
     * 跳出抓取时间
     */
    public final static int SPIDER_JSOUP_TIME_OUT = 1000000;

    /**
     * 休眠最少时间
     */
    public final static int SLEEP_MIN = 30*1000;

    public final static int SLEEP_MIN_TAOBAO = 60*1000;

    /**
     * 休眠随机跳动幅度
     */
    public final static int SLEEP_RANDOM = 30*1000;

    public final static int SLEEP_RANDOM_TAOBAO = 60*1000;

    /**
     * 是否导出全部商品
     */
    public final static String IS_ALL = "Y";

    public final static boolean isSleep = true;

    public final static String SPIDER_YES = "Y";

    public final static String SPIDER_NO = "N";

    public final static String PROXY_IP_REDIS_KEY = "proxyIpRedisKey";

    public final static String ALIBABA_COOKIE_KEY = "alibaba";

    public final static String TAOBAO_COOKIE_KEY = "taobao";

}
