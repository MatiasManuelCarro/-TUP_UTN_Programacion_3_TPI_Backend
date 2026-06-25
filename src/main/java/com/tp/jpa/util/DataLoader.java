package com.tp.jpa.util;

import com.tp.jpa.model.entities.*;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;

import java.time.LocalDate;

public class DataLoader {

    public static void load(CategoriaRepository categoriaRepo,
                            ProductoRepository productoRepo,
                            UsuarioRepository usuarioRepo, PedidoRepository pedidoRepo) {

        //ESTA CLASE ES SOLO PARA PRUEBAS DEL CODIGO. CARGA CATEGORIAS, PRODUCTOS, USUARIOS Y PEDIDOS SOLAMENTE SI LA BASE SE ENCUENTRA VACIA.
        // Si ya existen categorías o productos cargados, no carga nada


            boolean hayCategorias = !categoriaRepo.listarActivos().isEmpty() ||
                    !categoriaRepo.listarInactivos().isEmpty();
            boolean hayProductos = !productoRepo.listarActivos().isEmpty() ||
                    !productoRepo.listarInactivos().isEmpty();
            boolean hayUsuarios = !usuarioRepo.listarActivos().isEmpty() ||
                    !usuarioRepo.listarInactivos().isEmpty();

            if (hayCategorias || hayProductos || hayUsuarios) {
                return;
            }


            // ============================
            // CATEGORÍAS
            // ============================
            Categoria hamburguesas = categoriaRepo.guardar(
                    Categoria.builder().nombre("Hamburguesas")
                            .descripcion("Hamburguesas artesanales con carne premium").build());

            Categoria pizzas = categoriaRepo.guardar(
                    Categoria.builder().nombre("Pizzas")
                            .descripcion("Pizzas al horno de barro").build());

            Categoria empanadas = categoriaRepo.guardar(
                    Categoria.builder().nombre("Empanadas")
                            .descripcion("Empanadas caseras horneadas").build());

            Categoria bebidas = categoriaRepo.guardar(
                    Categoria.builder().nombre("Bebidas")
                            .descripcion("Bebidas frías y calientes").build());

            Categoria papas = categoriaRepo.guardar(
                    Categoria.builder().nombre("Papas Fritas")
                            .descripcion("Acompañamientos crujientes").build());

            // ============================
            // PRODUCTOS (Food Store)
            // ============================

// Hamburguesas
            Producto hamburguesaClasica = productoRepo.guardar(Producto.builder()
                    .nombre("Hamburguesa Clásica")
                    .precio(4500.0)
                    .descripcion("Carne 150g, lechuga, tomate, cebolla morada y mayonesa")
                    .stock(50)
                    .imagen("https://hamburguesa_clasica.jpg")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build());

            Producto hamburguesaDoble = productoRepo.guardar(Producto.builder()
                    .nombre("Hamburguesa Doble")
                    .precio(6200.0)
                    .descripcion("Doble carne 150g c/u, cheddar, panceta y salsa barbacoa")
                    .stock(40)
                    .imagen("https://hamburguesa_doble.jpg")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build());

            Producto hamburguesaCompleta = productoRepo.guardar(Producto.builder()
                    .nombre("Hamburguesa Completa")
                    .precio(5500.0)
                    .descripcion("Carne 150g, huevo, jamón, queso, lechuga y tomate")
                    .stock(30)
                    .imagen("https://hamburguesa_completa.jpg")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build());

            Producto hamburguesaVeggie = productoRepo.guardar(Producto.builder()
                    .nombre("Hamburguesa Veggie")
                    .precio(5000.0)
                    .descripcion("Medallón de lentejas y quinoa, rúcula, tomato confitado y hummus")
                    .stock(20)
                    .imagen("https://hamburguesa_veggie.jpg")
                    .disponible(true)
                    .categoria(hamburguesas)
                    .build());

// Pizzas
            Producto pizzaMozzarella = productoRepo.guardar(Producto.builder()
                    .nombre("Pizza Mozzarella")
                    .precio(3800.0)
                    .descripcion("Mozzarella, salsa de tomate y orégano")
                    .stock(25)
                    .imagen("https://pizza_mozzarella.jpg")
                    .disponible(true)
                    .categoria(pizzas)
                    .build());

            Producto pizzaNapolitana = productoRepo.guardar(Producto.builder()
                    .nombre("Pizza Napolitana")
                    .precio(4500.0)
                    .descripcion("Mozzarella, rodajas de tomate, ajo, aceitunas y albahaca")
                    .stock(25)
                    .imagen("https://pizza_napolitana.jpg")
                    .disponible(true)
                    .categoria(pizzas)
                    .build());

            Producto pizzaFugazzeta = productoRepo.guardar(Producto.builder()
                    .nombre("Pizza Fugazzeta")
                    .precio(4800.0)
                    .descripcion("Mozzarella, cebolla caramelizada, jamón y aceitunas")
                    .stock(20)
                    .imagen("https://pizza_fugazzeta.jpg")
                    .disponible(true)
                    .categoria(pizzas)
                    .build());

            Producto pizzaEspecial = productoRepo.guardar(Producto.builder()
                    .nombre("Pizza Especial")
                    .precio(5200.0)
                    .descripcion("Mozzarella, pepperoni, morrón, cebolla, huevo y aceitunas")
                    .stock(15)
                    .imagen("https://pizza_especial.jpg")
                    .disponible(true)
                    .categoria(pizzas)
                    .build());

// Empanadas
            Producto empanadaCarne = productoRepo.guardar(Producto.builder()
                    .nombre("Empanada de Carne")
                    .precio(1200.0)
                    .descripcion("Carne picada, cebolla, huevo duro, aceitunas y especias")
                    .stock(100)
                    .imagen("https://empanada_carne.jpg")
                    .disponible(true)
                    .categoria(empanadas)
                    .build());

            Producto empanadaPollo = productoRepo.guardar(Producto.builder()
                    .nombre("Empanada de Pollo")
                    .precio(1200.0)
                    .descripcion("Pollo desmenuzado, cebolla, morrón y crema")
                    .stock(80)
                    .imagen("https://empanada_pollo.jpg")
                    .disponible(true)
                    .categoria(empanadas)
                    .build());

            Producto empanadaJamonQueso = productoRepo.guardar(Producto.builder()
                    .nombre("Empanada Jamón y Queso")
                    .precio(1100.0)
                    .descripcion("Jamón cocido y mozzarella")
                    .stock(90)
                    .imagen("https://empanada_jamon_queso.jpg")
                    .disponible(false)
                    .categoria(empanadas)
                    .build());

            Producto empanadaVerdura = productoRepo.guardar(Producto.builder()
                    .nombre("Empanada de Verdura")
                    .precio(1100.0)
                    .descripcion("Espinaca, acelga, ricota y nuez moscada")
                    .stock(60)
                    .imagen("https://empanada_verdura.jpg")
                    .disponible(true)
                    .categoria(empanadas)
                    .build());

// Bebidas
            Producto cocaCola500ml = productoRepo.guardar(Producto.builder()
                    .nombre("Coca-Cola 500ml")
                    .precio(1500.0)
                    .descripcion("Gaseosa Coca-Cola original 500ml")
                    .stock(200)
                    .imagen("https://coca_cola_500ml.jpg")
                    .disponible(true)
                    .categoria(bebidas)
                    .build());

            Producto sprite500ml = productoRepo.guardar(Producto.builder()
                    .nombre("Sprite 500ml")
                    .precio(1500.0)
                    .descripcion("Gaseosa Sprite 500ml")
                    .stock(180)
                    .imagen("https://sprite_500ml.jpg")
                    .disponible(true)
                    .categoria(bebidas)
                    .build());

            Producto aguaMineral500ml = productoRepo.guardar(Producto.builder()
                    .nombre("Agua Mineral 500ml")
                    .precio(1000.0)
                    .descripcion("Agua mineral sin gas 500ml")
                    .stock(300)
                    .imagen("https://agua_mineral_500ml.jpg")
                    .disponible(true)
                    .categoria(bebidas)
                    .build());

            Producto cervezaArtesanal473ml = productoRepo.guardar(Producto.builder()
                    .nombre("Cerveza Artesanal 473ml")
                    .precio(2500.0)
                    .descripcion("Cerveza artesanal rubia 473ml")
                    .stock(100)
                    .imagen("https://cerveza_artesanal_473ml.jpg")
                    .disponible(true)
                    .categoria(bebidas)
                    .build());

// Papas Fritas
            Producto papasFritasClasicas = productoRepo.guardar(Producto.builder()
                    .nombre("Papas Fritas Clásicas")
                    .precio(2500.0)
                    .descripcion("Papas fritas bastón crocantes con sal parrillera")
                    .stock(70)
                    .imagen("https://papas_fritas_clasicas.jpg")
                    .disponible(true)
                    .categoria(papas)
                    .build());

            Producto papasCheddarPanceta = productoRepo.guardar(Producto.builder()
                    .nombre("Papas con Cheddar y Panceta")
                    .precio(3500.0)
                    .descripcion("Papas fritas bañadas en cheddar y panceta crocante")
                    .stock(50)
                    .imagen("https://papas_cheddar_panceta.jpg")
                    .disponible(true)
                    .categoria(papas)
                    .build());

            Producto arosCebolla = productoRepo.guardar(Producto.builder()
                    .nombre("Aros de Cebolla")
                    .precio(2800.0)
                    .descripcion("Aros de cebolla empanizados, servidos con salsa barbacoa")
                    .stock(40)
                    .imagen("https://aros_cebolla.jpg")
                    .disponible(true)
                    .categoria(papas)
                    .build());

            // ============================
            // 3. USUARIOS
            // ============================
          Usuario admin = usuarioRepo.guardar(Usuario.builder()
                    .nombre("Admin")
                    .apellido("Sistema")
                    .mail("admin@admin.com")
                    .celular("1145678901")
                    .contrasenia("123456")
                    .rol(Rol.ADMIN)
                    .build());

           Usuario juan = usuarioRepo.guardar(Usuario.builder()
                    .nombre("Juan")
                    .apellido("Pérez")
                    .mail("cliente@food.com")
                    .celular("1198765432")
                    .contrasenia("cliente123")
                    .rol(Rol.USUARIO)
                    .build());

                // ============================
                // PEDIDOS (Food Store)
                // ============================

        // Pedido 1
            Pedido pedido1 = Pedido.builder()
                    .fecha(LocalDate.of(2026, 6, 8))
                    .estado(Estado.TERMINADO)
                    .total(9600.0)
                    .formapago(FormaPago.TARJETA)
                    .usuario(juan) // usuario cargado previamente
                    .build();

            DetallePedido det1a = DetallePedido.builder()
                    .cantidad(2)
                    .subtotal(9000.0)
                    .producto(hamburguesaClasica) // producto cargado previamente
                    .pedido(pedido1)
                    .build();

            DetallePedido det1b = DetallePedido.builder()
                    .cantidad(2)
                    .subtotal(600.0)
                    .producto(aguaMineral500ml) // producto cargado previamente
                    .pedido(pedido1)
                    .build();

            pedido1.getDetallePedidos().add(det1a);
            pedido1.getDetallePedidos().add(det1b);
            pedidoRepo.guardar(pedido1);

        // Pedido 2
            Pedido pedido2 = Pedido.builder()
                    .fecha(LocalDate.of(2026, 6, 9))
                    .estado(Estado.PENDIENTE)
                    .total(16400.0)
                    .formapago(FormaPago.EFECTIVO)
                    .usuario(juan)
                    .build();

            DetallePedido det2a = DetallePedido.builder()
                    .cantidad(1)
                    .subtotal(6200.0)
                    .producto(hamburguesaDoble)
                    .pedido(pedido2)
                    .build();

            DetallePedido det2b = DetallePedido.builder()
                    .cantidad(1)
                    .subtotal(5200.0)
                    .producto(pizzaEspecial)
                    .pedido(pedido2)
                    .build();

            DetallePedido det2c = DetallePedido.builder()
                    .cantidad(2)
                    .subtotal(5000.0)
                    .producto(papasFritasClasicas)
                    .pedido(pedido2)
                    .build();

            pedido2.getDetallePedidos().add(det2a);
            pedido2.getDetallePedidos().add(det2b);
            pedido2.getDetallePedidos().add(det2c);
            pedidoRepo.guardar(pedido2);

        // Pedido 3
            Pedido pedido3 = Pedido.builder()
                    .fecha(LocalDate.of(2026, 6, 10))
                    .estado(Estado.PENDIENTE)
                    .total(6100.0)
                    .formapago(FormaPago.TRANSFERENCIA)
                    .usuario(juan)
                    .build();

            DetallePedido det3a = DetallePedido.builder()
                    .cantidad(3)
                    .subtotal(3600.0)
                    .producto(empanadaCarne)
                    .pedido(pedido3)
                    .build();

            DetallePedido det3b = DetallePedido.builder()
                    .cantidad(1)
                    .subtotal(2500.0)
                    .producto(cervezaArtesanal473ml)
                    .pedido(pedido3)
                    .build();

            pedido3.getDetallePedidos().add(det3a);
            pedido3.getDetallePedidos().add(det3b);
            pedidoRepo.guardar(pedido3);

        }


    }