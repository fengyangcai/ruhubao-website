package cn.ruhubao.website.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ruhubao.website.utils.PicUploadUtils;

@RequestMapping("/pic")
@Controller
public class PicUploadController {

	// 这是一个java对象与jason格式字符串互转的工具lei

	private static final ObjectMapper MAPPER = new ObjectMapper();

	//@RequestMapping(value = "/uploadimage", produces = MediaType.TEXT_HTML_VALUE)
	@RequestMapping(value = "/uploadimage")
	public ResponseEntity<Map<String, Object>> upload(HttpServletRequest request,
			@RequestParam("pictureFile") MultipartFile pictureFile) throws Exception {
		// 需要支持callback参数,返回jsonp格式
		/*
		 * { "imageUrl": "http://localhost/ueditor/php/controller.php?action=uploadimage",
		 * "imagePath": "/ueditor/php/", 
		 * "imageFieldName": "upfile",
		 *  "imageMaxSize":2048, 
		 *  "imageAllowFiles": [".png", ".jpg", ".jpeg", ".gif", ".bmp"] 
		 *  
		 { "state": "SUCCESS", 
		  "url": "upload/demo.jpg", 
		  "title": "demo.jpg",
		  "original": "demo.jpg" }
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();

		boolean checkPic = PicUploadUtils.checkPic(pictureFile);
		if (!checkPic) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		// 获取文件的后缀名。
		String ext = FilenameUtils.getExtension(pictureFile.getOriginalFilename());

		String uuidName = UUID.randomUUID().toString();

		String url = request.getSession().getServletContext().getRealPath("/ueditor/jsp/upload/image/");
		String name = request.getServerName();
		int port = request.getServerPort();
		String servletPath = request.getServletPath();
		System.out.println(url);
		/// pic/upload
		System.out.println(name + ":" + port  + servletPath);
		// 绝对路径:名称全部
		String trueFilePath = url + "/" + uuidName + "." + ext;
		// 以绝对路径保存重名命后的图片
		pictureFile.transferTo(new File(trueFilePath));
		// pictureFile.transferTo(new File(trueFilePath));
		String returnPath = name + ":" + port + "/" + "/ueditor/jsp/upload/image/"  + uuidName + "." + ext;
		
		map.put("state", "success");
		map.put("url", returnPath);
		map.put("title",uuidName+"." + ext);
		map.put("original", uuidName+"." + ext);
				
		return ResponseEntity.ok(map);

	}
}
