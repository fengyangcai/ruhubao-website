package cn.ruhubao.website.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import cn.ruhubao.website.pojo.Content;
import cn.ruhubao.website.pojo.DataGridResult;
import cn.ruhubao.website.service.ContentService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RequestMapping("/content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	
	private static Map<String, Object> result =new HashMap<String, Object>();
	
	@RequestMapping(value = "/controlle")
	public ResponseEntity<Map<String,Object>> controller(HttpServletRequest request){
		//默认需要传回去的参数
		
		/*
		  {
    "imageUrl": "http://localhost/ueditor/php/controller.php?action=uploadimage",
    "imagePath": "/ueditor/php/",
    "imageFieldName": "upfile",
    "imageMaxSize": 2048,
    "imageAllowFiles": [".png", ".jpg", ".jpeg", ".gif", ".bmp"]
    "其他配置项...": "其他配置值..."
			}
		 * */
		
		String imageUrl ="http://localhost:9091/pic/uploadimage";
		String imagePath="/ueditor/jsp/upload/";
		String imageFieldName="uploadimage";
		int imageMaxSize=2018;
		String[] imageAllowFiles= {".png", ".jpg", ".jpeg", ".gif",".bmp"};
		String qita="其他的配置";
		
		
		result.put(imageUrl,imageUrl);
		result.put("imagePath", imagePath);
		result.put("imageFieldName", imageFieldName);
		result.put("imageMaxSize", imageMaxSize);
		result.put("imageAllowFiles", imageAllowFiles);
		result.put("qita", qita);
		return ResponseEntity.ok(result);
		
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, Object>> deleteContent(
			@RequestParam(value = "ids", required = false) Long[] ids) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);

		try {
			if (ids != null && ids.length > 0) {
				contentService.deleteByIds(ids);
			}
			result.put("status", 200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(result);

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity<Void> updateContent(Content content) {

		try {
			contentService.updateSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 保存内容
	@RequestMapping(value = "/savaContent")
	public ResponseEntity<Void> savaContent(Content content) {
		/*
		 * 需要返回的数据格式为这个 { "imageUrl":
		 * "http://localhost/ueditor/php/controller.php?action=uploadimage",
		 * "imagePath": "/ueditor/php/", "imageFieldName": "upfile", "imageMaxSize":
		 * 2048, "imageAllowFiles": [".png", ".jpg", ".jpeg", ".gif", ".bmp"]
		 * "其他配置项...": "其他配置值..." }
		 * 
		 */
		

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
	 * 
	 * @param categoryId 分类id
	 * @param page       页号
	 * @param rows       页大小
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryContentListByPage(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "20") Integer rows) {

		try {
			DataGridResult dataGridResult = contentService.queryContentListByPage(categoryId, page, rows);

			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 使用freemarker生成html存储
	@RequestMapping(value = "/createHtml")
	public ResponseEntity<Void> createHtml(@RequestParam(value = "contentId", required = true) Integer id,
			HttpServletRequest request) throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {

		try {
			// 获取数据库的content的信息
			Content content = contentService.queryById(id);

			// 获取模板
			Configuration configuration = freeMarkerConfig.getConfiguration();

			Template template = configuration.getTemplate("content.ftl");

			HashMap<String, Object> map = new HashMap<String, Object>();
			// 把需要展示的内容存到map中。
			map.put("content", content.getContent());
			// map.put("content", "sdsrfsdfsdfgdsgdsfgsd");
			// String COMTENT_HTML_PATH="";
			// 获取输出的对象
			// FileWriter writer = new
			// FileWriter("/content/"+File.separator+content.getId()+"./html");

			// request.getre
			String url = request.getSession().getServletContext().getRealPath("/content");
			System.out.println(url);
			// E:\mygit\ruhubao-website\ruhubao-website\src\main\webapp\content
			// D:\\icaca\\aclass2tc\\20170626\\tt-html\\item
			// E:\\mygit\\ruhubao-website\\ruhubao-website\\src\\main\\webapp\\content"
			// FileWriter writer = new
			// FileWriter(request.getSession().getServletContext().getRealPath("/content/")+File.separator+".html");
			// 生成文件名称
			String fileUrlAndName = url + File.separator + "sd" + ".html";
			FileWriter writer = new FileWriter(fileUrlAndName);

			// 输出
			template.process(map, writer);
			// 在这里把路径写入content的url上
			content.setUrl(fileUrlAndName);
			// 更新content的数据
			contentService.saveSelective(content);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}

}
