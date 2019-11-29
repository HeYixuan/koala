package org.igetwell.system.member.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.member.entity.Member;
import org.igetwell.system.member.mapper.MemberMapper;
import org.igetwell.system.member.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MemberService implements IMemberService {

    @Resource
    private MemberMapper memberMapper;

    @Override
    public void deleteById(Long id) {
        memberMapper.deleteById(id);
    }

    @Override
    public ResponseEntity insert(Member member) {
        if (checkObject(member)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "参数不能为空");
        }
        if (checkParam(member.getMerchantId()) || checkParam(member.getMerchantNo())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商户编号不能为空");
        }
        if (checkParam(member.getMemberName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员名称不能为空");
        }
        if (checkParam(member.getMemberNickName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "昵称不能为空");
        }
        if (checkParam(member.getMobile())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "手机号码不能为空");
        }
        if (checkParam(member.getMemberNickName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "手机号码不能为空");
        }
        int i = memberMapper.insert(member);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public Member get(Long id) {
        return memberMapper.get(id);
    }

    @Override
    public Member getMemberNo(String memberNo) {
        return memberMapper.getMemberNo(memberNo);
    }


    @Override
    public ResponseEntity update(Member member) {
        if (checkObject(member) || checkParam(member.getId()) || checkParam(member.getMemberNo())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员编号不能为空");
        }
        if (checkParam(member.getMerchantId()) || checkParam(member.getMerchantNo())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "商户编号不能为空");
        }
        if (checkParam(member.getMemberName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员名称不能为空");
        }
        if (checkParam(member.getMemberNickName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "昵称不能为空");
        }
        if (checkParam(member.getMobile())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "手机号码不能为空");
        }
        if (checkParam(member.getMemberNickName())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "手机号码不能为空");
        }
        int i = memberMapper.update(member);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkObject(Object o) {
        if (o == null) {
            return false;
        }
        return true;
    }

    private boolean checkParam(Object o) {
        if (StringUtils.isEmpty(o)) {
            return false;
        }
        return true;
    }
}
