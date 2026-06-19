package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;

import java.util.Optional;
import java.util.Scanner;

import static com.tp.jpa.util.Input.intSeguro;

public class Validator {

    public static boolean validarNombreCategoria(String nombre, CategoriaRepository categoriaRepo, Scanner sc) {
        //Validacion de nombre duplicado
        boolean existe = categoriaRepo.listarActivos()
                .stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));

        Optional<Categoria> categoriaInactiva = categoriaRepo.listarInactivos()
                .stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();

        //verificacion de categoria repetida
        if (existe) {
            System.out.println("Ya existe una categoría con ese nombre.");
            return true; // no continua
        }
        //verificacion de categoria existente pero con baja logica
        if (categoriaInactiva.isPresent()) {
            System.out.println("Ya existe una categoría con ese nombre, pero se encuentra inactiva" +
                    "\n ¿Desea activarla nuevamente");
            System.out.println("1. Si");
            System.out.println("2. No");
            int opcionAlta = intSeguro(sc, "Seleccione una opción: ");

            switch (opcionAlta) {
                case 1 -> {
                    Categoria cat = categoriaInactiva.get();
                    categoriaRepo.AltaLogica(cat.getId());
                    System.out.println("Categoría reactivada correctamente. ID: " + cat.getId());
                    return true;
                }
                case 2 -> {
                    System.out.println("Operación cancelada.");
                    return true; //no continua
                }

            }

        }
        return false; //si pasa las evaluaciones continua con el alta
    }


    public static boolean validarNombreProducto(String nombre, ProductoRepository productoRepo, Scanner sc) {
        boolean existe = productoRepo.listarActivos()
                .stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));

        Optional<Producto> prodInactivo = productoRepo.listarInactivos()
                .stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();

        //verificacion de producto repetido
        if (existe) {
            System.out.println("Ya existe un producto con ese nombre.");
            return true; // no continua
        }

        //verificacion de producto existente pero con baja logica
        if (prodInactivo.isPresent()) {
            System.out.println("Ya existe un producto con ese nombre, pero se encuentra inactivo" +
                    "\n ¿Desea activarlo nuevamente");//
            System.out.println("1. Si");
            System.out.println("2. No");
            int opcionAltaProd = intSeguro(sc, "Seleccione una opción: ");

            switch (opcionAltaProd) {
                case 1 -> {
                    Producto prod = prodInactivo.get();
                    productoRepo.AltaLogica(prod.getId());
                    System.out.println("Producto reactivado correctamente. ID: " + prod.getId());
                    return true; // no continua
                }
                case 2 -> {
                    System.out.println("Operación cancelada.");
                    return true; // no continua
                }
            }


        }
        return false; //si pasa las evaluaciones continua con el alta
    }
}
