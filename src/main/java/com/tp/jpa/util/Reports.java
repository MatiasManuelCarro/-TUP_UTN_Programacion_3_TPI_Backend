package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;

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

    public static boolean mostrarUsuariosActivos(UsuarioRepository usuarioRepo) {
        var usuarios = usuarioRepo.listarActivos();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios activos.");
            return false;
        }

        System.out.println("\n--- USUARIOS ACTIVOS ---");
        for (Usuario u : usuarios) {
            System.out.println("ID: " + u.getId() +
                    " | Nombre: " + u.getNombre() + " " + u.getApellido() +
                    " | Mail: " + u.getMail() +
                    " | Rol: " + u.getRol());
        }
        return true;
    }

    public static boolean mostrarPedidosActivos(PedidoRepository pedidoRepo) {
        var pedidos = pedidoRepo.listarActivos();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos activos.");
            return false;
        }

        System.out.println("\n--- PEDIDOS ACTIVOS ---");
        for (Pedido p : pedidos) {
            System.out.println("ID: " + p.getId() +
                    " | Fecha: " + p.getFecha() +
                    " | Estado: " + p.getEstado() +
                    " | Forma de pago: " + p.getFormapago() +
                    " | Usuario: " + p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() +
                    " | Total: $" + p.getTotal());
        }
        return true;
    }




}