package com.booqueen.partner.member;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/login.pdo", method = RequestMethod.GET)
	public String test(MemberVO vo, HttpSession session) {
		System.out.println("ȭ�� test");
		return "register";
	}
	
	@RequestMapping(value = "/password.pdo", method = RequestMethod.GET)
	public String testTest(MemberVO vo, HttpSession session) {
		System.out.println("ȭ�� �̵� test");
		return "register";
	}
	
	@RequestMapping(value = "/login.pdo", method = RequestMethod.POST)
	public String confirmEmail(MemberVO vo, HttpSession session) {
		System.out.println("�̸��� ���� ó��");
		MemberVO member = memberService.getMember(vo);
		
		if(member != null) {
			session.setAttribute("email", member.getEmail());
			System.out.println("���Ե� �̸��� ���� �Ϸ�");
			return "password";
		} else {
			System.out.println("�̸��� ���� ����");
			return "register";
			/* ���� ��û ȭ������ ���� */
		}
	}
	
	@RequestMapping(value = "/password.pdo", method = RequestMethod.POST)
	public String comfirmPassword(MemberVO vo, HttpSession session) {
		System.out.println("��й�ȣ ���� ó��");
		MemberVO member = memberService.getPassword(vo);
		
		if(member != null) {
			session.setAttribute("firstName", member.getFirstName());
			System.out.println("�α��� ����");
			return "home";
		} else {
			return "password";
		}
	}

}
