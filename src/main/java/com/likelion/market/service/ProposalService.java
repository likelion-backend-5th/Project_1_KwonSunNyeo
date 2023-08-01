package com.likelion.market.service;

import com.likelion.market.dto.ProposalDto;
import com.likelion.market.dto.ProposalPageDto;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import com.likelion.market.entity.UserEntity;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;

    private static final String NOT_FOUND_ITEM = "해당 물품을 찾을 수 없습니다.";
    private static final String NOT_FOUND_USER = "해당 사용자를 찾을 수 없습니다.";
    private static final String NOT_FOUND_PROPOSAL = "해당 제안을 찾을 수 없습니다.";
    private static final String UNAUTHORIZED_USER = "구매를 제안한 작성자의 정보가 일치하지 않습니다.";
    private static final String NOT_ACCEPTED_STATUS = "제안이 수락 상태가 아니면 확정할 수 없습니다.";

    // 구매 제안 - 등록
    public ProposalDto createProposal(Long itemId, ProposalDto dto) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ITEM));
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
        ProposalEntity newProposal = new ProposalEntity();
        newProposal.setItem(item);
        newProposal.setUser(user);
        newProposal.setSuggestedPrice(dto.getSuggestedPrice());
        newProposal.setStatus(ProposalStatus.PROPOSED);
        proposalRepository.save(newProposal);
        return ProposalDto.fromEntity(newProposal);
    }

    // 구매 제안 - 페이지 단위 조회
    public Page<ProposalPageDto> readProposalPaged(Long itemId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<ProposalEntity> proposalEntityPage = proposalRepository.findByItemId(itemId, pageable);
        return proposalEntityPage.map(ProposalPageDto::fromEntity);
    }

    // 구매 제안 - 수정 - 가격
    public ProposalDto updateProposalPrice(Long proposalId, Long userId, Integer suggestedPrice) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_PROPOSAL));
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        proposal.updatePrice(suggestedPrice);
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 수정 - 상태
    public ProposalDto updateProposalStatus(Long proposalId, Long userId, ProposalStatus status) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_PROPOSAL));
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        proposal.updateStatus(status);
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 물품 정보, 구매 제안 - 수정 - 상태
    public ProposalDto confirmProposal(Long itemId, Long proposalId, Long userId) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_PROPOSAL));
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        if (proposal.getStatus() != ProposalStatus.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NOT_ACCEPTED_STATUS);
        }
        proposal.setStatus(ProposalStatus.CONFIRMED);
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 삭제
    public void deleteProposal(Long proposalId, Long userId) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_PROPOSAL));
        if (!proposal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        proposalRepository.deleteById(proposalId);
    }
}
