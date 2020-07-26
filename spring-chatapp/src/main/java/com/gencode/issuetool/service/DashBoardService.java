package com.gencode.issuetool.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gencode.issuetool.dao.ChatConsultDetailsDao;
import com.gencode.issuetool.dao.ChatSessionStatusDao;
import com.gencode.issuetool.dao.NoticeBoardDao;
import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SortDirection;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.GroupSum;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;
import com.gencode.issuetool.obj.StatsGoal;
import com.gencode.issuetool.obj.StatsPerDay;

@Service
public class DashBoardService {
	private final static Logger logger = LoggerFactory.getLogger(DashBoardService.class);

	@Autowired
	private NoticeBoardDao noticeBoardDao;

	@Autowired
	private ChatSessionStatusDao chatSessionStatusDao;
	
	@Autowired
	private ChatConsultDetailsDao chatConsultDetailsDao;

	private Map<String, Object> getWeekDays() {
		Map<String, Object> workDays = new HashMap<String, Object>();
		Date date = new Date();
		for (int i=1;i<=7;i++) {
			workDays.put(Utils.YYYYMMDDWithHypen(DateUtils.addDays(date, -7+i)), new Integer(0));
		}
		return workDays;
	}
	
	public Optional<List<StatsPerDay>> getCustomerInboundCount(String bizId) {
		Map<String, Object> workDays = getWeekDays();
		Optional<List<StatsPerDay>> list= chatSessionStatusDao.getCustomerInboundCount(bizId);
		List<StatsPerDay> resultList = new ArrayList<StatsPerDay>();
		list.get().forEach(e-> workDays.put(e.getStatsDate(), e.getCnt()));
		workDays.forEach((k, v) -> resultList.add(new StatsPerDay(k, new Integer((int)v))));
		return Optional.of(resultList);
	}
	
	public Optional<List<StatsPerDay>> getResponseCountByBiz(String bizId) {
		Map<String, Object> workDays = getWeekDays();
		Optional<List<StatsPerDay>> list= chatConsultDetailsDao.getResponseCountByBiz(bizId);
		List<StatsPerDay> resultList = new ArrayList<StatsPerDay>();
		list.get().forEach(e-> workDays.put(e.getStatsDate(), e.getCnt()));
		workDays.forEach((k, v) -> resultList.add(new StatsPerDay(k, new Integer((int)v))));
		return Optional.of(resultList);
	}
	
	public Optional<List<StatsPerDay>> getResponseCountByAgent(String agentId) {
		Map<String, Object> workDays = getWeekDays();
		Optional<List<StatsPerDay>> list= chatConsultDetailsDao.getResponseCountByAgent(agentId);
		List<StatsPerDay> resultList = new ArrayList<StatsPerDay>();
		list.get().forEach(e-> workDays.put(e.getStatsDate(), e.getCnt()));
		workDays.forEach((k, v) -> resultList.add(new StatsPerDay(k, new Integer((int)v))));
		return Optional.of(resultList);
	}
	
	public Optional<List<StatsPerDay>> getResolvedCountByBiz(String bizId) {
		Map<String, Object> workDays = getWeekDays();
		Optional<List<StatsPerDay>> list= chatConsultDetailsDao.getResolvedCountByBiz(bizId);
		List<StatsPerDay> resultList = new ArrayList<StatsPerDay>();
		list.get().forEach(e-> workDays.put(e.getStatsDate(), e.getCnt()));
		workDays.forEach((k, v) -> resultList.add(new StatsPerDay(k, new Integer((int)v))));
		return Optional.of(resultList);
	}
	
	public Optional<List<StatsPerDay>> getResolvedCountByAgent(String agentId) {
		Map<String, Object> workDays = getWeekDays();
		Optional<List<StatsPerDay>> list= chatConsultDetailsDao.getResolvedCountByAgent(agentId);
		List<StatsPerDay> resultList = new ArrayList<StatsPerDay>();
		list.get().forEach(e-> workDays.put(e.getStatsDate(), e.getCnt()));
		workDays.forEach((k, v) -> resultList.add(new StatsPerDay(k, new Integer((int)v))));
		return Optional.of(resultList);
	}

	public Optional<StatsGoal> getResolvedGoal(String bizId) {
		int resolvedCnt = chatConsultDetailsDao.getResolvedCountForGoal(bizId);
		int responseCnt = chatConsultDetailsDao.getResponseCountForGoal(bizId);
		int waitingCnt = chatSessionStatusDao.getWaitingCountForGoal(bizId);
		StatsGoal resultList = new StatsGoal(resolvedCnt , (responseCnt+waitingCnt));
		return Optional.of(resultList);
	}

	public Optional<List<GroupSum>> getConsultTypeCount(String bizId) {
		Optional<List<GroupSum>> consultTypeCount = chatConsultDetailsDao.getConsultTypeCount(bizId);
		return consultTypeCount;
	}
	
	public Optional<List<GroupSum>> getConsultStatusCount(String bizId) {
		Optional<List<GroupSum>> consultTypeCount = chatConsultDetailsDao.getConsultStatusCount(bizId);
		return consultTypeCount;
	}

	public Optional<PageResultObj<List<NoticeBoardEx>>> listNoticeBoard(PageRequest req) {
		return noticeBoardDao.searchEx(req);
	}
	
	public Optional<List<ChatSessionStatusEx>> listChatSessionStatus(String bizId) {
		return chatSessionStatusDao.findByBizId(bizId);
	}
}
