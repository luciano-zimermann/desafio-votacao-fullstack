import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Sessions() {
  const [agendas, setAgendas] = useState([])
  const [sessions, setSessions] = useState([])
  const [agendaId, setAgendaId] = useState('')
  const [duration, setDuration] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  function loadData() {
    setLoading(true)
    Promise.all([api.getAgendas(), api.getSessions()])
      .then(([agendasData, sessionsData]) => {
        setAgendas(agendasData)
        setSessions(sessionsData)
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false))
  }

  useEffect(loadData, [])

  function agendaName(id) {
    return agendas.find((a) => a.id === id)?.name || `Pauta #${id}`
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSubmitting(true)
    try {
      await api.openSession({
        agendaId: Number(agendaId),
        duration: duration ? Number(duration) : null,
      })
      setAgendaId('')
      setDuration('')
      loadData()
    } catch (e) {
      setError(e.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="panel">
      <div className="panel-form">
        <h2>Abrir sessão de votação</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Pauta
            <select value={agendaId} onChange={(e) => setAgendaId(e.target.value)} required>
              <option value="" disabled>
                Selecione uma pauta
              </option>
              {agendas.map((agenda) => (
                <option key={agenda.id} value={agenda.id}>
                  {agenda.name}
                </option>
              ))}
            </select>
          </label>
          <label>
            Duração em minutos (opcional, padrão 1 min)
            <input
              type="number"
              min="1"
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
            />
          </label>
          {error && <p className="error-message">{error}</p>}
          <button type="submit" disabled={submitting || agendas.length === 0}>
            {submitting ? 'Abrindo…' : 'Abrir sessão'}
          </button>
          {agendas.length === 0 && !loading && (
            <p className="muted">Cadastre uma pauta antes de abrir uma sessão.</p>
          )}
        </form>
      </div>

      <div className="panel-list">
        <h2>Sessões</h2>
        {loading && <p className="muted">Carregando…</p>}
        {!loading && sessions.length === 0 && <p className="muted">Nenhuma sessão aberta ainda.</p>}
        <ul className="cards">
          {sessions.map((session) => (
            <li key={session.id} className="card">
              <strong>{agendaName(session.agendaId)}</strong>
              <p>
                Duração: {session.duration} min · Encerra em{' '}
                {new Date(session.endDate).toLocaleString('pt-BR')}
              </p>
              <span className={`badge ${session.status === 'OPEN' ? 'badge-open' : 'badge-closed'}`}>
                {session.status === 'OPEN' ? 'Aberta' : 'Encerrada'}
              </span>
            </li>
          ))}
        </ul>
      </div>
    </section>
  )
}
