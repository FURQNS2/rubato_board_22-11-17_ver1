package com.sycompany.rubato.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RFBoardDto {

	private int rfbnum;   // 전체 게시판에 작성된 글의 순서(시퀀스)
	private String rfbname;   // 작성자 이름
	private String rfbuserid;  // 작성자 아이디
	private String rfbtitle;  // 작성된 글의 제목
	private String rfbcontent;  // 작성된 글
	private int rfbhit;   // 조회수
	private String rfbdate; // 게시일
	private int rfbreplycount;  // 댓글 개수
	private int filecount; //첨부된 파일 개수
}
