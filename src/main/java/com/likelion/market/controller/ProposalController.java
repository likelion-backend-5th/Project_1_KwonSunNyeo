package com.likelion.market.controller;

import com.likelion.market.dto.ProposalDto;
import com.likelion.market.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class ProposalController {
    private final ProposalService service;

    // 구매 제안 - 등록
    // POST /items/{itemId}/proposals
    @PostMapping
    public ProposalDto create(
            @PathVariable("itemId") Long itemId,
            @RequestBody ProposalDto dto
    ) {
        return service.createProposal(itemId, dto);
    }

    // 구매 제안 - 페이지 단위 조회
    // GET /items/{itemId}/proposals
    @GetMapping
    public Page<ProposalDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.readProposalPaged(itemId, page, size);
    }

    // 구매 제안 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    // GET /items/{itemId}/proposals
//    @GetMapping
//    public List<ProposalDto> readAll(
//            @PathVariable("itemId") Long itemId
//    ) {
//        return service.readProposalAll(itemId);
//    }

    // 구매 제안 - 수정
    // PUT /items/{itemId}/proposals/{proposalId}
    @PutMapping("/{proposalId}")
    public ProposalDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        return service.updateProposal(itemId, proposalId, dto);
    }

    // 구매 제안 - 삭제
    // DELETE /items/{itemId}/proposals/{proposalId}
    @DeleteMapping("/{proposalId}")
    public void delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        service.deleteProposal(itemId, proposalId, dto);
    }
}
