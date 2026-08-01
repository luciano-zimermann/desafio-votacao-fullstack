package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociateRepository extends JpaRepository<Associate, Long> {

    boolean existsByCpf(String cpf);
}