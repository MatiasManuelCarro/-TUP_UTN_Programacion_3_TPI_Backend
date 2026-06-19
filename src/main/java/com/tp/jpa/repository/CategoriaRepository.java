package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Base;
import com.tp.jpa.model.entities.Categoria;



public class CategoriaRepository extends BaseRepository<Categoria> {

    public CategoriaRepository() {
        super(Categoria.class);
    }
}