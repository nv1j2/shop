package com.shop.manage.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_item_cat")	//表和类的映射
public class ItemCat extends BasePojo{
	@Id	//标识主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)	//自增，序列，UUID
	private Long id;
	
	@Column(name="parent_id")	//当属性和数据库的字段名称不一致时，增加此注解，如果配置了全局mybaits驼峰规则，此注解就不用写
	private Long parentId;
	private String name;
	private Integer status;
	
	@Column(name="sort_order")
	private Integer sortOrder;
	
	@Column(name="is_parent")
	private Boolean isParent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
	//为EasyUI.tree增加节点
	public String getText() {
		return this.getName();
	}
	public String getState(){
		//树枝要关闭，叶子节点打开
		return this.getIsParent()?"closed":"open";
	}
	
}
