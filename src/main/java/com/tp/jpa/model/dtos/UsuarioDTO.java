package com.tp.jpa.model.dtos;

public record UsuarioDTO(
        String nombre,
        String apellido,
        String mail,
        String celular) {
    public UsuarioDTO {
        if (nombre == null) {
            nombre = "Sin nombre";
        } else if (apellido == null) {
            apellido = "Sin apellido";
        } else if (mail == null) {
            mail = "Sin mail";
        } else if (celular == null) {
            celular = "Sin celular";
        }
    }

}

