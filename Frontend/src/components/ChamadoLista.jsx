import { useState, useEffect, useCallback } from 'react';
import { chamadoService } from '../services/api';
import ChamadoCard from './ChamadoCard';

const FILTROS = [
  { label: 'Todos', value: '' },
  { label: 'Abertos', value: 'ABERTO' },
  { label: 'Em andamento', value: 'EM_ANDAMENTO' },
  { label: 'Fechados', value: 'FECHADO' },
];

export default function ChamadoLista({ refresh }) {
  const [chamados, setChamados] = useState([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);

  const carregar = useCallback(async () => {
    try {
      setLoading(true);
      const res = filtro
        ? await chamadoService.listarPorStatus(filtro)
        : await chamadoService.listarTodos();
      setChamados(res.data);
    } finally {
      setLoading(false);
    }
  }, [filtro]);

  useEffect(() => { carregar(); }, [carregar, refresh]);

  return (
    <div className="lista-wrapper">
      <div className="lista-header">
        <h2 className="lista-titulo">Chamados</h2>
        <div className="filtros">
          {FILTROS.map((f) => (
            <button
              key={f.value}
              className={`btn-filtro ${filtro === f.value ? 'btn-filtro--ativo' : ''}`}
              onClick={() => setFiltro(f.value)}
            >
              {f.label}
            </button>
          ))}
        </div>
      </div>

      {loading ? (
        <p className="estado-vazio">Carregando...</p>
      ) : chamados.length === 0 ? (
        <p className="estado-vazio">Nenhum chamado encontrado.</p>
      ) : (
        <div className="lista-grid">
          {chamados.map((c) => (
            <ChamadoCard key={c.id} chamado={c} onAtualizado={carregar} />
          ))}
        </div>
      )}
    </div>
  );
}
