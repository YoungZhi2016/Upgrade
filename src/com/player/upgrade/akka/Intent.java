package com.player.upgrade.akka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.player.upgrade.utils.JsonUtil;

public class Intent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String action;

	private List<Map<String, Object>> value;

	public Intent() {
		value = new ArrayList<>();
	}

	@Override
	public String toString() {
		String json = null;
		try {
			json = JsonUtil.getStringByObject(this);
		} catch (JsonProcessingException e) {
		}
		return json;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Map<String, Object>> getValue() {
		return value;
	}

	public void addValue(Map<String, Object> aMap) {
		value.add(aMap);
	}
}
