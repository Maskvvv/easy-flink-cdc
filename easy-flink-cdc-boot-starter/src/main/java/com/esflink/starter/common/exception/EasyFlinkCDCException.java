package com.esflink.starter.common.exception;


/**
 * @author jianghang 2012-7-12 上午10:10:31
 * @version 1.0.0
 */
public class EasyFlinkCDCException extends RuntimeException {

    private static final long serialVersionUID = -654893533794556357L;

    public EasyFlinkCDCException(String errorCode) {
        super(errorCode);
    }

    public EasyFlinkCDCException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public EasyFlinkCDCException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public EasyFlinkCDCException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public EasyFlinkCDCException(Throwable cause) {
        super(cause);
    }

    public Throwable fillInStackTrace() {
        return this;
    }

}
