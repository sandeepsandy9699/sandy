package com.sbms.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SBMS")
public class Sbms {

	@Id
	private String date;
	private List<Long> data;
	private List<String> label;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Long> getData() {
		return data;
	}
	public void setData(List<Long> data) {
		this.data = data;
	}
	public List<String> getLabel() {
		return label;
	}
	public void setLabel(List<String> label) {
		this.label = label;
	}

	

	

}
