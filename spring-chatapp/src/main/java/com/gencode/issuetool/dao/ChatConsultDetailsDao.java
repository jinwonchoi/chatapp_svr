package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatConsultDetailsEx;
import com.gencode.issuetool.obj.GroupSum;
import com.gencode.issuetool.obj.StatsPerDay;

public interface ChatConsultDetailsDao extends Dao<ChatConsultDetails> {
	Optional<List<StatsPerDay>> getResponseCountByBiz(String bizId);
	Optional<List<StatsPerDay>> getResponseCountByAgent(String agentId);
	Optional<List<StatsPerDay>> getResolvedCountByBiz(String bizId);
	Optional<List<StatsPerDay>> getResolvedCountByAgent(String agentId);
	int getResolvedCountForGoal(String bizId);
	int getResponseCountForGoal(String bizId);
	Optional<List<GroupSum>> getConsultTypeCount(String bizId);
	Optional<List<GroupSum>> getConsultStatusCount(String bizId);
	Optional<ChatConsultDetailsEx> loadEx(long id);
	Optional<List<ChatConsultDetailsEx>> findBySessionId(long refSessionId);
	Optional<PageResultObj<List<ChatConsultDetailsEx>>> searchEx(String bizId, PageRequest req);
}
