import { useState } from 'react'
import Agendas from './components/Agendas.jsx'
import Associates from './components/Associates.jsx'
import Sessions from './components/Sessions.jsx'
import Vote from './components/Vote.jsx'
import Results from './components/Results.jsx'

const TABS = [
  { id: 'agendas', label: 'Pautas' },
  { id: 'associates', label: 'Associados' },
  { id: 'sessions', label: 'Sessões' },
  { id: 'vote', label: 'Votar' },
  { id: 'results', label: 'Resultado' },
]

export default function App() {
  const [activeTab, setActiveTab] = useState('agendas')

  return (
    <div className="app">
      <header className="app-header">
        <h1>Votação</h1>
        <p>Assembleias da cooperativa</p>
      </header>

      <nav className="tabs">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            className={`tab ${activeTab === tab.id ? 'tab-active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>

      <main className="content">
        {activeTab === 'agendas' && <Agendas />}
        {activeTab === 'associates' && <Associates />}
        {activeTab === 'sessions' && <Sessions />}
        {activeTab === 'vote' && <Vote />}
        {activeTab === 'results' && <Results />}
      </main>
    </div>
  )
}
