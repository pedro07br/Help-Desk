import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

export const chamadoService = {
  listarTodos: () => api.get('/chamados'),
  listarPorStatus: (status) => api.get(`/chamados/status/${status}`),
  buscarPorId: (id) => api.get(`/chamados/${id}`),
  criar: (chamado) => api.post('/chamados', chamado),
  atualizarStatus: (id, status) =>
    api.patch(`/chamados/${id}/status`, { status }),
};
