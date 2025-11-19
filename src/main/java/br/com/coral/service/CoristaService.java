package br.com.coral.service;

import br.com.coral.dao.CoristaDAO;
import br.com.coral.model.Corista;

import java.util.Optional;

public class CoristaService {

    private final CoristaDAO coristaDAO = new CoristaDAO();

    public boolean podeSeApresentar(int coristaId) throws Exception {

        Optional<Corista> coristaOpt = coristaDAO.buscarPorId(coristaId);

        if (coristaOpt.isEmpty()) {
            throw new IllegalArgumentException("Corista ID " + coristaId + " não encontrado.");
        }

        Corista corista = coristaOpt.get();

        if (corista.isTemPendencias()) {
            System.out.println("RN_FAIL: Corista ID " + coristaId + " possui pendências e não pode se apresentar.");
            return false;
        }

        if (corista.getFaltasNosUltimosDoisEnsaios() >= 2) {
            System.out.println("RN_FAIL: Corista ID " + coristaId + " faltou aos dois últimos ensaios.");
            return false;
        }

        return true;
    }

    public void registrarFalta(int coristaId) throws Exception {
        Optional<Corista> coristaOpt = coristaDAO.buscarPorId(coristaId);

        if (coristaOpt.isEmpty()) {
            throw new IllegalArgumentException("Corista ID " + coristaId + " não encontrado para registro de falta.");
        }

        Corista corista = coristaOpt.get();
        int novasFaltas = corista.getFaltasNosUltimosDoisEnsaios() + 1;

        corista.setFaltasNosUltimosDoisEnsaios(novasFaltas);

        System.out.println("RN_UPDATE: Corista ID " + coristaId + " agora tem " + novasFaltas + " faltas.");
    }
}