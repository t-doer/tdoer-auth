package com.tdoer.auth.constants;

public interface Enums {

    public enum SMSSignature {
        BBKJ("BBKJ", "百邦科技"), SDF("SDF", "闪电蜂"), BBLIFE("BBSH", "百邦生活");

        private String value;
        private String desc;

        SMSSignature(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return this.value.concat(":").concat(this.desc);
        }
    }

    /**
     * 发送短信验证码的操作类型
     */
    public enum SMSVerifyOperation {
        LOGIN(0, "登录操作"), RESET_PASSWD(1, "重置密码"), EMPTY(99, "");

        private int value;
        private String desc;

        SMSVerifyOperation(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static SMSVerifyOperation resolve(int value) {
            for (SMSVerifyOperation op : values()) {
                if (op.value == value){
                    return op;
                }
            }
            return EMPTY;
        }
    }
}