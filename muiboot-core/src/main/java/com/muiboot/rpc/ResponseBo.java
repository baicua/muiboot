package com.muiboot.rpc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "ResponseBo")
public class ResponseBo<T> extends ResponseEntity<T> implements Serializable {
    private String msg;
    private int threads;
    public ResponseBo() {
        this( HttpStatus.OK);
    }

    public ResponseBo(T t) {
        this(t, HttpStatus.OK);
    }
    public ResponseBo(HttpStatus status) {
        this(null,status);
    }
    public ResponseBo(T t,HttpStatus status) {
        this(t,status,null);
    }
    public ResponseBo(HttpStatus status,String msg) {
        this(null,status,msg);
    }
    public ResponseBo(T t,HttpStatus status,String msg) {
        super(t,status);
        this.msg=msg;
    }

    public static ResponseBo error(String msg) {
        ResponseBo bo = new ResponseBo(HttpStatus.INTERNAL_SERVER_ERROR,msg);
        return bo;
    }

    public static ResponseBo limit(String msg) {
        ResponseBo bo = new ResponseBo(HttpStatus.TOO_MANY_REQUESTS,msg);
        return bo;
    }

    public static ResponseBo warn(String msg) {
        ResponseBo bo = new ResponseBo(HttpStatus.NO_CONTENT,msg);
        return bo;
    }

    public static ResponseBo ok(Object t) {
        ResponseBo ResponseBo = new ResponseBo(t);
        ResponseBo.msg="操作成功";
        return ResponseBo;
    }
    public static ResponseBo error() {
        return ResponseBo.error("");
    }

    @XmlElement
    public int getCode() {
        return this.getStatusCode().value();
    }
    @XmlElement
    public String getMsg() {
        return msg;
    }
    @XmlElement
    public int getThreads() {
        return AbstractService.threads_size.get();
    }
    @XmlElement
    public T getData() {
        return this.getBody();
    }
}
