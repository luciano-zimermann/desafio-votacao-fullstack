package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByAgendaIdAndEndDateAfter(Long agendaId, LocalDateTime dateTime);
}