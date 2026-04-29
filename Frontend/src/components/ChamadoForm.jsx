import { useState } from 'react';
import { chamadoService } from '../services/api';

export default function ChamadoForm({ onCriado }) {
  const [form, setForm] = useState({ titulo: '', descricao: '' });
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro('');
    if (!form.titulo.trim()) {
      setErro('O título é obrigatório.');
      return;
    }
    try {
      setLoading(true);
      await chamadoService.criar(form);
      setForm({ titulo: '', descricao: '' });
      onCriado();
    } catch (err) {
      setErro(err.response?.data?.erro || 'Erro ao criar chamado.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="form-card" onSubmit={handleSubmit}>
      <h2 className="form-title">Novo chamado</h2>

      <div className="field">
        <label>Título</label>
        <input
          name="titulo"
          value={form.titulo}
          onChange={handleChange}
          placeholder="Descreva o problema brevemente"
          autoComplete="off"
        />
      </div>

      <div className="field">
        <label>Descrição</label>
        <textarea
          name="descricao"
          value={form.descricao}
          onChange={handleChange}
          placeholder="Detalhes do problema..."
          rows={4}
        />
      </div>

      {erro && <p className="erro">{erro}</p>}

      <button className="btn-submit" type="submit" disabled={loading}>
        {loading ? 'Enviando...' : 'Abrir chamado'}
      </button>
    </form>
  );
}
