package com.tp.jpa.model.entities;
import com.tp.jpa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter

@ToString(callSuper = true, exclude = "contrasenia")
@EqualsAndHashCode(callSuper = false, of = {"mail"})

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends Base {
    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String mail;
    @Column(unique = true)
    private String celular;
    private String contrasenia;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    //sets de pedidos


    @Builder.Default
    @OneToMany(mappedBy = "usuario")
    private Set<Pedido> pedidos = new HashSet<>();
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
    }



}