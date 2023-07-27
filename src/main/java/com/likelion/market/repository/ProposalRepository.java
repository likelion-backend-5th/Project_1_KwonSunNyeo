package com.likelion.market.repository;

import com.likelion.market.entity.ProposalEntity;
import com.likelion.market.entity.ProposalStatus;
import com.likelion.market.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<ProposalEntity, Long> {
    Page<ProposalEntity> findByItemIdAndUserAndPassword(Long itemId, String writer, String password, Pageable pageable);
    List<ProposalEntity> findAllByItemIdAndStatus(Long itemId, ProposalStatus status);
    Optional<ProposalEntity> findByIdAndUser(Long id, UserEntity user);
}
