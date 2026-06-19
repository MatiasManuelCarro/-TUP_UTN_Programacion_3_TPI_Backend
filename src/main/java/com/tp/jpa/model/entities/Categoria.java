package com.tp.jpa.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true, exclude = "productos")
@EqualsAndHashCode(callSuper = false, of = {"nombre"})
@AllArgsConstructor
@NoArgsConstructor
public class Categoria extends Base {
    @Column(unique = true)
    private String nombre;
    private String descripcion;
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Producto> productos = new HashSet<>();

    //helper para mantener la bidireccionalidad
    public void addProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }
}



