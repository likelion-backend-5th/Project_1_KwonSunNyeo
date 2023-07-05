package com.likelion.market.service;

import com.likelion.market.dto.ProposalDto;
import com.likelion.market.dto.ProposalPageDto;
import com.likelion.market.entity.ItemEntity;
import com.likelion.market.entity.ItemStatus;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

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
    public Page<ProposalPageDto> readProposalPaged(
            Long itemId, String writer, String password, Integer pageNumber, Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id").descending()
        );
        Page<ProposalEntity> proposalEntityPage = proposalRepository.findByItemIdAndWriterAndPassword(itemId, writer, password, pageable);
        Page<ProposalPageDto> proposalPageDtoPage = proposalEntityPage.map(ProposalPageDto::fromEntity);
        return proposalPageDtoPage;
    }

    // 구매 제안 - 수정 - 가격
    public ProposalDto updateProposalPrice(Long itemId, Long proposalId, ProposalDto dto) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!proposal.getWriter().equals(dto.getWriter())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
            );
        }
        if (!proposal.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        proposal.setSuggestedPrice(dto.getSuggestedPrice());
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 구매 제안 - 수정 - 상태
    public ProposalDto updateProposalStatus(Long itemId, Long proposalId, ProposalDto dto) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!item.getWriter().equals(dto.getWriter())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
            );
        }
        if (!item.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        proposal.setStatus(dto.getStatus());
        proposalRepository.save(proposal);
        return ProposalDto.fromEntity(proposal);
    }

    // 물품 정보, 구매 제안 - 수정 - 상태
    public ProposalDto confirmProposal(Long itemId, Long proposalId, ProposalDto dto) {
        ProposalEntity proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!proposal.getWriter().equals(dto.getWriter())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."
            );
        }
        if (!proposal.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."
            );
        }
        if (proposal.getStatus() != ProposalStatus.ACCEPTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "제안이 수락 상태가 아니면 확정할 수 없습니다."
            );
        }
        proposal.setStatus(ProposalStatus.CONFIRMED);
        proposalRepository.save(proposal);
        itemService.updateItemStatus(itemId, ItemStatus.SOLD_OUT);
        List<ProposalEntity> proposals = proposalRepository.findAllByItemIdAndStatus(itemId, ProposalStatus.PROPOSED);
        for (ProposalEntity otherProposal : proposals) {
            otherProposal.setStatus(ProposalStatus.REJECTED);
            proposalRepository.save(otherProposal);
        }
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
