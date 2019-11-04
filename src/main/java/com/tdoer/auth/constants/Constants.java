package com.tdoer.auth.constants;

/**
 * Contants
 */
public interface Constants {

    /** 全局空字符串 */
    String S_EMPTYSTRING = "";
    /** 验证码缓存 codekey 前缀 */
    String S_CODEKEY_PREFIX = "codekey_";
    /** 操作凭证缓存 crendential 前缀 */
    String S_CREDENTIAL_PREFIX = "credit_";
    /** 验证码 codekey 有效期，单位（秒） */
    int S_CODEKEY_VALIDITY_PERIOD = 60;
    /** 用户信息操作凭证有效期, 单位（秒） */
    int S_CREDENTIAL_VALIDITY_PERIOD = 60 * 3;
    /** 应用ID，百邦生活 */
    String S_CLIENT_BBLIFE = "BBSH";
    /** 默认的短信SvcCode */
    String S_SVC_CODE_DEFAULT = "SDF";
    /** 百邦生活短信SvcCode */
    String S_SVC_CODE_AUTH_BBLIFE = "BBSH";
    /** 默认验证码短信模版 */
    String S_SMS_TEMPLATE_CODE_DEFAULT = "SMS_143706047";
    /** 百邦生活验证码短信模版 */
    String S_SMS_TEMPLATE_CODE_BBLIFE = "SMS_20181101001";

    String APP_TYPE = "app_type";

    String TYPE_INTERNAL_EMPLOYEE = "internal-employee";

    String TYPE_CLIENT_USER = "client-user";
    /** 返回成功message */
    String S_RESP_SUCCESS = "success";
    /** 返回失败message */
    String S_RESP_FAILED = "failed";

}