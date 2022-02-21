package com.booqueen.partner.member.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booqueen.partner.member.MemberService;
import com.booqueen.partner.member.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;

	public void setMemberDAO(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}

	@Override
	public MemberVO getMember(MemberVO vo) {
		System.out.println("�޾ƿ� �̸��ϰ�: " + vo.getEmail());
		return memberDAO.getMember(vo);
	}

	@Override
	public MemberVO getPassword(MemberVO vo) {
		System.out.println("�޾ƿ� �̸��ϰ�: " + vo.getEmail());
		System.out.println("�޾ƿ� ��й�ȣ��: " + vo.getPassword());
		return memberDAO.getPassword(vo);
	}

	@Override
	public void addMember(MemberVO vo) {
		System.out.println("�޾ƿ� �̸��ϰ�: " + vo.getEmail());
		System.out.println("�޾ƿ� ��й�ȣ��: " + vo.getPassword());
		System.out.println("�޾ƿ� ��: " + vo.getLastName());
		System.out.println("�޾ƿ� �̸�: " + vo.getFirstName());
		System.out.println("�޾ƿ� ��ȭ��ȣ: " + vo.getTelephone());
		memberDAO.register(vo);
	}

}
