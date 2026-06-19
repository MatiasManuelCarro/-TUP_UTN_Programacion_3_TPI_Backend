package com.tp.jpa;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.util.DataLoader;

import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;

import static com.tp.jpa.util.Reports.*;
import static com.tp.jpa.util.Input.*;
import static com.tp.jpa.util.Validator.validarNombreCategoria;
import static com.tp.jpa.util.Validator.validarNombreProducto;


public class Main {
    public static void main(String[] args) {


        //quita los logs de hibernate dejando solo los errores.
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Scanner sc = new Scanner(System.in);

        CategoriaRepository categoriaRepo = new CategoriaRepository();
        ProductoRepository productoRepo = new ProductoRepository();

        //Clase solo para pruebas - carga categorias y productos si la base se encuentra vacia.
        DataLoader.load(categoriaRepo, productoRepo);

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("===== MENÚ PRINCIPAL =====\n..........................");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Reportes");
            System.out.println("0. Salir");
            System.out.println("..........................\n");

            opcion = intSeguro(sc, "Ingrese un numero: ");

            switch (opcion) {

                case 1 -> menuCategorias(sc, categoriaRepo);

                case 2 -> menuProductos(sc, productoRepo, categoriaRepo);

                case 3 -> {

                    System.out.println("\n--- REPORTES ---");
                    System.out.println("Elija una opcion: ");

                    System.out.println("1. Listar  Productos por categoria");
                    System.out.println("0. Volver al menu principal");

                    int opcionReporte = intSeguro(sc, "Ingrese un numero: \n");

                    switch (opcionReporte) {
                        case 1 -> {

                            if (!mostrarCategoriasActivas(categoriaRepo)) {
                                System.out.println("Operación cancelada.");
                                break;
                            }

                            Long idReporte = LongSeguro(sc, "\nSeleccione ID de categoría: ");

                            listarProductoPorCategoriaHelper(productoRepo, idReporte);


                        }
                        case 0 -> System.out.println("Volviendo al menú principal...");

                    }

                }

                case 0 -> System.out.println("Saliendo del sistema...");

                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void menuCategorias(Scanner sc, CategoriaRepository categoriaRepo) {

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("\n===== MENÚ DE CATEGORÍAS =====");
            System.out.println("1. Alta de categoría");
            System.out.println("2. Baja lógica de categoría");
            System.out.println("3. Modificación de categoría");
            System.out.println("4. Listado de categorías activas");
            System.out.println("0. Volver al Menu Principal");
            System.out.println("..........................\n");

            opcion = intSeguro(sc, "Ingrese un numero: ");


            switch (opcion) {

                case 1 -> { //Alta de categoría
                    System.out.println("\n--- ALTA DE CATEGORÍA ---");
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();

                    if (nombre.isBlank()) {
                        System.out.println("No se puede crear una categoria sin nombre. Operación cancelada.");
                        break;
                    }

                    if (validarNombreCategoria(nombre, categoriaRepo, sc)) {
                        break; // si devuelve true no continua
                    }

                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();

                    Categoria nueva = Categoria.builder()
                            .nombre(nombre)
                            .descripcion(descripcion)
                            .build();

                    nueva = categoriaRepo.guardar(nueva);

                    System.out.println("Categoría creada con ID: " + nueva.getId());
                }

                case 2 -> { //Baja logica de Categoria
                    System.out.println("\n--- BAJA LÓGICA DE CATEGORÍA ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    System.out.print("Elija una categoria ");

                    //ingreso seguro de long
                    Long id = LongSeguro(sc, "Seleccione ID:");


                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(id);

                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("No existe una categoría activa con ese ID.");
                    } else {
                        Categoria cat = optionalCat.get();
                        boolean eliminado = categoriaRepo.eliminarLogico(id);

                        if (eliminado) {
                            System.out.println("Categoría dada de baja: " + cat.getNombre());
                        } else {
                            System.out.println("No se pudo eliminar la categoría.");
                        }
                    }
                }

                case 3 -> { //modificar categoria
                    System.out.println("\n--- MODIFICACIÓN DE CATEGORÍA ---");

                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("No hay categorías activas. Operación cancelada.");
                        break;
                    }

                    //revisar

                    Long id = LongSeguro(sc, "Ingrese ID para modificar la categoria: ");

                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(id);

                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("No existe una categoría activa con ese ID.");
                        break;
                    }

                    Categoria cat = optionalCat.get();

                    System.out.println("Valores actuales:");
                    System.out.println("Nombre: " + cat.getNombre());
                    System.out.println("Descripción: " + cat.getDescripcion());

                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String nuevoNombre = sc.nextLine();

                    if (validarNombreCategoria(nuevoNombre, categoriaRepo, sc)) {
                        break; // salir del case sin pedir descripción
                    }

                    if (!nuevoNombre.isBlank()) cat.setNombre(nuevoNombre);

                    System.out.print("Nueva descripción (ENTER para mantener): ");
                    String nuevaDesc = sc.nextLine();
                    if (!nuevaDesc.isBlank()) cat.setDescripcion(nuevaDesc);

                    categoriaRepo.guardar(cat);

                    System.out.println("Categoría modificada correctamente.");
                }

                case 4 -> {
                    // Listar categorias
                    System.out.println("\n--- LISTADO DE CATEGORÍAS ACTIVAS ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("No hay categorías activas. Operación cancelada.");

                    }

                }


                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }


        } while (opcion != 0);

    }

    private static void menuProductos(Scanner sc, ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("\n===== MENÚ DE PRODUCTOS =====");
            System.out.println("1. Alta de producto");
            System.out.println("2. Baja lógica de producto");
            System.out.println("3. Modificación de producto");
            System.out.println("4. Listado de productos activos");
            System.out.println("0. Volver al Menu Principal");
            System.out.println("..........................\n");
            opcion = intSeguro(sc, "Ingrese un numero: ");

            switch (opcion) {

                case 1 -> { //Alta de producto
                    System.out.println("\n--- ALTA DE PRODUCTO ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    System.out.print("Seleccione ID de categoría: ");
                    //verificar long
                    Long idCat = LongSeguro(sc, "Seleccione ID de categoría: ");

                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(idCat);


                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("Categoría inválida.");
                        break;
                    }

                    Categoria categoria = optionalCat.get();

                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    if (nombre.isBlank()) {
                        System.out.println("El nombre no puede estar vacío.");
                        break;
                    }
                    if (validarNombreProducto(nombre, productoRepo, sc)) {
                        break; // salir del case sin pedir descripción
                    }

                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();

                    System.out.print("Precio: ");
                    double precio = DoubleSeguro(sc, "Ingrese el precio: ");

                    if (precio <= 0) {
                        System.out.println("El precio debe ser mayor a 0.");
                        break;
                    }

                    System.out.print("Stock: ");
                    int stock = intSeguro(sc, "Ingrese el stock: ");

                    if (stock < 0) {
                        System.out.println("El stock no puede ser negativo.");
                        break;
                    }

                    Producto nuevo = Producto.builder()
                            .nombre(nombre)
                            .descripcion(descripcion)
                            .precio(precio)
                            .stock(stock)
                            .disponible(true)
                            .categoria(categoria)
                            .build();

                    nuevo = productoRepo.guardar(nuevo);

                    System.out.println("Producto creado con ID: " + nuevo.getId());
                    System.out.println("Categoría asignada: " + categoria.getNombre());
                }

                case 2 -> { //Baja lógica de producto
                    System.out.println("\n--- BAJA LÓGICA DE PRODUCTO ---");


                    if (!mostrarProductosActivos(productoRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    Long id = LongSeguro(sc, "Seleccione ID de producto: ");

                    Optional<Producto> optionalProd = productoRepo.buscarPorId(id);

                    if (optionalProd.isEmpty() || optionalProd.get().isEliminado()) {
                        System.out.println("No existe un producto activo con ese ID.");
                        break;
                    }

                    Producto prod = optionalProd.get();

                    boolean eliminado = productoRepo.eliminarLogico(id);

                    if (eliminado) {
                        System.out.println("Producto dado de baja: " + prod.getNombre());
                    } else {
                        System.out.println("No se pudo eliminar el producto.");
                    }
                }

                case 3 -> { //Modificación de producto
                    System.out.println("\n--- MODIFICACIÓN DE PRODUCTO ---");


                    if (!mostrarProductosActivos(productoRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    Long id = LongSeguro(sc, "Seleccione ID de producto: ");

                    Optional<Producto> optionalProd = productoRepo.buscarPorId(id);

                    if (optionalProd.isEmpty() || optionalProd.get().isEliminado()) {
                        System.out.println("No existe un producto activo con ese ID.");
                        break;
                    }

                    Producto prod = optionalProd.get();

                    System.out.println("Valores actuales:");
                    System.out.println("Nombre: " + prod.getNombre());
                    System.out.println("Precio: " + prod.getPrecio());
                    System.out.println("Descripción: " + prod.getDescripcion());
                    System.out.println("Stock: " + prod.getStock());

                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String nuevoNombre = sc.nextLine();
                    if (!nuevoNombre.isBlank()) {
                        if (validarNombreProducto(nuevoNombre, productoRepo, sc)) {
                            break;
                        }
                        prod.setNombre(nuevoNombre);
                    }


                    System.out.print("Nuevo precio (ENTER para mantener): ");
                    String inputPrecio = sc.nextLine();

                    if (!inputPrecio.isBlank()) {
                        double precio;

                        try {
                            precio = Double.parseDouble(inputPrecio); //intenta parsearlo
                        } catch (NumberFormatException e) {
                            //si falla llama la funcion doubleseguro
                            precio = DoubleSeguro(sc, "Ingrese el nuevo precio, recuerde utilizar solo numeros : ");
                        }

                        if (precio <= 0) {
                            System.out.println("El precio debe ser mayor a 0.");
                            break;
                        }

                        prod.setPrecio(precio);
                    }

                    System.out.print("Nueva descripción (ENTER para mantener): ");
                    String nuevaDescripcion = sc.nextLine();
                    if (!nuevaDescripcion.isBlank()) prod.setDescripcion(nuevaDescripcion);

                    System.out.print("Nuevo stock (ENTER para mantener): ");
                    String inputStock = sc.nextLine();

                    if (!inputStock.isBlank()) {
                        int stock;

                        try {
                            stock = Integer.parseInt(inputStock); //intenta parsearlo
                        } catch (NumberFormatException e) {
                            //si falla llama la funcion intseguro
                            stock = intSeguro(sc, "Ingrese el nuevo stock, recuerde utilizar solo numeros : ");
                        }

                        if (stock < 0) {
                            System.out.println("El stock no puede ser negativo.");
                            break;
                        }

                        prod.setStock(stock);
                    }

                    productoRepo.guardar(prod);

                    System.out.println("Producto modificado correctamente.");
                }

                case 4 -> { //Listado de productos activos
                    System.out.println("\n--- LISTADO DE PRODUCTOS ACTIVOS ---");

                    mostrarProductosActivos(productoRepo);


                }


                // Volver
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

}





