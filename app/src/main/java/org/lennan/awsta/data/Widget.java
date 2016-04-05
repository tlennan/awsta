package org.lennan.awsta.data;

import org.springframework.data.annotation.Id;

public class Widget {
	@Id
	private Long id;
	
	private String name;
	private String description;
	
	public Widget() {
	}
	
	public Widget(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
