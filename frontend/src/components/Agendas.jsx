import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Agendas() {
  const [agendas, setAgendas] = useState([])
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  function loadAgendas() {
    setLoading(true)
    api
      .getAgendas()
      .then(setAgendas)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false))
  }

  useEffect(loadAgendas, [])

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSubmitting(true)
    try {
      await api.createAgenda({ name, description: description || null })
      setName('')
      setDescription('')
      loadAgendas()
    } catch (e) {
      setError(e.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="panel">
      <div className="panel-form">
        <h2>Nova pauta</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Nome
            <input value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            Descrição (opcional)
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={3} />
          </label>
          {error && <p className="error-message">{error}</p>}
          <button type="submit" disabled={submitting}>
            {submitting ? 'Cadastrando…' : 'Cadastrar pauta'}
          </button>
        </form>
      </div>

      <div className="panel-list">
        <h2>Pautas cadastradas</h2>
        {loading && <p className="muted">Carregando…</p>}
        {!loading && agendas.length === 0 && <p className="muted">Nenhuma pauta cadastrada ainda.</p>}
        <ul className="cards">
          {agendas.map((agenda) => (
            <li key={agenda.id} className="card">
              <strong>{agenda.name}</strong>
              {agenda.description && <p>{agenda.description}</p>}
              <span className="tag">#{agenda.id}</span>
            </li>
          ))}
        </ul>
      </div>
    </section>
  )
}
