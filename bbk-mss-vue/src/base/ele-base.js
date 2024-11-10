
export function resetForm(formName, callBack) {
    console.log("重置表单"+formName);
    this.$refs[formName].resetFields();
    if(callBack!==undefined){
        callBack();
    }
}

export function submitForm(formName, validCall, invalidCall) {
    this.$refs[formName].validate((valid) => {
        console.log("校验表单"+formName);
        if (valid) {
            if(validCall!==undefined){
                validCall();
            }
        } else {
            this.$alert('需要按照规定输入正确的数据才可以提交', '表单校验不通过', {confirmButtonText: '我知道了', type:'warning', showClose:false});
            if (invalidCall!==undefined) {
                invalidCall();
            }
            return false;
        }
    });
}