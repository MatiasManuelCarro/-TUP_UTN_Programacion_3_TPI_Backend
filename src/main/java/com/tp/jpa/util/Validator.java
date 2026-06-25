package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;

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


    //devuelve true si esta disponible | false si no esta disponible el mail (ya en uso)
    public static boolean validarMailDisponible(String mail, UsuarioRepository usuarioRepo, Scanner sc) {
        // null o vacío
        if (mail == null || mail.isBlank()) {
            System.out.println("El mail no puede estar vacío.");
            return false;
        }

        // buscar usuario activo por mail
        Optional<Usuario> existente = usuarioRepo.buscarPorMail(mail);
        if (existente.isPresent()) {
            System.out.println("Ya existe un usuario activo con ese mail.");
            return false;
        }

        // buscar usuario inactivo
        Optional<Usuario> usuarioInactivo = usuarioRepo.listarInactivos()
                .stream()
                .filter(u -> u.getMail().equalsIgnoreCase(mail))
                .findFirst();


        // buscar usuario activo por mail
        //existe inactivo → ofrecer reactivación

        if (usuarioInactivo.isPresent()) {
            System.out.println("Ya existe un usuario con ese mail, pero se encuentra inactivo." +
                    "\n¿Desea activarlo nuevamente?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            int opcionAlta = intSeguro(sc, "Seleccione una opción: ");

            switch (opcionAlta) {
                case 1 -> {
                    Usuario u = usuarioInactivo.get();
                    usuarioRepo.AltaLogica(u.getId());
                    System.out.println("Usuario reactivado correctamente. ID: " + u.getId());
                    return false;
                }
                case 2 -> {
                    System.out.println("Operación cancelada.");
                    return false;
                }
                default -> {
                    System.out.println("Opción inválida. Se cancela la operación.");
                    return false; // corta el flujo
                }
            }
        }

        // Si pasa todas las evaluaciones se puede dar de alta
        return true;
    }

    public static boolean validarFormatoMail(String mail) {
        if (mail == null || mail.isBlank()) return false;
        String REGEX_EMAIL = "^.+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return mail.matches(REGEX_EMAIL);
    }


    //validar celulares
    public static boolean validarCelular(String celular) {
        if (celular == null || celular.isBlank()) {
            return true; // como es opcional se permite vacio
        }
        return celular.matches("^\\+?[0-9]+$"); // permite numeros y el + (por ejemplo +549)
    }


}
