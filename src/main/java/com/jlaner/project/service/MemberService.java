package com.jlaner.project.service;

import com.jlaner.project.domain.Member;
import com.jlaner.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findByMemberId(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElseThrow(() -> new IllegalArgumentException("Unexpected member"));

    }
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
