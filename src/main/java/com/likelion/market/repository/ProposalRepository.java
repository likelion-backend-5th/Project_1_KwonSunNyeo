package com.likelion.market.repository;

import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {
    Page<ProposalEntity> findAllByItemId(Long id, Pageable pageable);
    List<ProposalEntity> findAllByItemIdAndStatus(Long itemId, ProposalStatus status);
}
