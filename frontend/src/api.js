const API_BASE = 'http://localhost:8080/api/v1'

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  const isJson = response.headers.get('content-type')?.includes('application/json')
  const body = isJson ? await response.json() : null

  if (!response.ok) {
    const message = body?.error || `Erro ${response.status}`
    const details = body?.errors?.join(', ')

    throw new Error(details ? `${message}: ${details}` : message)
  }

  return body
}

export const api = {
  getAgendas: () => apiRequest('/agendas'),
  createAgenda: (data) =>
    apiRequest('/agendas', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getAssociates: () => apiRequest('/associates'),
  createAssociate: (data) =>
    apiRequest('/associates', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getSessions: () => apiRequest('/sessions'),
  openSession: (data) =>
    apiRequest('/sessions', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  registerVote: (data) =>
    apiRequest('/votes', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getResult: (agendaId) => apiRequest(`/votes/results/${agendaId}`),
}