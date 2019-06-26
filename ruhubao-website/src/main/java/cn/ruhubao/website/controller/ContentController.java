package cn.ruhubao.website.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ruhubao.website.pojo.Content;
import cn.ruhubao.website.pojo.DataGridResult;
import cn.ruhubao.website.service.ContentService;

@RequestMapping("/content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>>deleteContent(@RequestParam(value="ids", required = false)Long[] ids){
		
		HashMap<String,Object> result = new HashMap<String, Object>();
		result.put("status", 500);
		
		try {
			if (ids!=null&&ids.length>0) {
				contentService.deleteByIds(ids);
			}
			result.put("status", 200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public ResponseEntity<Void> updateContent(Content content){
		
		try {
			contentService.updateSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	//保存内容
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> savaContent(Content content){
		
		try {
			contentService.saveSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	/**
	 * 根据内容分类id查询该分类的分页内容列表
	 * @param categoryId 分类id
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryContentListByPage(
			@RequestParam(value = "categoryId", defaultValue = "0")Long categoryId,
			@RequestParam(value="page", defaultValue="1")Integer page,
			@RequestParam(value = "rows",defaultValue="20")Integer rows
			){
		
		try {
			DataGridResult dataGridResult =contentService.queryContentListByPage(categoryId, page, rows);
			
			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//返回500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
