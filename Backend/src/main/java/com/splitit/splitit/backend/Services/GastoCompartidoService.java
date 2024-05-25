package com.splitit.splitit.backend.Services;

import java.util.List;

import com.splitit.splitit.backend.DTO.CantGastosPorCategoria;
import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.DTO.DtResponse;

public interface GastoCompartidoService {
	DtResponse nuevoGastoCompartido(DtGastoCompartido gastoCompartido);
	List<CantGastosPorCategoria> getCantGastosPorCategoria();
	DtResponse eliminarGastoCompartido(int gastoCompartidoId);
}
