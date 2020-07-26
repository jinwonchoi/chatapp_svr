package com.gencode.issuetool.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SearchMapObj;
import com.gencode.issuetool.obj.QnABoard;

@Component
public class QnABoardDaoImpl extends AbstractDaoImpl implements QnABoardDao {

	public QnABoardDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super(jdbcTemplate, namedParameterJdbcTemplate);
	}

	@Override
	public long register(QnABoard t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update("INSERT INTO qna_board(title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id)\r\n" + 
				"VALUES(:title,:content,:registerId,NOW(3),NOW(3),:postType,:postLevel,:refId )"
				,new BeanPropertySqlParameterSource(t), keyHolder);
		return (long) keyHolder.getKey().longValue();
	}

	@Override
	public Optional<QnABoard> load(long id) {
		QnABoard t = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(
				"select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from qna_board where id = :id",
				new MapSqlParameterSource("id",id ), (resultSet,i)->{
					return toQnABoard(resultSet);
				}));
		return Optional.of(t);
	}

	@Override
	public void delete(long id) {
		namedParameterJdbcTemplate.update("DELETE FROM qna_board where id = :id",
				new MapSqlParameterSource("id", id));
	}

	@Override
	public void update(QnABoard t) {
		namedParameterJdbcTemplate.update("UPDATE qna_board SET " +
				"title      =:title,"+ 
				"content    =:content,"+
				"register_id=:registerId,"+
				"updated_dtm =:NOW(3),"+
				"post_type  =:postType,"+
				"post_level =:postLevel,"+
				"ref_id     =:refId "+
				"WHERE id = :id"
				,new BeanPropertySqlParameterSource(t));
	}

	@Override
	public Optional<List<QnABoard>> loadAll() {
		List<QnABoard> list = jdbcTemplate.query(
				"select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from qna_board where 1=1", (resultSet, i) -> {
            return toQnABoard(resultSet);
        });
		return Optional.of(list);
	}

	@Override
	public Optional<List<QnABoard>> search(Map<String, String> map) {
		SearchMapObj searchMapObj = new SearchMapObj(map);
		List<QnABoard> t = namedParameterJdbcTemplate.query
				("select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from qna_board where 1=1"
						+ searchMapObj.andQuery()
						, searchMapObj.params()
						, new BeanPropertyRowMapper<QnABoard>(QnABoard.class));
		return Optional.of(t);
	}

	@Override
	public Optional<PageResultObj<List<QnABoard>>> search(PageRequest req) {
		String queryStr = "select id,title,content,register_id,updated_dtm,created_dtm,post_type,post_level,ref_id from qna_board where 1=1";
		return internalSearch(queryStr, req, QnABoard.class);
	}

	private QnABoard toQnABoard(ResultSet resultSet) throws SQLException {
		QnABoard obj = new QnABoard();
		obj.setId(resultSet.getLong("ID"));
		obj.setTitle(resultSet.getString("TITLE"));
		obj.setContent(resultSet.getString("CONTENT"));
		obj.setRegisterId(resultSet.getString("REGISTER_ID"));
		obj.setPostType(resultSet.getString("POST_TYPE"));
		obj.setPostLevel(resultSet.getInt("POST_LEVEL"));
		obj.setRefId(resultSet.getLong("REF_ID"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp("UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp("CREATED_DTM")));
		return obj;
	}
}
