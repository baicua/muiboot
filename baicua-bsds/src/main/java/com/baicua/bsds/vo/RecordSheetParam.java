package com.baicua.bsds.vo;

import com.baicua.bsds.domain.RecordSheet;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Created by 75631 on 2018/7/22.
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordSheetParam extends RecordSheet implements Serializable{
    private MultipartFile[] files;
    @JsonIgnore
    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
