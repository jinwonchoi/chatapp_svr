package com.gencode.issuetool.dao;

import java.util.List;
import java.util.Optional;

import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;

public interface NoticeBoardDao extends Dao<NoticeBoard> {
	Optional<NoticeBoardEx> loadEx(long id);
	Optional<PageResultObj<List<NoticeBoardEx>>> searchEx(PageRequest req);
	
}
