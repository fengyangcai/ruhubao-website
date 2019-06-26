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

@RequestMapping("/pic")
@Controller
public class PicUploadController {

	//允许上传的图片类型
	private static final String[] IMAGE_TYPE= {".jpg",".png",".bmp",".jpeg",".gif"};
	
	//这是一个java对象与jason格式字符串互转的工具lei
	
	private static final ObjectMapper MAPPER =new ObjectMapper();
	
	@RequestMapping(value = "/upload",produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String upload(HttpServletRequest request,@RequestParam("pictureFile")MultipartFile pictureFile) throws Exception {
		
		String newImageName=null;
		boolean isLegal =false;
		String trueFilePath="";
		//校验图片
		for (String type : IMAGE_TYPE) {
			if (pictureFile.getOriginalFilename().lastIndexOf(type)>0) {
				isLegal=true;
				break;
			}
		}
		
		if (isLegal) {
			//校验图片内容
			try {
				//BufferedImage image = ImageIO.read(pictureFile.getInputStream());
				//String file_ext_name = StringUtils.substringAfter(multipartFile.getOriginalFilename(), ".");
				//获取文件的扩展名
				String ext = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
				//创建新的唯一图片名
				String uuidName = UUID.randomUUID().toString();
				//封装上传的文件将要存储的 全路径 = 路径 + 文件名
				newImageName =uuidName+ext;
				//设置图片上传路径
				String url =request.getSession().getServletContext().getRealPath("/upload");
				System.out.println(url);
				//绝对路径
				 trueFilePath=url+"/"+newImageName+ "." + ext;
				//以绝对路径保存重名命后的图片
		
				 pictureFile.transferTo(new File(trueFilePath));
				//把图片存储的路径保存到数据库
				//return trueFilePath;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		 return MAPPER.writeValueAsString(trueFilePath);
		
		
		
	}
}
