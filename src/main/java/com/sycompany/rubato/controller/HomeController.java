package com.sycompany.rubato.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sycompany.rubato.dao.IDao;

@Controller
public class HomeController {

	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/")
	public String HomeIndex() {
		return "redirect:index";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/board_list")
	public String board_list() {

		return "board_list";
	}
	
	@RequestMapping("/board_view")
	public String board_view() {

		return "board_view";
	}
	
	@RequestMapping("/board_write")
	public String board_write() {

		return "board_write";
	}
	
	@RequestMapping("/member_join")
	public String member_join() {

		return "member_join";
	}
	
	@RequestMapping("/joinok")
	public String joinok(HttpServletRequest request) {

		IDao dao =sqlSession.getMapper(IDao.class);
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);
		
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
		
		//String rfbuserid = session.getAttribute(sessionId).toString();
		//String rfbuserid = request.getParameter("rfbuserid");
		String rfbname = request.getParameter("rfbname");
		String rfbtitle = request.getParameter("rfbtitle");
		String rfbcontent = request.getParameter("rfbcontent");
		
		//dao.rfbwrite(rfbname, rfbtitle, rfbcontent, rfbuserid);
		
		return "redirect:index";
	}
}
