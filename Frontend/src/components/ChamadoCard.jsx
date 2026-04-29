import { useState } from 'react';
import { chamadoService } from '../services/api';

const STATUS_LABEL = {
  ABERTO: 'Aberto',
  EM_ANDAMENTO: 'Em andamento',
  FECHADO: 'Fechado',
};

const PROXIMOS_STATUS = {
  ABERTO: ['EM_ANDAMENTO', 'FECHADO'],
  EM_ANDAMENTO: ['FECHADO'],
  FECHADO: [],
};

export default function ChamadoCard({ chamado, onAtualizado }) {
  const [loading, setLoading] = useState(false);

  const atualizarStatus = async (novoStatus) => {
    try {
      setLoading(true);
      await chamadoService.atualizarStatus(chamado.id, novoStatus);
      onAtualizado();
    } finally {
      setLoading(false);
    }
  };

  const dataFormatada = new Date(chamado.dataCriacao).toLocaleString('pt-BR');

  return (
    <div className={`card card--${chamado.status.toLowerCase()}`}>
      <div className="card-header">
        <span className={`badge badge--${chamado.status.toLowerCase()}`}>
          {STATUS_LABEL[chamado.status]}
        </span>
        <span className="card-id">#{chamado.id}</span>
      </div>

      <h3 className="card-titulo">{chamado.titulo}</h3>
      {chamado.descricao && (
        <p className="card-descricao">{chamado.descricao}</p>
      )}
      <p className="card-data">{dataFormatada}</p>

      {PROXIMOS_STATUS[chamado.status].length > 0 && (
        <div className="card-acoes">
          {PROXIMOS_STATUS[chamado.status].map((s) => (
            <button
              key={s}
              className={`btn-acao btn-acao--${s.toLowerCase()}`}
              onClick={() => atualizarStatus(s)}
              disabled={loading}
            >
              {loading ? '...' : STATUS_LABEL[s]}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
