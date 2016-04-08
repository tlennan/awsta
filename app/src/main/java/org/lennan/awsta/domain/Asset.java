package org.lennan.awsta.domain;

import org.springframework.data.annotation.Id;

public class Asset {
	@Id
	private String id;

	private String name;
	private String description;
	private String owner;

	public Asset() {
	}

	public Asset(String name, String description, String owner) {
		this.name = name;
		this.description = description;
		this.owner = owner;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
