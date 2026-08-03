import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Vote() {
  const [agendas, setAgendas] = useState([])
  const [sessions, setSessions] = useState([])
  const [associates, setAssociates] = useState([])
  const [sessionId, setSessionId] = useState('')
  const [associateId, setAssociateId] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  function loadData() {
    setLoading(true)
    Promise.all([api.getAgendas(), api.getSessions(), api.getAssociates()])
      .then(([agendasData, sessionsData, associatesData]) => {
        setAgendas(agendasData)
        setSessions(sessionsData)
        setAssociates(associatesData)
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false))
  }

  useEffect(loadData, [])

  function agendaName(id) {
    return agendas.find((a) => a.id === id)?.name || `Pauta #${id}`
  }

  const openSessions = sessions.filter((s) => s.status === 'OPEN')

  async function submitVote(vote) {
    setError(null)
    setSuccess(null)
    setSubmitting(true)
    try {
      await api.registerVote({
        sessionId: Number(sessionId),
        associateId: Number(associateId),
        vote,
      })
      setSuccess(`Voto "${vote === 'YES' ? 'Sim' : 'Não'}" registrado com sucesso!`)
    } catch (e) {
      setError(e.message)
    } finally {
      setSubmitting(false)
    }
  }

  const canVote = sessionId && associateId && !submitting

  return (
    <section className="panel panel-single">
      <div className="panel-form">
        <h2>Registrar voto</h2>

        {loading && <p className="muted">Carregando…</p>}

        {!loading && openSessions.length === 0 && (
          <p className="muted">Nenhuma sessão aberta no momento. Abra uma sessão na aba "Sessões".</p>
        )}

        {!loading && openSessions.length > 0 && (
          <>
            <label>
              Sessão aberta
              <select value={sessionId} onChange={(e) => setSessionId(e.target.value)}>
                <option value="" disabled>
                  Selecione uma sessão
                </option>
                {openSessions.map((session) => (
                  <option key={session.id} value={session.id}>
                    {agendaName(session.agendaId)} — encerra em{' '}
                    {new Date(session.endDate).toLocaleTimeString('pt-BR')}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Associado
              <select value={associateId} onChange={(e) => setAssociateId(e.target.value)}>
                <option value="" disabled>
                  Selecione um associado
                </option>
                {associates.map((associate) => (
                  <option key={associate.id} value={associate.id}>
                    {associate.name}
                  </option>
                ))}
              </select>
            </label>

            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}

            <div className="vote-buttons">
              <button className="vote-yes" disabled={!canVote} onClick={() => submitVote('YES')}>
                Sim
              </button>
              <button className="vote-no" disabled={!canVote} onClick={() => submitVote('NO')}>
                Não
              </button>
            </div>
          </>
        )}
      </div>
    </section>
  )
}
