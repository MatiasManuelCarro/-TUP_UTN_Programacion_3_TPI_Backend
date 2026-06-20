package com.tp.jpa.repository;

import com.tp.jpa.model.entities.DetallePedido;

public class DetallePedidoRepository extends BaseRepository<DetallePedido> {
    public DetallePedidoRepository() {
        super(DetallePedido.class);
    }

}