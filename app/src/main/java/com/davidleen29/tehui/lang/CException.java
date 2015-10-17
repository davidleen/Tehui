package com.davidleen29.tehui.lang;

/**
 * Created by davidleen29 on 2015/7/16.
 */

/**
 * 所有自定义异常的基类
 *
 */
public class CException extends  Exception {


    public static final int FAIL_ASYNC_CLIENT = 1000;
    public static final int FAIL_READ_RESPONSE_IO = 1001;
    public static final int FAIL_SCALE_IMAGE=1002;






    public String message;
    public int errorCode;

    protected CException(String s, Throwable cause) {
        super(s,cause);

    }

    public CException() {
        super();
    }

    public static CException create(int errorCode,Throwable cause)
    {
        CException hdException= new CException("", cause);
        hdException.errorCode=errorCode;
        hdException.message=cause.getLocalizedMessage();
        return hdException;
    }

    public static CException create(String message
    )
    {
        CException hdException= new CException();
        hdException.errorCode=-1;
        hdException.message=message;
        return hdException;
    }

    public static CException create( Throwable cause)
    {
        return create(-1,cause);
    }
}
