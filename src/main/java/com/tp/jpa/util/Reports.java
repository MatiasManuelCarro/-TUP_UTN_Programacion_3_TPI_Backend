package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;

import java.util.List;
import java.util.Locale;

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

    public static boolean mostrarPedidosPorEstado(PedidoRepository pedidoRepo, Estado estadoBuscado) {
        var pedidos = pedidoRepo.buscarPorEstado(estadoBuscado);

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos con estado " + estadoBuscado + ".");
            return false;
        }

        System.out.println("\n--- PEDIDOS EN ESTADO " + estadoBuscado + " ---");
        for (Pedido p : pedidos) {
            System.out.println("ID: " + p.getId() +
                    " | Fecha: " + p.getFecha() +
                    " | Usuario: " + p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() +
                    " | Total: $" + p.getTotal());
        }
        return true;
    }

    public static boolean mostrarPedidosPorUsuario(PedidoRepository pedidoRepo, Long idUsuario) {
        var pedidos = pedidoRepo.buscarPorUsuario(idUsuario);

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos para el usuario con ID " + idUsuario + ".");
            return false;
        }

        System.out.println("\n--- PEDIDOS DEL USUARIO " + idUsuario + " ---");
        for (Pedido p : pedidos) {
            System.out.println("ID: " + p.getId() +
                    " | Fecha: " + p.getFecha() +
                    " | Estado: " + p.getEstado() +
                    " | Forma de pago: " + p.getFormapago() +
                    " | Total: $" + p.getTotal());
        }
        return true;
    }

    public static void mostrarTotalFacturado(PedidoRepository pedidoRepo) {
        var pedidosTerminados = pedidoRepo.buscarPorEstado(Estado.TERMINADO);

        double totalFacturado = pedidosTerminados.stream()
                .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                .sum();

        System.out.println("Total facturado: " +
                String.format(Locale.US, "$%.2f", totalFacturado));
    }



}