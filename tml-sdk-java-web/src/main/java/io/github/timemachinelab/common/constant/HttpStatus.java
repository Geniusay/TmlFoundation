package io.github.timemachinelab.common.constant;

/**
 * HTTP状态码常量池
 * 定义常用的HTTP状态码
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
public final class HttpStatus {
    
    private HttpStatus() {
        // 工具类，禁止实例化
    }
    
    // ========== 1xx 信息响应 ==========
    /** 继续 */
    public static final int CONTINUE = 100;
    
    /** 切换协议 */
    public static final int SWITCHING_PROTOCOLS = 101;
    
    // ========== 2xx 成功响应 ==========
    /** 请求成功 */
    public static final int OK = 200;
    
    /** 已创建 */
    public static final int CREATED = 201;
    
    /** 已接受 */
    public static final int ACCEPTED = 202;
    
    /** 非权威信息 */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    
    /** 无内容 */
    public static final int NO_CONTENT = 204;
    
    /** 重置内容 */
    public static final int RESET_CONTENT = 205;
    
    /** 部分内容 */
    public static final int PARTIAL_CONTENT = 206;
    
    // ========== 3xx 重定向 ==========
    /** 多种选择 */
    public static final int MULTIPLE_CHOICES = 300;
    
    /** 永久移动 */
    public static final int MOVED_PERMANENTLY = 301;
    
    /** 临时移动 */
    public static final int FOUND = 302;
    
    /** 查看其他位置 */
    public static final int SEE_OTHER = 303;
    
    /** 未修改 */
    public static final int NOT_MODIFIED = 304;
    
    /** 临时重定向 */
    public static final int TEMPORARY_REDIRECT = 307;
    
    /** 永久重定向 */
    public static final int PERMANENT_REDIRECT = 308;
    
    // ========== 4xx 客户端错误 ==========
    /** 错误请求 */
    public static final int BAD_REQUEST = 400;
    
    /** 未授权 */
    public static final int UNAUTHORIZED = 401;
    
    /** 需要付费 */
    public static final int PAYMENT_REQUIRED = 402;
    
    /** 禁止访问 */
    public static final int FORBIDDEN = 403;
    
    /** 未找到 */
    public static final int NOT_FOUND = 404;
    
    /** 方法不允许 */
    public static final int METHOD_NOT_ALLOWED = 405;
    
    /** 不可接受 */
    public static final int NOT_ACCEPTABLE = 406;
    
    /** 需要代理认证 */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    
    /** 请求超时 */
    public static final int REQUEST_TIMEOUT = 408;
    
    /** 冲突 */
    public static final int CONFLICT = 409;
    
    /** 已删除 */
    public static final int GONE = 410;
    
    /** 需要内容长度 */
    public static final int LENGTH_REQUIRED = 411;
    
    /** 前提条件失败 */
    public static final int PRECONDITION_FAILED = 412;
    
    /** 请求实体过大 */
    public static final int PAYLOAD_TOO_LARGE = 413;
    
    /** 请求URI过长 */
    public static final int URI_TOO_LONG = 414;
    
    /** 不支持的媒体类型 */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    
    /** 请求范围无法满足 */
    public static final int RANGE_NOT_SATISFIABLE = 416;
    
    /** 期望失败 */
    public static final int EXPECTATION_FAILED = 417;
    
    /** 我是茶壶 */
    public static final int I_AM_A_TEAPOT = 418;
    
    /** 请求过多 */
    public static final int TOO_MANY_REQUESTS = 429;
    
    // ========== 5xx 服务器错误 ==========
    /** 服务器内部错误 */
    public static final int INTERNAL_SERVER_ERROR = 500;
    
    /** 未实现 */
    public static final int NOT_IMPLEMENTED = 501;
    
    /** 错误网关 */
    public static final int BAD_GATEWAY = 502;
    
    /** 服务不可用 */
    public static final int SERVICE_UNAVAILABLE = 503;
    
    /** 网关超时 */
    public static final int GATEWAY_TIMEOUT = 504;
    
    /** HTTP版本不支持 */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    
    /** 网络认证需要 */
    public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;
    
    /**
     * 判断是否为成功状态码
     * 
     * @param status HTTP状态码
     * @return 是否成功
     */
    public static boolean isSuccess(int status) {
        return status >= 200 && status < 300;
    }
    
    /**
     * 判断是否为重定向状态码
     * 
     * @param status HTTP状态码
     * @return 是否重定向
     */
    public static boolean isRedirection(int status) {
        return status >= 300 && status < 400;
    }
    
    /**
     * 判断是否为客户端错误状态码
     * 
     * @param status HTTP状态码
     * @return 是否客户端错误
     */
    public static boolean isClientError(int status) {
        return status >= 400 && status < 500;
    }
    
    /**
     * 判断是否为服务器错误状态码
     * 
     * @param status HTTP状态码
     * @return 是否服务器错误
     */
    public static boolean isServerError(int status) {
        return status >= 500 && status < 600;
    }
    
    /**
     * 判断是否为错误状态码
     * 
     * @param status HTTP状态码
     * @return 是否错误
     */
    public static boolean isError(int status) {
        return status >= 400;
    }
}