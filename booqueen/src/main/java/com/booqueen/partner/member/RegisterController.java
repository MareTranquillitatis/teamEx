package com.booqueen.partner.member;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/register.pdo", method = RequestMethod.POST)
	public String confirmEmail(MemberVO vo, HttpSession session) {
		System.out.println("���� ���� ����");
		MemberVO member = memberService.getMember(vo);
		
		if(member == null) {
			System.out.println("�Է��� �̸��� ������ ���� ����");
			session.setAttribute("email", vo.getEmail());
			return "contact-details";
		} else {
			System.out.println("�̹� �ش� �̸��� �ּҷ� ��ϵ� ������ �ֽ��ϴ�.");
			return "login";
		}
	}
	
	@RequestMapping(value = "details.pdo", method = RequestMethod.POST)
	public String addDetails(MemberVO vo, HttpSession session) {
		System.out.println("�������� �Է�");
		session.setAttribute("lastName", vo.getLastName());
		session.setAttribute("firstName", vo.getFirstName());
		session.setAttribute("telephone", vo.getTelephone());
		System.out.println(session.getAttribute("email"));
		System.out.println(session.getAttribute("lastName"));
		System.out.println(session.getAttribute("firstName"));
		System.out.println(session.getAttribute("telephone"));
		return "set-password";
	}
	
	@RequestMapping(value = "/set-password.pdo", method= RequestMethod.POST)
	public String setPassword(MemberVO vo, HttpSession session) {
		System.out.println("��й�ȣ �Է�");
		session.setAttribute("password", vo.getPassword());
		System.out.println(session.getAttribute("password"));
		
		/* *********************************** */
		memberService.addMember(vo);
		System.out.println("����?");
		return "sample";
	}
}
