package com.bestbigkk.simple;

import com.bestbigkk.common.web.ResultCode;
import org.junit.Test;

import java.io.IOException;

/**
* @author: 开
* @date: 2020-03-24 16:03:36
* @describe: 普通Java测试
*/
public class SimpleTest {

    @Test
    public void test() throws IOException {
        f();
        f("1");
    }

    public void f(String...a){
        System.out.println(a.length);

    }

}
