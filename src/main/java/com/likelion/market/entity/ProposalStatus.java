package com.likelion.market.entity;

// 제안의 상태를 제한된 값만 가질 수 있도록 열거형 사용
public enum ProposalStatus {
    PROPOSED, // 제안
    ACCEPTED, // 수락
    REJECTED, // 거절
    CONFIRMED // 확정
}
