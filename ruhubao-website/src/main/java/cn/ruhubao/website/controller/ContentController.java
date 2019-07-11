package cn.ruhubao.website.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import cn.ruhubao.website.pojo.Content;
import cn.ruhubao.website.pojo.ContentCategory;
import cn.ruhubao.website.pojo.DataGridResult;
import cn.ruhubao.website.pojo.DataGridResult2;
import cn.ruhubao.website.service.ContentCategoryService;
import cn.ruhubao.website.service.ContentService;
import freemarker.template.Configuration;
import freemarker.template.Template;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	@Autowired
	private ContentCategoryService contentCategroryService;

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@Value("${CONTENT_HTML_PATH}")
	private String CONTENT_HTML_PATH;

	// linux下的相对路径/home/pic
	@Value("${PIC_PATH}")
	private String PIC_PATH;
	// 对外的访问路劲：http://pic.bolinjy.cn
	@Value("${PIC_REQUEST_URL}")
	private String PIC_REQUEST_URL;

	private static Map<String, Object> result = new HashMap<String, Object>();

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, Object>> deleteContent(
			@RequestParam(value = "ids", required = false) Long[] ids, HttpServletRequest request,
			HttpServletResponse response) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("status", 500);

		try {
			if (ids != null && ids.length > 0) {
				contentService.deleteByIds(ids);
				// 根据商品id到 特定路径 下将对应的静态页面删除
				for (Long itemId : ids) {
					// E:\\mygit\\ruhubao-website\\ruhubao-website\\src\\main\\webapp\\content\\ftl\\37.html
					String filePath = request.getSession().getServletContext().getRealPath("/content/ftl")
							+ File.separator + itemId + ".html";
					// String filePath = UrlRequestUtils.getContextUrl(request)
					// +"content"+File.separator +"ftl"+File.separator + itemId + ".html";//拿到路径全称。

					File file = new File(filePath);
					if (file.exists()) {
						file.delete();

					}
				}
			}
			result.put("status", 200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(result);

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity<Void> updateContent(Content content, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			// 先把cotent的id 的图片地址查询处理
			Content content2 = contentService.queryById(content.getId());
			// 把之前的图片删除在设置这个新的图片上去
			if (content.getPic() != null && !content.getPic().equals("")) {
				// 有新的图片地址，把之前的图片删除
				String pic = content2.getPic();
				// 获取到文件名
				String fileName = pic.substring(pic.lastIndexOf("/") + 1);
				File file = new File(PIC_PATH + fileName);
				if (file.exists()) {
					file.delete();
				}
			}
			if (content.getPic2() != null && !content.getPic2().equals("")) {
				// 有新的文件地址，把之前的删除
				String pic2 = content2.getPic2();
				String fileName2 = pic2.substring(pic2.lastIndexOf("/") + 1);
				File file2 = new File(PIC_PATH + fileName2);
				if (file2.exists()) {
					file2.delete();
				}

			}

			contentService.updateSelective(content);
			// 重新删除生成新的静态页面
			String filePath = request.getSession().getServletContext().getRealPath("/content/ftl") + File.separator
					+ content.getId() + ".html";
			if (filePath != null) {

				boolean b = new File(filePath).delete();
				System.out.println("删除成功了吗：" + b);
			}
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("content.ftl");
			// String url =
			// request.getSession().getServletContext().getRealPath("/content/ftl");
			FileWriter writer = new FileWriter(new File(filePath));
			Map<String, Object> map = new HashMap<String, Object>();

			String strUrl = CONTENT_HTML_PATH + File.separator + "content" + File.separator + "ftl" + File.separator
					+ content.getId() + ".html";// 拿到路径全称。
			content.setUrl(strUrl);
			System.out.println(content);
			// 把需要展示的内容存到map中。
			ContentCategory contentCategory = contentCategroryService.queryById(content.getCategoryId());
			map.put("contentCategory", contentCategory.getName());
			map.put("content", content.getContent());
			map.put("title", content.getTitle());
			// pic来到这里是null,
			map.put("pic", content.getPic());
			map.put("created", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			map.put("url", strUrl);
			if (content.getPic2() != null && !content.getPic2().equals("")) {
				map.put("file", content.getPic2());

			} else {

				map.put("file", "kong");
			}

			// 输出
			template.process(map, writer);
			// 在这里把路径写入content的url上
			// 更新content的数据
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
	public ResponseEntity<Void> savaContent(Content content, HttpServletRequest request) {

		try {
			System.out.println("游离态：" + content);
			contentService.saveSelective(content);
			System.out.println("瞬时态：" + content);
			Long id = content.getId();
			/* 这里保存他的一个访问的路径 */
			// 获取模板
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("content.ftl");

			String fileUrlAndName = request.getSession().getServletContext().getRealPath("/content/ftl")
					+ File.separator + id + ".html";

			FileWriter writer = new FileWriter(new File(fileUrlAndName));
			Map<String, Object> map = new HashMap<String, Object>();

			// String strUrl = UrlRequestUtils.getContextUrl(request)
			// +"content"+File.separator +"ftl"+File.separator + id + ".html";//拿到路径全称。
			String strUrl = CONTENT_HTML_PATH + File.separator + "content" + File.separator + "ftl" + File.separator
					+ id + ".html";
			content.setUrl(strUrl);
			// 把需要展示的内容存到map中。
			ContentCategory contentCategory = contentCategroryService.queryById(content.getCategoryId());
			map.put("contentCategory", contentCategory.getName());
			map.put("content", content.getContent());
			map.put("title", content.getTitle());
			map.put("pic", content.getPic());
			map.put("created", new SimpleDateFormat("yyyy-MM-dd").format(content.getCreated()));
			map.put("url", strUrl);
			if (content.getPic2() != null && !content.getPic2().equals("")) {
				map.put("file", content.getPic2());

			} else {

				map.put("file", "kong");
			}
			map.put("nextUrl", "");
			map.put("nextTitle", "");

			// 输出
			template.process(map, writer);
			// 在这里把路径写入content的url上
			// 更新content的数据
			contentService.updateSelective(content);
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
			@RequestParam(value = "rows", defaultValue = "5") Integer rows) {

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

	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public ResponseEntity<DataGridResult2> qeuryContentListBypage2(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows) {
		try {
			DataGridResult2 dataGridResult2 = contentService.queryContentListByPage2(categoryId, page, rows);
			dataGridResult2.setCode(0L);
			return ResponseEntity.ok(dataGridResult2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value = "/queryAll", method = RequestMethod.GET)
	public ResponseEntity<DataGridResult> queryAllContentListByPage(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows) {

		try {
			DataGridResult dataGridResult = contentService.queryAllContentListByPage(page, rows);

			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}

	@RequestMapping(value = "/queryContentListByName", method = RequestMethod.POST)
	public ResponseEntity<DataGridResult> queryContentListByName(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows,
			@RequestParam(value = "name") String name) {
		try {
			ContentCategory cc = new ContentCategory();
			/*
			 * String contentCategoryName = new
			 * String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8");
			 */
			cc.setName(name);
			List<ContentCategory> list = contentCategroryService.queryByWhere(cc);
			Long id = 0L;
			if (list != null && list.size() > 0) {
				id = list.get(0).getId();
			}
			DataGridResult dataGridResult = contentService.queryAllContentListByCategroryId(id, page, rows);
			return ResponseEntity.ok(dataGridResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}

}
