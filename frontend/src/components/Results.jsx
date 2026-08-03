import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Results() {
  const [agendas, setAgendas] = useState([])
  const [agendaId, setAgendaId] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    api.getAgendas().then(setAgendas).catch((e) => setError(e.message))
  }, [])

  async function handleConsult(e) {
    e.preventDefault()
    setError(null)
    setResult(null)
    setLoading(true)
    try {
      const data = await api.getResult(Number(agendaId))
      setResult(data)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="panel panel-single">
      <div className="panel-form">
        <h2>Resultado da votação</h2>
        <form onSubmit={handleConsult}>
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
          {error && <p className="error-message">{error}</p>}
          <button type="submit" disabled={loading}>
            {loading ? 'Consultando…' : 'Consultar resultado da Pauta'}
          </button>
        </form>

        {result && (
          <div className="result-box">
            <span className={`result-outcome ${result.result === 'Aprovada' ? 'outcome-approved' : 'outcome-rejected'}`}>
              {result.result}
            </span>
            <div className="result-grid">
              <div>
                <strong>{result.totalVotes}</strong>
                <span>Total de votos</span>
              </div>
              <div>
                <strong>{result.yesVotes}</strong>
                <span>Sim</span>
              </div>
              <div>
                <strong>{result.noVotes}</strong>
                <span>Não</span>
              </div>
            </div>
            <p className="muted">
              Sessão: {result.sessionStatus === 'OPEN' ? 'ainda aberta' : result.sessionStatus === 'CLOSED' ? 'encerrada' : 'não iniciada'}
            </p>
          </div>
        )}
      </div>
    </section>
  )
}
