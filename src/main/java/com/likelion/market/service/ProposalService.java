package com.likelion.market.service;

import com.likelion.market.dto.ProposalDto;
import com.likelion.market.dto.ProposalPageDto;
import com.likelion.market.entity.*;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.repository.ProposalRepository;
import com.likelion.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;

    // 구매 제안 - 등록
    public ProposalDto createProposal(Long itemId, ProposalDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "물품을 찾을 수 없습니다."));
        ProposalEntity newProposal = new ProposalEntity();
        newProposal.setItem(item);
        newProposal.setUser(user);
        newProposal.setSuggestedPrice(dto.getSuggestedPrice());
        newProposal.setStatus(ProposalStatus.PROPOSED);
        proposalRepository.save(newProposal);
        return ProposalDto.fromEntity(newProposal);
    }

    // 구매 제안 - 페이지 단위 조회
    public Page<ProposalPageDto> readProposalPaged(
            Long itemId, Integer page, Integer limit
    ) {
        Pageable pageable = PageRequest.of(
                page, limit, Sort.by("id").descending()
        );
        Page<ProposalEntity> proposalEntityPage = proposalRepository.findByItemId(itemId, pageable);
        Page<ProposalPageDto> proposalPageDtoPage = proposalEntityPage.map(ProposalPageDto::fromEntity);
        return proposalPageDtoPage;
    }

    // 구매 제안 - 수정 - 가격
    public ProposalDto updateProposalPrice(Long proposalId, ProposalDto dto) {
        Optional<ProposalEntity> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ProposalEntity proposal = optionalProposal.get();
        if (!proposal.getUser().getId().equals(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "제안을 등록한 작성자가 일치하지 않습니다.");

        }
        proposal.setSuggestedPrice(dto.getSuggestedPrice());
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 수정 - 상태
    public ProposalDto updateProposalStatus(Long proposalId, ProposalDto dto) {
        Optional<ProposalEntity> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ProposalEntity proposal = optionalProposal.get();
        if (!proposal.getItem().getUser().getId().equals(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물품을 등록한 작성자가 일치하지 않습니다.");
        }
        proposal.setStatus(dto.getStatus());
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 물품 정보, 구매 제안 - 수정 - 상태
    public ProposalDto confirmProposal(Long itemId, Long proposalId, Long userId) {
        Optional<ProposalEntity> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "제안을 찾을 수 없습니다.");
        }
        ProposalEntity proposal = optionalProposal.get();
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "구매를 제안한 작성자가 일치하지 않습니다.");
        }
        if (proposal.getStatus() != ProposalStatus.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제안이 수락 상태가 아니면 확정할 수 없습니다.");
        }
        proposal.setStatus(ProposalStatus.CONFIRMED);
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 삭제
    public void deleteProposal(Long proposalId, Long userId) {
        Optional<ProposalEntity> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "제안을 찾을 수 없습니다.");
        }
        ProposalEntity proposal = optionalProposal.get();
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "구매를 제안한 작성자가 일치하지 않습니다.");
        }
        proposalRepository.deleteById(proposalId);
    }
}
