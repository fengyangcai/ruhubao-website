package cn.ruhubao.website.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ruhubao.website.utils.FycHttpclientUtils;



@Controller
@RequestMapping("/customer")
public class GetCustomersController {

	//
	@Value("${CUSTOMER_JIAOYU_DATA_URL}")
	private String CUSTOMER_JIAOYU_DATA_URL;
	//crm系统的获取的所有的数据路径
	@Value("${CUSTOMER_CRM_DATA_URL}")
	private String CUSTOMER_CRM_DATA_URL;
	
	//是一个java对象与json格式字符串互转的工具类
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	@RequestMapping(value = "/getxueliData", produces = "application/json; charset=utf-8")
	public ResponseEntity<String> getxueli() throws Exception {
		
			
			try {
				String objectString = FycHttpclientUtils.doget(CUSTOMER_JIAOYU_DATA_URL);
				System.out.println("获得的数据是s"+objectString);
				return ResponseEntity.ok(objectString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
				
	}
	@RequestMapping(value = "/getRuhuCrmData",produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getcrmData() {
		
		try {
			String string = FycHttpclientUtils.doget(CUSTOMER_CRM_DATA_URL);
			return ResponseEntity.ok(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
