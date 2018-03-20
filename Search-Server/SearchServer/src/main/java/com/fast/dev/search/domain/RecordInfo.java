package com.fast.dev.search.domain;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fast.dev.search.model.FileModel;

@Document
public class RecordInfo extends Record {

	@Id
	private String id;

	// 文件列表
	private Collection<FileModel> files;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the files
	 */
	public Collection<FileModel> getFiles() {
		return files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(Collection<FileModel> files) {
		this.files = files;
	}

}
