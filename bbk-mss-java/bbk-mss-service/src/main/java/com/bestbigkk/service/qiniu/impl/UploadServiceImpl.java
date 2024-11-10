package com.bestbigkk.service.qiniu.impl;

import com.alibaba.fastjson.JSON;
import com.bestbigkk.common.config.oss.qiniu.QiniuConfig;
import com.bestbigkk.service.qiniu.UploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
* @author: 开
* @date: 2020-04-25 12:24:23
* @describe: 七牛云上传实现
*/
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private QiniuConfig qiniuProperties;

    @Override
    public String uploadFile(InputStream inputStream, String fileName) throws QiniuException {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone0());
            UploadManager uploadManager = new UploadManager(cfg);
            //默认不指定key的情况下，以文件内容的hash值作为文件名
                Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
                String upToken = auth.uploadToken(qiniuProperties.getBucket());
                try {
                    Response response = uploadManager.put(inputStream, fileName, upToken, null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                    String return_path = qiniuProperties.getPath()+"/"+putRet.key;
                    log.info("七牛云上传地址:{}", return_path);
                    return return_path;
                } catch (QiniuException ex) {
                    log.error("上传错误", ex);
                }
        return "";
    }

}
