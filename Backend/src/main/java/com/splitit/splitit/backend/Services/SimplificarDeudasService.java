package com.splitit.splitit.backend.Services;

import java.util.List;

import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.DTO.DtSaldoSimplificado;



public interface SimplificarDeudasService {
	public List<DtSaldoSimplificado> simplificarDeudas(List<DtSaldo> saldos);
	

}
