package com.sycompany.rubato.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RMemberDto {

	private String mid;   //멤버 아이디
	private String mpw;   // 멤버의 비밀번호
	private String mname; // 멤버 이름
	private String memail; // 멤버 이메일
	private String mdate;  //멤버 가입일자
}

