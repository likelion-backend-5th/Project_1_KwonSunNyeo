package com.likelion.market.controller;

import com.likelion.market.dto.MessageResponseDto;
import com.likelion.market.dto.ProposalDto;
import com.likelion.market.dto.ProposalPageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.entity.UserEntity;
import com.likelion.market.repository.UserRepository;
import com.likelion.market.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class ProposalController {
    private final ProposalService service;
    private final UserRepository userRepository;

    // 구매 제안 - 등록
    // POST /items/{itemId}/proposals
    @PostMapping
    public ResponseEntity<ResponseDto> create(
            @PathVariable("itemId") Long itemId,
            @RequestBody ProposalDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                service.createProposal(itemId, dto);
                ResponseDto response = new ResponseDto();
                response.setMessage("구매 제안이 등록되었습니다.");
                response.setStatus(200);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        response.setStatus(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 구매 제안 - 페이지 단위 조회
    // GET /items/{itemId}/proposals
    @GetMapping
    public Page<ProposalPageDto> readAll(
            @PathVariable("itemId") Long itemId,
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return service.readProposalPaged(itemId, pageable.getPageNumber(), pageable.getPageSize());
    }

    // 구매 제안 - 수정 - 가격
    // PUT /items/{itemId}/proposals/{proposalId}/price
    @PutMapping("/{proposalId}/price")
    public ResponseEntity<MessageResponseDto> update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                dto.setId(proposalId);
                service.updateProposalPrice(proposalId, dto);
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("제안이 수정되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 구매 제안 - 수정 - 상태
    // PUT /items/{itemId}/proposals/{proposalId}/status
    @PutMapping("/{proposalId}/status")
    public ResponseEntity<MessageResponseDto> updateStatus(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                dto.setUserId(user.getId());
                dto.setItemId(itemId);
                dto.setId(proposalId);
                service.updateProposalStatus(proposalId, dto);
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("제안의 상태가 변경되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 물품 정보, 구매 제안 - 수정 - 상태
    // PUT /items/{itemId}/proposals/{proposalId}/confirm
    @PutMapping("/{proposalId}/confirm")
    public ResponseEntity<MessageResponseDto> confirmProposal(
            @PathVariable Long itemId,
            @PathVariable Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                service.confirmProposal(itemId, proposalId, user.getId());
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("구매가 확정되었습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 구매 제안 - 삭제
    // DELETE /items/{itemId}/proposals/{proposalId}
    @DeleteMapping("/{proposalId}")
    public ResponseEntity<MessageResponseDto> delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("proposalId") Long proposalId,
            @RequestBody ProposalDto dto
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<UserEntity> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                service.deleteProposal(proposalId, user.getId());
                MessageResponseDto response = new MessageResponseDto();
                response.setMessage("제안을 삭제했습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("작성자 정보를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
