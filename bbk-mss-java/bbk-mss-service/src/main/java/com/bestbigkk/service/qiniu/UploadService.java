package com.bestbigkk.service.qiniu;

import com.qiniu.common.QiniuException;

import java.io.InputStream;

public interface UploadService {

    String uploadFile(InputStream inputStream, String fileName) throws QiniuException;

}
