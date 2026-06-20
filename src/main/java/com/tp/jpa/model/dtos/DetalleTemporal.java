package com.tp.jpa.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Objeto temporal usado en el alta de pedido.
 * Guarda ID de producto y cantidad seleccionada
 * antes de persistir el pedido en la BD.
 */
@Getter
@AllArgsConstructor
public class DetalleTemporal {
    private Long idProducto;
    private int cantidad;
}

