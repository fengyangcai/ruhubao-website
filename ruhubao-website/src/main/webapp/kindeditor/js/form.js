//把传统的参数格式  转换 json格式
function conveterParamsToJson(paramsAndValues) {  
	//name=xx&minWeight=xx&minLength=xx 
	
	//1.创建json对象（没有属性）
    var jsonObj = {};  
  
    //2.切割每个keyv=value对
    var param = paramsAndValues.split("&");
    
    //3.遍历key-value对
    for ( var i = 0; param != null && i < param.length; i++) {  
    	//4.再次切割每个key-value对
        var para = param[i].split("=");  
        //para[0]:代表属性名称
        // para[1]: 代表属性值
        
        //给json对象添加属性，且赋值
        jsonObj[para[0]] = para[1];  
    }  
    //alert(JSON.stringify(jsonObj));
    //{name:xxx,minWeight:xxx,minLength:xxx}
    return jsonObj;  
}  
 
/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {   //searchForm
    var formValues = $("#" + form).serialize(); // name=xx&minWeight=xx&minLength=xx 
    //decodeURIComponent: 解决js的内容存在中文乱码问题
    return conveterParamsToJson(decodeURIComponent(formValues));  
}  