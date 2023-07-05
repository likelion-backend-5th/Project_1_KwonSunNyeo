package com.likelion.market.controller;

import com.likelion.market.dto.MessageResponseDto;
import com.likelion.market.dto.ProposalDto;
import com.likelion.market.dto.ProposalPageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto> create(
            @PathVariable("itemId") Long itemId,
            @RequestBody ProposalDto dto
    ) {
        service.createProposal(itemId, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("구매 제안이 등록되었습니다.");
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구매 제안 - 페이지 단위 조회
    // GET /items/{itemId}/proposals?writer={writer}&password={password}&{page}={page}
    @GetMapping
    public Page<ProposalPageDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "writer", defaultValue = "") String writer,
            @RequestParam(name = "password", defaultValue = "") String password,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "25") Integer size
    ) {
        return service.readProposalPaged(itemId, writer, password, page, size);
    }

    // 구매 제안 - 수정 - 가격
    // PUT /items/{itemId}/proposals/{proposalId}/price
    @PutMapping("/{proposalId}/price")
    public ResponseEntity<MessageResponseDto> update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        service.updateProposalPrice(itemId, proposalId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("제안이 수정되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구매 제안 - 수정 - 상태
    // PUT /items/{itemId}/proposals/{proposalId}/status
    @PutMapping("/{proposalId}/status")
    public ResponseEntity<MessageResponseDto> updateStatus(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        service.updateProposalStatus(itemId, proposalId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("제안의 상태가 변경되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 물품 정보, 구매 제안 - 수정 - 상태
    // PUT /items/{itemId}/proposals/{proposalId}/confirm
    @PutMapping("/{proposalId}/confirm")
    public ResponseEntity<MessageResponseDto> confirmProposal(
            @PathVariable Long itemId,
            @PathVariable Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        service.confirmProposal(itemId, proposalId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("구매가 확정되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구매 제안 - 삭제
    // DELETE /items/{itemId}/proposals/{proposalId}
    @DeleteMapping("/{proposalId}")
    public ResponseEntity<MessageResponseDto> delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        service.deleteProposal(itemId, proposalId, dto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("제안을 삭제했습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
