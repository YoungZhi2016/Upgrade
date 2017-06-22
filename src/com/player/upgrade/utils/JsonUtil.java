package com.player.upgrade.utils;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class JsonUtil {

	private final static ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper();
	}

	/**
	 * @return new ObjectNode
	 */
	public static ObjectNode getObjectNode() {
		return MAPPER.createObjectNode();
	}

	/**
	 * @return new ArrayNode
	 */
	public static ArrayNode getArrayNode() {
		return MAPPER.createArrayNode();
	}

	/**
	 * @param filePath
	 *            JSON file path
	 * @return JsonNode
	 */
	public static JsonNode getJsonNodeByFilePath(String filePath) throws Exception {
		InputStreamReader aStreamReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		return MAPPER.readTree(aStreamReader);
	}
}
