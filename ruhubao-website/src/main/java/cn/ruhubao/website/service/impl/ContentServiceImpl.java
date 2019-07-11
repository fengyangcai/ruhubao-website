package cn.ruhubao.website.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.ruhubao.website.mapper.ContentCategoryMapper;
import cn.ruhubao.website.mapper.ContentMapper;
import cn.ruhubao.website.pojo.Content;
import cn.ruhubao.website.pojo.ContentCategory;
import cn.ruhubao.website.pojo.DataGridResult;
import cn.ruhubao.website.pojo.DataGridResult2;
import cn.ruhubao.website.service.ContentService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;

	@Override
	public DataGridResult queryContentListByPage(Long categoryId, Integer page, Integer rows) {

		// 根据内容分类id分页查询该分类下的内容列表并根据更新时间降序排序
		Example example = new Example(Content.class);

		// 设置查询条件
		example.createCriteria().andEqualTo("categoryId", categoryId);

		// 设置排序
		example.orderBy("updated").desc();

		// 设置分页
		PageHelper.startPage(page, rows);

		List<Content> list = contentMapper.selectByExample(example);

		PageInfo<Content> pageInfo = new PageInfo<>(list);
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public DataGridResult queryAllContentListByPage(Integer page, Integer rows) {

		Example example = new Example(Content.class);

		example.orderBy("updated").desc();
		// 设置分页
		PageHelper.startPage(page, rows);

		List<Content> contents = contentMapper.selectByExample(example);
		PageInfo<Content> pageInfo = new PageInfo<>(contents);
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());

	}

	@Override
	public DataGridResult2 queryContentListByPage2(Long categoryId, Integer page, Integer rows) {

		// 根据内容分类id分页查询该分类下的内容列表并根据更新时间降序排序
		Example example = new Example(Content.class);

		// 设置查询条件
		example.createCriteria().andEqualTo("categoryId", categoryId);

		// 设置排序
		example.orderBy("updated").desc();

		// 设置分页
		PageHelper.startPage(page, rows);

		List<Content> list = contentMapper.selectByExample(example);

		PageInfo<Content> pageInfo = new PageInfo<>(list);

		DataGridResult2 result2 = new DataGridResult2();
		result2.setData(list);
		result2.setTotal(pageInfo.getTotal());
		return result2;
	}

	// 根据类目id遍历查询其下的所有的文章
	@Override
	public DataGridResult queryAllContentListByCategroryId(Long categoryId, Integer page, Integer rows) {
		ArrayList<Long> ids = new ArrayList<Long>();
		getContentCategoryId(ids, categoryId);
		System.out.println("----------------------------------------");
		System.out.println(ids);
		System.out.println("----------------------------------------");
		Example example = new Example(Content.class);
		//example.createCriteria().andIn("categoryId", ids);
		Criteria criteria = example.createCriteria();
		criteria.andIn("categoryId", ids);
		example.orderBy("updated").desc();
		PageHelper.startPage(page, rows);
		List<Content> list = contentMapper.selectByExample(example);
		// 这里查询完了把contentCategoryIds清除一下
		PageInfo<Content> pageInfo = new PageInfo<>(list);
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());

	}

	//private static ArrayList<Long> contentCategoryIds = new ArrayList<Long>();

	private void getContentCategoryId(ArrayList<Long> ids,Long categoryId) {

		ContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(categoryId);

		if (contentCategory != null) {
			Boolean isParent = contentCategory.getIsParent();
			if (isParent) { // 查询下一级 Example example =new
				Example example =new Example(ContentCategory.class);
				Criteria criteria = example.createCriteria();
				criteria.andEqualTo("parentId", contentCategory.getId());
				List<ContentCategory> list2 = contentCategoryMapper.selectByExample(example);
				System.out.println(list2);
				for (ContentCategory contentCategory2 : list2) {
					ids.add(contentCategory2.getId());
					if (contentCategory2.getIsParent()) {
						getContentCategoryId(ids,contentCategory2.getId());
					}
				}

			} else {
				Long id = contentCategory.getId();
				//System.out.println("idwei阿斯顿顶顶顶顶顶顶顶顶顶顶" + id);
				ids.add(id);
			}

		}

	}

	
}
