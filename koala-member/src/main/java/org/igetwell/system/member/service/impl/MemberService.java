package org.igetwell.system.member.service.impl;

import org.igetwell.system.member.entity.Member;
import org.igetwell.system.member.service.IMemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements IMemberService {

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int insert(Member member) {
        return 0;
    }

    @Override
    public Member get(Long id) {
        return null;
    }

    @Override
    public int update(Member member) {
        return 0;
    }
}
