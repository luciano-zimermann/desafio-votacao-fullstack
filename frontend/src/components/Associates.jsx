import { useEffect, useState } from 'react'
import { api } from '../api.js'

export default function Associates() {
  const [associates, setAssociates] = useState([])
  const [name, setName] = useState('')
  const [cpf, setCpf] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  function loadAssociates() {
    setLoading(true)
    api
      .getAssociates()
      .then(setAssociates)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false))
  }

  useEffect(loadAssociates, [])

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSubmitting(true)
    try {
      await api.createAssociate({ name, cpf })
      setName('')
      setCpf('')
      loadAssociates()
    } catch (e) {
      setError(e.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="panel">
      <div className="panel-form">
        <h2>Novo associado</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Nome
            <input value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            CPF
            <input
              value={cpf}
              onChange={(e) => setCpf(e.target.value)}
              placeholder="Permite números ou pontuação"
              required
            />
          </label>
          {error && <p className="error-message">{error}</p>}
          <button type="submit" disabled={submitting}>
            {submitting ? 'Cadastrando…' : 'Cadastrar associado'}
          </button>
        </form>
      </div>

      <div className="panel-list">
        <h2>Associados cadastrados</h2>
        {loading && <p className="muted">Carregando…</p>}
        {!loading && associates.length === 0 && <p className="muted">Nenhum associado cadastrado ainda.</p>}
        <ul className="cards">
          {associates.map((associate) => (
            <li key={associate.id} className="card">
              <strong>{associate.name}</strong>
              <p>{associate.cpf}</p>
              <span className="tag">#{associate.id}</span>
            </li>
          ))}
        </ul>
      </div>
    </section>
  )
}
