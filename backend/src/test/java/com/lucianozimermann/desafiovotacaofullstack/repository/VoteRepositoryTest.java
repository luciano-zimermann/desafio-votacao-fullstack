package com.lucianozimermann.desafiovotacaofullstack.repository;

import com.lucianozimermann.desafiovotacaofullstack.entity.Agenda;
import com.lucianozimermann.desafiovotacaofullstack.entity.Associate;
import com.lucianozimermann.desafiovotacaofullstack.entity.Session;
import com.lucianozimermann.desafiovotacaofullstack.entity.Vote;
import com.lucianozimermann.desafiovotacaofullstack.enums.VoteValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
@DataJpaTest
class VoteRepositoryTest {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssociateRepository associateRepository;

    @Autowired
    private VoteRepository repository;

    @Test
    @DisplayName("Deve retornar true quando o associado já tiver votado na pauta")
    void shouldReturnTrueWhenAssociateAlreadyVotedOnAgenda() {
        Agenda agenda = agendaRepository.save(buildAgenda());
        Session session = sessionRepository.save(buildSession(agenda));
        Associate associate = associateRepository.save(buildAssociate());

        repository.save(buildVote(session, associate, VoteValue.YES));

        boolean exists = repository.existsByAssociateIdAndSessionAgendaId(associate.getId(), agenda.getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando o associado não tiver votado na pauta")
    void shouldReturnFalseWhenAssociateHasNotVotedOnAgenda() {
        Agenda agenda = agendaRepository.save(buildAgenda());
        Associate associate = associateRepository.save(buildAssociate());

        boolean exists = repository.existsByAssociateIdAndSessionAgendaId(associate.getId(), agenda.getId());

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve contar corretamente os votos Sim e Não de uma pauta")
    void shouldCountYesAndNoVotesForAgenda() {
        Agenda agenda = agendaRepository.save(buildAgenda());
        Session session = sessionRepository.save(buildSession(agenda));

        Associate associate1 = associateRepository.save(buildAssociate("11144477735"));
        Associate associate2 = associateRepository.save(buildAssociate("52998224725"));

        repository.save(buildVote(session, associate1, VoteValue.YES));
        repository.save(buildVote(session, associate2, VoteValue.NO));

        long yesVotes = repository.countBySessionAgendaIdAndVote(agenda.getId(), VoteValue.YES);
        long noVotes = repository.countBySessionAgendaIdAndVote(agenda.getId(), VoteValue.NO);

        Assertions.assertThat(yesVotes).isEqualTo(1L);
        Assertions.assertThat(noVotes).isEqualTo(1L);
    }

    private Agenda buildAgenda() {
        return Agenda.builder()
                     .name("Nova pauta")
                     .description("Descrição da pauta")
                     .build();
    }

    private Session buildSession(Agenda agenda) {
        LocalDateTime now = LocalDateTime.now();

        return Session.builder()
                      .agenda(agenda)
                      .duration(1)
                      .startDate(now)
                      .endDate(now.plusMinutes(1))
                      .build();
    }

    private Associate buildAssociate() {
        return buildAssociate("52998224725");
    }

    private Associate buildAssociate(String cpf) {
        return Associate.builder()
                        .name("Renato Ferreira")
                        .cpf(cpf)
                        .build();
    }

    private Vote buildVote(Session session, Associate associate, VoteValue value) {
        return Vote.builder()
                   .session(session)
                   .associate(associate)
                   .vote(value)
                   .build();
    }
}