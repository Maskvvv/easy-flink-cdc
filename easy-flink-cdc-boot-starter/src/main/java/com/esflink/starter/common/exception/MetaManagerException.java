package com.esflink.starter.common.exception;


/**
 * @author jianghang 2012-7-12 上午10:10:31
 * @version 1.0.0
 */
public class MetaManagerException extends RuntimeException {

    private static final long serialVersionUID = -654893533794556357L;

    public MetaManagerException(String errorCode) {
        super(errorCode);
    }

    public MetaManagerException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public MetaManagerException(String errorCode, String errorDesc) {
        super(errorCode + ":" + errorDesc);
    }

    public MetaManagerException(String errorCode, String errorDesc, Throwable cause) {
        super(errorCode + ":" + errorDesc, cause);
    }

    public MetaManagerException(Throwable cause) {
        super(cause);
    }

    public Throwable fillInStackTrace() {
        return this;
    }

}
