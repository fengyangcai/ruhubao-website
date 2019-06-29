package cn.ruhubao.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.ruhubao.website.pojo.ContentCategory;
import cn.ruhubao.website.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
		
		try {
			//保存内容分类
			ContentCategory result=contentCategoryService.saveContentCategory( contentCategory);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
	
	/*
	 * public ResponseEntity<ContentCategory> getCategoryIds(ArrayList<Long> ids,
	 * Long categoryId){
	 * 
	 * contentCategoryService.c
	 * 
	 * }
	 */
	
}
