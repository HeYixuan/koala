package org.igetwell.system.member.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.entity.Member;

public interface IMemberService {

    void deleteById(Long id);

    ResponseEntity insert(Member member);

    Member get(Long id);

    Member getMemberNo(String memberNo);

    ResponseEntity update(Member member);
}
