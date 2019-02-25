package com.muiboot.rpc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "ResponseBo")
public class ResponseDTO<T> extends ResponseEntity<T> implements Serializable {
    private String msg;
    private int threads;
    public ResponseDTO() {
        this( HttpStatus.OK);
        threads=AbstractService.threads_size.get();
    }

    public ResponseDTO(T t) {
        this(t, HttpStatus.OK);
    }
    public ResponseDTO(HttpStatus status) {
        this(null,status);
    }
    public ResponseDTO(T t, HttpStatus status) {
        this(t,status,null);
    }
    public ResponseDTO(HttpStatus status, String msg) {
        this(null,status,msg);
    }
    public ResponseDTO(T t, HttpStatus status, String msg) {
        super(t,status);
        this.msg=msg;
    }

    public static ResponseDTO error(String msg) {
        ResponseDTO bo = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,msg);
        return bo;
    }

    public static ResponseDTO limit(String msg) {
        ResponseDTO bo = new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS,msg);
        return bo;
    }

    public static ResponseDTO warn(String msg) {
        ResponseDTO bo = new ResponseDTO(HttpStatus.NO_CONTENT,msg);
        return bo;
    }

    public static ResponseDTO ok(Object t) {
        ResponseDTO ResponseBo = new ResponseDTO(t);
        ResponseBo.msg="操作成功";
        return ResponseBo;
    }
    public static ResponseDTO error() {
        return ResponseDTO.error("");
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
        return threads;
    }
    @XmlElement
    public T getData() {
        return this.getBody();
    }
}
