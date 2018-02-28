package com.fast.dht.db.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fast.dht.net.model.Info;

@Document
public class Torrent extends SuperEntity {

	private String announce;
	private String[] announces;
	private long creationTime;
	private String comment;
	private String createdBy;
	private String type;
	private Info info;

	public String getAnnounce() {
		return announce;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	public String[] getAnnounces() {
		return announces;
	}

	public void setAnnounces(String[] announces) {
		this.announces = announces;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

}
