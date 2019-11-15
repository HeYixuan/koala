package org.igetwell.system.member.service.impl;

import org.igetwell.system.member.entity.Member;
import org.igetwell.system.member.mapper.MemberMapper;
import org.igetwell.system.member.service.IMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberService implements IMemberService {

    /*@Resource
    private MemberMapper memberMapper;*/

    @Override
    public int deleteById(Long id) {
        /*return memberMapper.deleteById(id);*/
        return 0;
    }

    @Override
    public int insert(Member member) {
        /*member.setId(1L);
        return memberMapper.insert(member);*/
        return 0;
    }

    @Override
    public Member get(Long id) {
//        return memberMapper.get(id);
        return null;
    }

    @Override
    public int update(Member member) {
        /*return memberMapper.update(member);*/
        return 0;
    }
}
