package com.gencode.issuetool.unittest;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Test;

import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.AuthUserInfo;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class EtcTest {

	@Test
	public void runGsonTest() {
		String jsonStr = "{\"resultCode\":\"0001\",\"resultMsg\":\"Success\",\"item\":[{\"id\":1.0,\"chatId\":0.0,\"customerId\":\"821042047792\",\"agentId\":null,\"lastMessageId\":13.0,\"lastMessage\":\"It's worth noting that the keyHolder object will contain the auto-generated key return from th\",\"direction\":\"I\",\"bizId\":\"A01\",\"country\":\"kr\",\"lang\":\"ko\",\"updatedDtm\":\"2020-04-30 00:43:52\",\"createdDtm\":\"2020-04-29 09:23:25\"}]}";
		Type type = new TypeToken<ResultObj<List<ChatSessionStatus>>>() {}.getType();
		Gson gson = GsonUtils.GetGson();
		ResultObj<List<ChatSessionStatus>> result = (ResultObj<List<ChatSessionStatus>>)gson.fromJson(jsonStr, type);
		List<ChatSessionStatus> authUserInfo = result.getItem();
		System.out.println(result.toString());
		System.out.println(result.getResultCode());
		System.out.println(result.getItem());
		System.out.println(authUserInfo.get(0).getBizId());
		
//		String json = "{ \"name\": \"Baeldung\", \"java\": true }";
//		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//		 
//		Assert.assertTrue(jsonObject.isJsonObject());
//		Assert.assertTrue(jsonObject.get("name").getAsString().equals("Baeldung"));
//		Assert.assertTrue(jsonObject.get("java").getAsBoolean() == true);
		String json = "{ \"name\": \"Baeldung\", \"java\": true }";
		JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
		assertTrue(convertedObject.isJsonObject());
		assertTrue(convertedObject.get("name").getAsString().equals("Baeldung"));
		assertTrue(convertedObject.get("java").getAsBoolean() == true);
	
	}
	
}
