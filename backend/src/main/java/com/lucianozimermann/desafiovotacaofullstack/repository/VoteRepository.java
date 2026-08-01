package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Vote;
import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByAssociateIdAndSessionAgendaId(Long associateId, Long agendaId);

    long countBySessionAgendaIdAndVote(Long agendaId, VoteValue vote);
}