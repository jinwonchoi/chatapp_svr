package com.gencode.issuetool.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;
import com.gencode.issuetool.obj.User;
import com.gencode.issuetool.service.NoticeBoardService;

@RestController
@RequestMapping("/notice-board")
@CrossOrigin(origins = "${cors_url}")
public class NoticeBoardController {

	private final static Logger logger = LoggerFactory.getLogger(NoticeBoardController.class);
	@Autowired
	private NoticeBoardService noticeBoardService;

		@RequestMapping(method=RequestMethod.POST, value="/add")
	ResultObj<NoticeBoard> addNoticeBoard(@RequestBody NoticeBoard notice) {
		try {
			logger.info(notice.toString());
			NoticeBoard resultNotice = noticeBoardService.add(notice).get();
			return ResultObj.<NoticeBoard>success(resultNotice);
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<NoticeBoard>errorUnknown();
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/update")
	ResultObj<String> updateNoticeBoard(@RequestBody NoticeBoard notice) {
		try {
			noticeBoardService.update(notice);
			return ResultObj.<String>success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<String>errorUnknown();
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/delete/{id}")
	ResultObj<String> deleteNoticeBoard(@PathVariable(name="id") long id) {
		try {
			noticeBoardService.delete(id);
			return ResultObj.<String>success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<String>errorUnknown();
		}
	}
	
	@RequestMapping("/{id}") 
	ResultObj<NoticeBoard> getNoticeBoard(@PathVariable(name="id") long id) {
		try {
			Optional<NoticeBoard> notice = noticeBoardService.load(id);
			if (notice.isPresent()) 
				return new ResultObj<NoticeBoard>(ReturnCode.SUCCESS, notice.get());
			else 
				return ResultObj.<NoticeBoard>dataNotFound();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<NoticeBoard>errorUnknown();
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/search")
	public PageResultObj<List<NoticeBoardEx>> searchTask(@RequestBody PageRequest req) {
		try {
			System.out.println(req.toString());
			Optional<PageResultObj<List<NoticeBoardEx>>> list = noticeBoardService.searchEx(req);
			if (list.isPresent()) {
				return new PageResultObj<List<NoticeBoardEx>>(ReturnCode.SUCCESS, list.get());
			} else {
				return PageResultObj.<List<NoticeBoardEx>>dataNotFound();
			}
		} catch (Exception e) {
			logger.error("normal error", e);
			return PageResultObj.<List<NoticeBoardEx>>errorUnknown();
		}
	}
	
}
