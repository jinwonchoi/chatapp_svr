package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Optional;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.obj.MessageQueue;

public interface MessageQueueDao extends Dao<MessageQueue> {
	Optional<PageResultObj<List<MessageQueue>>> loadByChatId(long chatId, PageRequest req);
}
