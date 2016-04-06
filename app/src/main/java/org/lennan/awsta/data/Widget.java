package org.lennan.awsta.data;

import org.springframework.data.annotation.Id;

public class Widget {
	@Id
	private String id;

	private String name;
	private String description;

	public Widget() {
	}

	public Widget(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
