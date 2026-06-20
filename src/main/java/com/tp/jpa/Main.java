package com.tp.jpa;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.DataLoader;
import com.tp.jpa.util.Validator;

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
        UsuarioRepository usuarioRepo = new UsuarioRepository();


        //Clase solo para pruebas - carga categorias y productos si la base se encuentra vacia.
        DataLoader.load(categoriaRepo, productoRepo);

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("===== MENÚ PRINCIPAL =====\n..........................");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4. Reportes");
            System.out.println("0. Salir");
            System.out.println("..........................\n");

            opcion = intSeguro(sc, "Ingrese un numero: ");

            switch (opcion) {

                case 1 -> menuCategorias(sc, categoriaRepo);

                case 2 -> menuProductos(sc, productoRepo, categoriaRepo);

                case 3 -> menuUsuarios(sc, usuarioRepo);

                case 4 -> menuReportes(sc, productoRepo, categoriaRepo);


                    /*System.out.println("\n--- REPORTES ---");
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

                    }*/


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

    private static void menuUsuarios(Scanner sc, UsuarioRepository usuarioRepo) {
        int opcion;
        do {
            System.out.println("\n===== MENÚ DE USUARIOS =====");
            System.out.println("1. Alta de usuario");
            System.out.println("2. Modificación de usuario");
            System.out.println("3. Baja lógica de usuario");
            System.out.println("4. Listado de usuarios activos");
            System.out.println("5. Buscar usuario por mail");
            System.out.println("0. Volver al menú principal");

            opcion = intSeguro(sc, "Ingrese un número: ");

            switch (opcion) {
                case 1 -> { // Alta de usuario
                    System.out.println("\n--- ALTA DE USUARIO ---");
                    System.out.println("\nIngrese los datos del usuario:");
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    if (nombre.isBlank()) {
                        System.out.println("El nombre no puede estar vacío.");
                        break;
                    }

                    System.out.print("Apellido: ");
                    String apellido = sc.nextLine();
                    if (apellido.isBlank()) {
                        System.out.println("El apellido no puede estar vacío.");
                        break;
                    }

                    System.out.print("Mail: ");
                    String mail = sc.nextLine();
                    // valida formato
                    if (!Validator.validarFormatoMail(mail)) {
                        System.out.println("Formato de mail inválido.");
                        break;
                    }

                    //valida si esta disponible
                    if (!Validator.validarMailDisponible(mail, usuarioRepo, sc)) {
                        break;
                    }

                    System.out.print("Celular (opcional): ");
                    String celular = sc.nextLine();

                    if (!Validator.validarCelular(celular)) {
                        System.out.println("El celular solo puede tener números y opcionalmente el símbolo +.");
                        break;
                    }

                    System.out.print("Ingrese Contraseña: ");
                    String contrasenia = sc.nextLine();
                    if (contrasenia.isBlank()) {
                        System.out.println("La contraseña no puede estar vacía.");
                        break;
                    }

                    System.out.println("Rol (1 = ADMIN, 2 = USUARIO): ");
                    int rolOpcion = intSeguro(sc, "Seleccione rol: ");
                    Rol rol = (rolOpcion == 1) ? Rol.ADMIN : Rol.USUARIO;

                    Usuario nuevo = Usuario.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .mail(mail)
                            .celular(celular)
                            .contrasenia(contrasenia)
                            .rol(rol)
                            .build();

                    nuevo = usuarioRepo.guardar(nuevo);
                    System.out.println("Usuario creado con ID: " + nuevo.getId());
                }

                case 2 -> { // Modificación
                    System.out.println("\n--- MODIFICACIÓN DE USUARIO ---");
                    if (!mostrarUsuariosActivos(usuarioRepo)) {
                        System.out.println("No hay usuarios activos.");
                        break;
                    }


                    Long id = LongSeguro(sc, "Seleccione ID de usuario: ");
                    var optUser = usuarioRepo.buscarPorId(id);
                    if (optUser.isEmpty() || optUser.get().isEliminado()) {
                        System.out.println("Usuario inválido.");
                        break;
                    }
                    Usuario user = optUser.get();

                    System.out.println("Nombre actual: " + user.getNombre());
                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String nuevoNombre = sc.nextLine();
                    if (!nuevoNombre.isBlank()) user.setNombre(nuevoNombre);

                    System.out.println("Apellido actual: " + user.getApellido());
                    System.out.print("Nuevo apellido (ENTER para mantener): ");
                    String nuevoApellido = sc.nextLine();
                    if (!nuevoApellido.isBlank()) user.setApellido(nuevoApellido);

                    System.out.println("Mail actual: " + user.getMail());
                    System.out.print("Nuevo mail (ENTER para mantener): ");
                    String nuevoMail = sc.nextLine();

                    if (!nuevoMail.isBlank()) {
                        // Validar formato de mail
                        if (!Validator.validarFormatoMail(nuevoMail)) {
                            System.out.println("Formato de mail inválido.");
                            break;
                        }

                        // valida disponibilidad
                        if (!Validator.validarMailDisponible(nuevoMail, usuarioRepo, sc)) {
                            break;
                        }
                    }

                    // Verifica que si el mail existe sea del mismo usuario
                    var existente = usuarioRepo.buscarPorMail(nuevoMail);
                    if (existente.isPresent() && !existente.get().getId().equals(user.getId())) {
                        System.out.println("Ya existe otro usuario con ese mail.");
                        break;
                    }

                    user.setMail(nuevoMail);

                    System.out.println("Celular actual: " + user.getCelular());
                    System.out.print("Nuevo celular (ENTER para mantener): ");
                    String nuevoCelular = sc.nextLine();

                    if (!nuevoCelular.isBlank()) {
                        if (!Validator.validarCelular(nuevoCelular)) {
                            System.out.println("El celular debe contener solo números.");
                            break;
                        }
                        user.setCelular(nuevoCelular);
                    }

                    System.out.print("Nueva contraseña (ENTER para mantener): ");
                    String nuevaPass = sc.nextLine();
                    if (!nuevaPass.isBlank()) user.setContrasenia(nuevaPass);

                    usuarioRepo.guardar(user);
                    System.out.println("Usuario modificado correctamente.");
                }

                case 3 -> { // Baja lógica
                    System.out.println("\n--- BAJA LÓGICA DE USUARIO ---");
                    if (!mostrarUsuariosActivos(usuarioRepo)) {
                        System.out.println("No hay usuarios activos.");
                        break;
                    }
                    Long id = LongSeguro(sc, "Seleccione ID de usuario: ");
                    var optUser = usuarioRepo.buscarPorId(id);
                    if (optUser.isEmpty() || optUser.get().isEliminado()) {
                        System.out.println("Usuario inválido.");
                        break;
                    }
                    Usuario user = optUser.get();
                    if (usuarioRepo.eliminarLogico(id)) {
                        System.out.println("Usuario dado de baja: " + user.getNombre() + " " + user.getApellido());
                    } else {
                        System.out.println("No se pudo eliminar el usuario.");
                    }
                }

                case 4 -> { // Listado
                    System.out.println("\n--- LISTADO DE USUARIOS ACTIVOS ---");
                    mostrarUsuariosActivos(usuarioRepo);
                }

                case 5 -> { // Buscar por mail
                    System.out.print("Ingrese mail: ");
                    String mail = sc.nextLine();
                    var optUser = usuarioRepo.buscarPorMail(mail);
                    if (optUser.isPresent()) {
                        Usuario u = optUser.get();
                        System.out.println("Usuario encontrado:");
                        System.out.println("ID: " + u.getId());
                        System.out.println("Nombre: " + u.getNombre() + " " + u.getApellido());
                        System.out.println("Mail: " + u.getMail());
                        System.out.println("Celular: " + u.getCelular());
                        System.out.println("Rol: " + u.getRol());
                    } else {
                        System.out.println("No existe usuario activo con ese mail.");
                    }
                }

                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }


    private static void menuReportes(Scanner sc, ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        int opcionReporte;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Listar Productos por categoría");
            System.out.println("0. Volver al menú principal");

            opcionReporte = intSeguro(sc, "Ingrese un número: ");

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
                default -> System.out.println("Opción inválida.");
            }
        } while (opcionReporte != 0);
    }
}





