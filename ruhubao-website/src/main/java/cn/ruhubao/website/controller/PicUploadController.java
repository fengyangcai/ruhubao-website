package cn.ruhubao.website.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
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

	
	//这是一个java对象与jason格式字符串互转的工具lei
	
	private static final ObjectMapper MAPPER =new ObjectMapper();
	
	@RequestMapping(value = "/upload",produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String upload(HttpServletRequest request,@RequestParam("pictureFile")MultipartFile pictureFile) throws Exception {
		
		
		String uploadPath="";
		boolean checkPic = PicUploadUtils.checkPic(pictureFile);
		if (!checkPic) {
			return MAPPER.writeValueAsString("上传的文件有异常");
		}
		uploadPath = PicUploadUtils.getUploadPath(request, pictureFile);
		
		 return MAPPER.writeValueAsString(uploadPath);
		
		
		
	}
}
