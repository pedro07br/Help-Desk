import { useState } from 'react';
import ChamadoForm from './components/ChamadoForm';
import ChamadoLista from './components/ChamadoLista';
import './App.css';

export default function App() {
  const [refresh, setRefresh] = useState(0);

  const handleCriado = () => setRefresh((r) => r + 1);

  return (
    <div className="app">
      <header className="header">
        <div className="header-inner">
          <div className="logo">
            <span className="logo-icon">HD</span>
            <span className="logo-text">Help<strong>Desk</strong></span>
          </div>
          <span className="header-sub">Sistema de chamados com Kafka</span>
        </div>
      </header>

      <main className="main">
        <div className="layout">
          <aside className="sidebar">
            <ChamadoForm onCriado={handleCriado} />
          </aside>
          <section className="content">
            <ChamadoLista refresh={refresh} />
          </section>
        </div>
      </main>
    </div>
  );
}
