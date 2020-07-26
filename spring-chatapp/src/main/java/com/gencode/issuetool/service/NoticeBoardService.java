package com.gencode.issuetool.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.gencode.issuetool.dao.NoticeBoardDao;
import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SortDirection;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;

@Service
public class NoticeBoardService {
	private final static Logger logger = LoggerFactory.getLogger(NoticeBoardService.class);

	@Autowired
	private NoticeBoardDao noticeBoardDao;


	@Transactional
	public Optional<NoticeBoard> add(NoticeBoard t) {
		long noticeId = noticeBoardDao.register(t);
		return noticeBoardDao.load(noticeId);
	}
	
	@Transactional
	public Optional<NoticeBoardEx> addEx(NoticeBoard t) {
		long noticeId = noticeBoardDao.register(t);
		return noticeBoardDao.loadEx(noticeId);
	}
	
	@Transactional
	public void update(NoticeBoard t) {
		noticeBoardDao.update(t);
	}

	public Optional<NoticeBoard> load(long id) {
		return noticeBoardDao.load(id);
	}
	
	public Optional<NoticeBoardEx> loadEx(long id) {
		return noticeBoardDao.loadEx(id);
	}
	
	@Transactional
	public void delete(long id) {
		noticeBoardDao.delete(id);
	}
	
	//add
	@Transactional
	public void register(NoticeBoard noticeBoard) throws TooManyRowException, IOException {
		noticeBoardDao.register(noticeBoard);		
	}

	public Optional<PageResultObj<List<NoticeBoard>>> search(PageRequest req) {
		return noticeBoardDao.search(req);
	}

	public Optional<PageResultObj<List<NoticeBoardEx>>> searchEx(PageRequest req) {
		return noticeBoardDao.searchEx(req);
	}
}
