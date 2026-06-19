package com.tp.jpa.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "productos")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false , of = {"nombre", "precio"})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Producto extends Base {
    @Column(unique = true)
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}