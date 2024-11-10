package com.bestbigkk.common.enums;

/**
* @author: 开
* @date: 2020-04-19 20:01:03
* @describe: 系统身份
*/
public enum  Identity {

    /**
     * 身份
     */
    EVENT_OPERATOR(1, "应急事件响应工作人员"),
    APPROVAL_OPERATOR(2, "审批工作人员"),
    MATERIAL_OPERATOR(3, "物资仓储管理人员"),
    SYSTEM_OPERATOR(4, "系统运维人员");

    public Integer identityCode;
    public String identityName;

    Identity(int identityCode, String identityName) {
        this.identityCode = identityCode;
        this.identityName = identityName;
    }

}
