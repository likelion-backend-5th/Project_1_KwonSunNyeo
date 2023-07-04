package com.likelion.market.service;

import com.likelion.market.dto.ProposalDto;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.repository.ProposalRepository;
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

    // 구매 제안 - 등록
    public ProposalDto createProposal(Long itemId, ProposalDto dto) {
        if (!proposalRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ProposalEntity newProposal = new ProposalEntity();
        newProposal.setItemId(itemId);
        newProposal.setWriter(dto.getWriter());
        newProposal.setPassword(dto.getPassword());
        newProposal.setSuggestedPrice(dto.getSuggestedPrice());
        newProposal.setStatus(ProposalStatus.PROPOSED);
        proposalRepository.save(newProposal);
        return ProposalDto.fromEntity(newProposal);
    }

    // 구매 제안 - 페이지 단위 조회
    public Page<ProposalDto> readProposalPaged(
            Long itemId, Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id").descending()
        );
        Page<ProposalEntity> proposalEntityPage = proposalRepository.findAllByItemId(itemId, pageable);
        Page<ProposalDto> proposalDtoPage = proposalEntityPage.map(ProposalDto::fromEntity);
        return proposalDtoPage;
    }

    // 구매 제안 - 전체 조회 -> 페이지 단위 조회와 중복이므로 주석 처리
//    public List<ProposalDto> readProposalAll(Long itemId) {
//        List<ProposalEntity> proposalEntities
//                = proposalRepository.findAllByItemId(itemId);
//        List<ProposalDto> proposalList = new ArrayList<>();
//        for (ProposalEntity entity: proposalEntities) {
//            proposalList.add(ProposalDto.fromEntity(entity));
//        }
//        return proposalList;
//    }

    // 구매 제안 - 수정
    public ProposalDto updateProposal(Long itemId, Long proposalId, ProposalDto dto) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!itemId.equals(proposal.getItemId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (proposal.getWriter().equals(dto.getWriter()) && proposal.getPassword().equals(dto.getPassword())) {
            // 제안을 등록한 사람만 가격 수정 가능
            if (dto.getSuggestedPrice() != 0)
                proposal.setSuggestedPrice(dto.getSuggestedPrice());
        }
        if (item.getWriter().equals(dto.getWriter()) && item.getPassword().equals(dto.getPassword())) {
            // 물품을 등록한 사람만 상태 변경 가능
            if (dto.getStatus() != null)
                proposal.setStatus(dto.getStatus());
        }
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 삭제
    public void deleteProposal(Long itemId, Long proposalId, ProposalDto dto) {
        Optional<ProposalEntity> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ProposalEntity proposal = optionalProposal.get();
        if (!itemId.equals(proposal.getItemId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!proposal.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        if (!proposal.getWriter().equals(dto.getWriter())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
            );
        }
        proposalRepository.deleteById(proposalId);
    }
}
