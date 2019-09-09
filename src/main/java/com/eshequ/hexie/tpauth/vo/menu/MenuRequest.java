package com.eshequ.hexie.tpauth.vo.menu;

import java.util.List;

public class MenuRequest {

	private String id;
	private String title;
	private String newtext;
	private List<SubMenu> subText;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SubMenu> getSubText() {
		return subText;
	}

	public void setSubText(List<SubMenu> subText) {
		this.subText = subText;
	}

	public String getNewtext() {
		return newtext;
	}

	public void setNewtext(String newtext) {
		this.newtext = newtext;
	}


	static class SubMenu {

		private String id;
		private String title;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

}
