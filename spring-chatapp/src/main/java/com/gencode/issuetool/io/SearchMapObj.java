package com.gencode.issuetool.io;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.gencode.issuetool.etc.Utils;

public class SearchMapObj {
	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	String andStr;
	
	public SearchMapObj(Map<String, String> map) {
		generateStr(map,"");
	}

	public SearchMapObj(Map<String, String> map, String alias) {
		generateStr(map,alias);
	}

	public void generateStr(Map<String, String> map, String alias) {

		StringBuffer sb = new StringBuffer();
		if (map == null || map.size()==0) {
			andStr = "";
		} else {
			map.forEach((k,v)->{
				String unCamelK=Utils.unCamel(k);
				System.out.println(unCamelK+":"+v);
				sb.append(String.format(" and %s%s like :%s",alias,unCamelK, k));
				this.namedParameters.addValue(k, "%" + v + "%");
			});
			andStr = sb.toString();
		}
	}
	
	public MapSqlParameterSource params() {
		return namedParameters;
	}

	public Map<String, Object> map() {
		return namedParameters.getValues();
	}

	public String andQuery() {
		return andStr;
	}
}
