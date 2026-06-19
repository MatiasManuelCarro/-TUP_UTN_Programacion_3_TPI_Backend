package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;

import java.util.List;

public class Reports {
    public static void listarProductoPorCategoriaHelper(ProductoRepository productoRepo, long idReporte){

        List<Producto> productos = productoRepo.buscarPorCategoria(idReporte);

        if (productos.stream().findAny().isEmpty()) {
            System.out.println("No hay productos activos para la categoría seleccionada.");
        } else {
            System.out.println("Productos de la categoria: ");
            productos.forEach(p ->
                    System.out.println(
                            "ID: " + p.getId() +
                                    " | Nombre: " + p.getNombre() +
                                    " | Precio: " + p.getPrecio() +
                                    " | Descripcion: " + p.getDescripcion() +
                                    " | Stock: " + p.getStock() +
                                    " | Categoría: " + p.getCategoria().getNombre()
                    )
            );
        }
    }

    public static boolean mostrarProductosActivos(ProductoRepository productoRepo) {

        List <Producto> productos = productoRepo.listarActivos();

        if (productos.stream().findAny().isEmpty()) {
            System.out.println("No hay productos activos.");
            return false;
        } else {
            productos.forEach(p ->
                    System.out.println(
                            "ID: " + p.getId() +
                                    " | Nombre: " + p.getNombre() +
                                    " | Precio: " + p.getPrecio() +
                                    " | Descripcion: " + p.getDescripcion() +
                                    " | Stock: " + p.getStock() +
                                    " | Categoría: " + p.getCategoria().getNombre()
                    )
            );
            return true;
        }
    }

    public static boolean mostrarCategoriasActivas(CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = categoriaRepo.listarActivos();

        if (categorias.stream().findAny().isEmpty()) {
            System.out.println("No hay categorías activas.");
            return false;
        } else {
            System.out.println("Categorias disponibles :");
            categorias.forEach(cat ->
                    System.out.println(
                            "ID: " + cat.getId() +
                                    " | Nombre: " + cat.getNombre() +
                                    " | Descripcion: " + cat.getDescripcion()
                    )
            );
            return true;}
    }

}