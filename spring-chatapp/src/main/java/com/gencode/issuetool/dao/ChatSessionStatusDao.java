package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Optional;

import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.StatsPerDay;

public interface ChatSessionStatusDao extends Dao<ChatSessionStatus> {
	void deleteByChatId(long id);
	Optional<ChatSessionStatusEx> loadEx(long id);
	Optional<ChatSessionStatusEx> loadExByChatId(long id);

	Optional<List<ChatSessionStatus>> findByCustomerId(String customerId);
	Optional<List<ChatSessionStatusEx>> findByBizId(String bizId);
	Optional<List<ChatSessionStatusEx>> findByAgentId(String agentId);
	void resetUnreadCnt(long chatId);
	Optional<List<StatsPerDay>> getCustomerInboundCount(String bizId);
	int getWaitingCountForGoal(String bizId);
}
