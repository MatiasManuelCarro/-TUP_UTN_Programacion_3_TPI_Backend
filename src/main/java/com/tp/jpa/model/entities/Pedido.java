package com.tp.jpa.model.entities;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private Double total;
    @Enumerated(EnumType.STRING)
    private FormaPago formapago;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    //coleccion de detalle pedidos
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetallePedido> detallePedidos = new HashSet<>();


    //Agregar detalle
    public void addDetallePedido(int cantidad, Producto producto) {

        boolean exists = detallePedidos.stream()
                .anyMatch(detallePedido -> detallePedido.getProducto().equals(producto));
        if (exists) return;

        DetallePedido detallePedido = DetallePedido.builder()
                .cantidad(cantidad)
                .producto(producto)
                .subtotal(producto.getPrecio()*cantidad)
                .pedido(this)
                .build();
        detallePedidos.add(detallePedido);

    }

    //Buscar detalle por producto
    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        return detallePedidos.stream()
                .filter(detallePedido -> detallePedido.getProducto().equals(producto))
                .findFirst()
                .orElse(null);
    }


    //Eliminar detalle por producto
    public void deleteDetalleByProducto(Producto producto) {
        detallePedidos.removeIf(d -> d.getProducto().equals(producto));
    }


    @Override
    public void calcularTotal() {
        double totalFinal = detallePedidos.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
        this.total = totalFinal;
        System.out.println("Total del pedido: " + totalFinal);
    }



}