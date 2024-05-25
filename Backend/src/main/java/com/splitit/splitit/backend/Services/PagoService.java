package com.splitit.splitit.backend.Services;

import com.splitit.splitit.backend.DTO.DtAvatar;
import com.splitit.splitit.backend.DTO.DtPago;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExistePagoException;

import java.util.List;


public interface PagoService {
	DtResponse nuevoPago(DtPago pago);
	List<DtSaldo> getSaldosByGrupo(int grupoId);
	List<DtSaldo> getSaldosSimplificadosByGrupo(int grupoId);
	List<DtSaldo> getSaldosSimplificadosByGrupoNew(int grupoId) throws NoExisteGrupoException, Exception;
	DtResponse eliminarPago(int pagoId) throws NoExistePagoException;
}
