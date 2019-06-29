//加载完页面
		$(function(){
			//列表展示
			$("#list").datagrid({
				//url:抓取数据的url
				url:"../../"+action+"/listByPage.action",	
				//columns: 填充所有列
				   //field: 该列找哪个字段名称填充
				columns:columns,
				//开启分页
				pagination:true,
				//绑定工具条
				toolbar:"#toolbar"
			});	
			
			//搜索功能
			$("#searchBtn").click(function(){
				//一次性获取整合搜索表单的所有参数
				//serialize():　序列化表单的参数为字符串。格式：name=10&minWeight=10&minLength=20
				//alert(decodeURIComponent($("#searchForm").serialize()));
				
				//把表单的参数转换为json对象格式，调用load方法
				$("#list").datagrid("load",getFormData("searchForm"));
			});
			
			//弹出录入窗口
			$("#addBtn").click(function(){
				//清空表单数据
				$("#editForm").form("clear");
				
				//弹出window
				$("#editWin").window("open");
			});
			
			//保存表单数据
			$("#save").click(function(){
				//表单的提交
				$("#editForm").form("submit",{
					//提交后台的url
					url:"../../"+action+"/save.action",
					//提交之前的回调函数  该函数需要返回布尔值，true：提交表单  false:不提交表单
					onSubmit:function(){
						//判断表单如果全部验证通过，则提交表单；否则，则不提交
						return $("#editForm").form("validate");
					},
					//提交成功后的回调函数
					success:function(data){ //data:后台返回的数据  注意：data：是一个字符串类型
						//成功：{success:true}
						//失败：{success:false,msg:'错误信息'}
						//alert(data); 
						//把data字符串转换为json对象     var 对象  = eval(字符串)
						data = eval("("+data+")");
						
						if(data.success){
							//成功
							//刷新datagrid
							$("#list").datagrid("reload");
							//关闭window
							$("#editWin").window("close");
							
							$.messager.alert('提示','保存成功！','info');
						}else{
							//失败
							$.messager.alert('提示','保存失败！'+data.msg,'error');
						}
					}
				});
			});
			
			//回显表单数据
			$("#editBtn").click(function(){
				//判断只能选择一行数据
				var rows = $("#list").datagrid("getSelections");
				if(rows.length!=1){
					$.messager.alert("提示","修改操作只能选择一行","warning");
					return;
				}
				
				//填充表单的数据
				/* $("#editForm").form("load",{
					name:"10-20斤"
				}); */
				
				//先清空表单
				$("#editForm").form("clear");
				
				//动态数据填充   url必须返回一个json格式的数据 {name:xxx,minWeight:xx....}
				$("#editForm").form("load","../../"+action+"/findById.action?uuid="+rows[0].id);
				
				loadEditForm(rows[0]);
				
				//弹出window
				$("#editWin").window("open");
			});
			
			//批量删除
			$("#deleteBtn").click(function(){
				//判断至少选择一行
				var rows = $("#list").datagrid("getSelections");
				if(rows.length==0){
					$.messager.alert("提示","删除操作至少选择一行","warning");
					return;
				}
				
				//提示用户确认是否删除
				$.messager.confirm("提示","确认删除记录吗？一旦删除不能恢复啦!",function(value){
					if(value){
						//点击确定
						var ids = ""; // 格式： 1,2,3
						//获取选中的行的id
						//遍历rows   $(rows) : rows变量是js对象，each()是jQuery对象的方法      $(rows) ：把js对象转换为jQuery对象
						var idArray = new Array();
						$(rows).each(function(i){
							idArray.push(rows[i].id);
						});
						//idArray: [1 2 3]
						//join():把数组里面的每个元素逐个取出，以某个字符进行拼接，然后最终一个返回字符串
						ids = idArray.join(",");
						//提交到后台去处理    
						$.post("../../"+action+"/delete.action",{ids:ids},function(data){
							
							if(data.success){
								//刷新datagrid
								$("#list").datagrid("reload");
								
								$.messager.alert('提示','删除成功！','info');
							}else{
								$.messager.alert('提示','删除失败！'+data.msg,'error');
							}
							
						},"json");
					}
				});
				
			});
			
		});
		
		function loadEditForm(row){
			
		}