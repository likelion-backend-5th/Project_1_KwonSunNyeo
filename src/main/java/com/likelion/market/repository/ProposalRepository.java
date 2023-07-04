package com.likelion.market.repository;

import com.likelion.market.entity.ProposalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {
    Page<ProposalEntity> findAllByItemId(Long id, Pageable pageable);
}
