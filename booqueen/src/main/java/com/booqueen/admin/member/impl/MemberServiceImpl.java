package com.booqueen.admin.member.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booqueen.admin.member.MemberService;
import com.booqueen.admin.member.MemberVO;

@Service("MemberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;

	@Override
	public MemberVO getAdminMember(MemberVO vo) {
		return memberDAO.getAdminMember(vo);
	}

	
}