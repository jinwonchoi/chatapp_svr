package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Optional;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.obj.MessageLog;

public interface MessageLogDao extends Dao<MessageLog> {
	Optional<PageResultObj<List<MessageLog>>> loadByChatId(long chatId, boolean viewPrevChat, PageRequest req);
	void updateByChatId(long chatId, String status);
}
