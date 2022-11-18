package com.sycompany.rubato.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.sycompany.rubato.dao.IDao;
import com.sycompany.rubato.dto.RFBoardDto;
import com.sycompany.rubato.dto.RReplyDto;

@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	
	//private IDao dao = sqlSession.getMapper(IDao.class);
	
	@RequestMapping("/")
	public String HomeIndex() {
		return "redirect:index";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/board_list")
	public String board_list(Model model) {

		IDao dao = sqlSession.getMapper(IDao.class);
		ArrayList<RFBoardDto> boardDtos = dao.rfblist();
		int boardcount = dao.rfboardAllCount(); // 글 목록 개수
		model.addAttribute("boardList", boardDtos); // 글 목록 전부 보여주기
		model.addAttribute("boardCount", boardcount); //글 목록 총개수
		
		return "board_list";
	}
	
	@RequestMapping("/board_view")
	public String board_view(HttpServletRequest request, Model model, HttpSession session) {

		IDao dao = sqlSession.getMapper(IDao.class);			
		
		String rfbnum = request.getParameter("rfbnum");
		
		RFBoardDto rfboardDto1 = dao.rfboardView(rfbnum);
		
		if(rfboardDto1.getRfbuserid().equals(session.getAttribute("memberId"))) {
			
		}else{
		//글 수정하는 update사용
		//조회수
		//변경 후 dto에 싣어줘야 한다.
		dao.rfbhit(rfbnum);  
		}
		
		// 글 내용보는 select문 사용
		RFBoardDto rfboardDto2 = dao.rfboardView(rfbnum);
		
		model.addAttribute("rfbView", rfboardDto2);
		
		String rrorinum = request.getParameter("rfbnum");
		ArrayList<RReplyDto> replyDtos = dao.rrlist(rrorinum);
		model.addAttribute("replylist",replyDtos);
		
		return "board_view";
	}
	
	@RequestMapping("/board_write")
	public String board_write(HttpServletResponse response,HttpServletRequest request, HttpSession session) {

		String sessionId = (String)session.getAttribute("memberId");
		if(sessionId == null) {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓸 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			
		}
		
		return "board_write";
	}
	
	@RequestMapping("/member_join")
	public String member_join() {

		return "member_join";
	}
	
	@RequestMapping("/joinok")
	public String joinok(HttpServletRequest request, HttpSession session) {

		IDao dao =sqlSession.getMapper(IDao.class);
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);
		
		session.setAttribute("memberId", memberId);
		
		return "redirect:index";
	}
	
	@RequestMapping("/loginok")
	public String loginok(HttpServletRequest request, HttpSession session, Model model) {

		IDao dao = sqlSession.getMapper(IDao.class);
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		
		//int checkIdFlag = dao.checkUserId(memberId);
		
//		if(checkIdFlag == 0) {
//			//없는 아이디
//			return "redirect:index";
//		}else {
			int checkIdFlag =dao.checkUserIdAndPw(memberId, memberPw);
			
			if(checkIdFlag == 1) {
				//로그인 성공
				 session.setAttribute("memberId",memberId);
				 session.setAttribute("memberPw", memberPw);
				model.addAttribute("memberId",memberId);
				return "redirect:index";
				
			}else{
				//로그인 실패
				return "redirect:index";
			}
		//}
		
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();
		
		return "redirect:index";
	}
	
	@RequestMapping("/writeOk")
	public String writeOk(HttpServletRequest request, HttpSession session) {

		IDao dao = sqlSession.getMapper(IDao.class);
		
		//로그인 된 경우 화면에 저장된 세션에서 아이디값을 불러온다. 
		//세션에 저장된 값은 어디서든 불러올 수 있다.
		String sessionId = session.getAttribute("memberId").toString();
		String boardName = request.getParameter("rfbname");
		String boardTitle = request.getParameter("rfbtitle");
		String boardContent = request.getParameter("rfbcontent");
		
		dao.rfbwrite(boardName, boardTitle, boardContent, sessionId);
		
		return "redirect:board_list";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String delete = request.getParameter("rfbnum");

		dao.delete(delete);
		
		return "redirect:board_list";
	}
	
	@RequestMapping("/replyOk")
	public String replyOk(HttpServletResponse response,HttpSession session, HttpServletRequest request, Model model) {

		

		String sessionId = session.getAttribute("memberId").toString();
		//댓글이 달린 원글의 번호
		String rrorinum = request.getParameter("rfbnum");
		String rrcontent = request.getParameter("rrcontent");
		
		if(sessionId == null) {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓸 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			IDao dao = sqlSession.getMapper(IDao.class);
			//댓글 쓰기
			dao.rrwrite(rrorinum, sessionId, rrcontent);
			//댓글 총 개수 증가
			dao.rrcount(rrorinum); 
			
			//댓글을 달려는 게시물이 보이게 만듦(DB에서 파일 불러옴)
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			model.addAttribute("rfbView",rfboardDto);
			
			//댓글 내용
			ArrayList<RReplyDto> replyDtos = dao.rrlist(rrorinum);
			model.addAttribute("replylist",replyDtos);
		}
		return "board_view";
		
	}
	
	@RequestMapping("/replyDelete")
	public String replyDelete(HttpServletResponse response, Model model ,HttpServletRequest request, HttpSession session) {
		
		String rrorinum = request.getParameter("rrorinum"); // 댓글이 달린 원글의 번호
		String rrid = request.getParameter("rrid");  // 작성자의 아이디
		String rrnum = request.getParameter("rrnum"); //댓글 고유번호
		String sessionId = session.getAttribute("memberId").toString(); // 로그인한 사람의 아이디
		

		
		if(sessionId.equals(rrid)) {
			
			IDao dao = sqlSession.getMapper(IDao.class);
			
			//댓글 개수 감소
			//dao.rrcountMinus(rrorinum);
			
			//댓글을 달려는 게시물이 보이게 만듦(DB에서 파일 불러옴)
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			dao.rrdelete(rrorinum, rrid, rrnum);
			model.addAttribute("rfbView",rfboardDto);
			
			
			
		} else {
			PrintWriter out;
			
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('본인이 쓴 글이 아니면 삭제할 수 없습니다.');history.go(-1);</script>");
				out.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
		return "board_view";
	}
	
	@RequestMapping("/search_list")
	public String search_list() {
		return "board_list";
	}
}
