package cn.ruhubao.website.pojo;

import java.util.List;

/**  

* <p>Title: DataGridResult2</p>  

* <p>Description: </p>  

* @author fyc 

* @date 2019年7月6日  

*/  
public class DataGridResult2 {
	
	 private Long total;
	 
	 private Long code;
	 
	 private List<?> data;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	 
	 
}
