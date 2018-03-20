package com.fast.dev.search.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fast.dev.search.model.PushData;

@Document
public class PushDataCache extends PushData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;

}
