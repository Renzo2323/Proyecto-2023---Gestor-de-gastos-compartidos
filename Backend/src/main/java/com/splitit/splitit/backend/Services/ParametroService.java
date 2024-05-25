package com.splitit.splitit.backend.Services;

import java.util.List;

import com.splitit.splitit.backend.DTO.DtParametro;
import com.splitit.splitit.backend.DTO.DtResponse;

public interface ParametroService {
	List<DtParametro> getParametros();

	DtResponse crearParametro(DtParametro dtParametro);

	DtResponse modificarParametro(DtParametro dtParametro);

	DtResponse eliminarParametro(int parametroId);

	DtResponse reemplazarParametros(List<DtParametro> parametros);
}
