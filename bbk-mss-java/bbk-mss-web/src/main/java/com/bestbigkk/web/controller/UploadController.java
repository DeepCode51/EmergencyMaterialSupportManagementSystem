package com.bestbigkk.web.controller;

import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.service.qiniu.UploadService;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RW
@RequestMapping(value = "/dev/qiniu", produces = {"application/json;charset=UTF-8"})
@Api(tags = "七牛云SSO上传接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping(value = "/upload")
    @ApiOperation("上传SSO文件")
    public String upload(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new BusinessException("请提供要上传的文件");
        }
        try {
            return uploadService.uploadFile(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("七牛云上传错误", e);
        }
        throw new BusinessException("上传失败,请稍候重试!");
    }


}
