package com.tp.jpa.util;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;

public class DataLoader {

    public static void load(CategoriaRepository categoriaRepo, ProductoRepository productoRepo) {

        //ESTA CLASE ES SOLO PARA PRUEBAS DEL CODIGO. CARGA CATEGORIAS Y PRODUCTOS SI LA BASE SE ENCUENTRA VACIA.
        // Si ya existen categorías o productos cargados, no carga nada
        boolean hayCategorias = !categoriaRepo.listarActivos().isEmpty() ||
                !categoriaRepo.listarInactivos().isEmpty();

        boolean hayProductos = !productoRepo.listarActivos().isEmpty() ||
                !productoRepo.listarInactivos().isEmpty();

        if (hayCategorias || hayProductos) {
            return;
        }

            System.out.println("Cargando datos iniciales de hardware...");

        // ============================
        // ====== 1. CATEGORÍAS =======
        // ============================
        Categoria micro = Categoria.builder()
                .nombre("Microprocesadores")
                .descripcion("CPUs Intel y AMD")
                .build();

        Categoria mother = Categoria.builder()
                .nombre("Motherboards")
                .descripcion("Placas madre Intel y AMD")
                .build();

        Categoria memorias = Categoria.builder()
                .nombre("Memorias RAM")
                .descripcion("DDR4 y DDR5")
                .build();

        Categoria perifericos = Categoria.builder()
                .nombre("Periféricos")
                .descripcion("Teclados, mouse, auriculares")
                .build();

        Categoria fuentes = Categoria.builder()
                .nombre("Fuentes de alimentación")
                .descripcion("PSU 500W a 1000W")
                .build();

        micro = categoriaRepo.guardar(micro);
        mother = categoriaRepo.guardar(mother);
        memorias = categoriaRepo.guardar(memorias);
        perifericos = categoriaRepo.guardar(perifericos);
        fuentes = categoriaRepo.guardar(fuentes);

        // ============================
        // 2. PRODUCTOS
        // ============================

        // MICROPROCESADORES
        productoRepo.guardar(Producto.builder().nombre("Intel i3").descripcion("4 núcleos, 8 hilos").precio(95000.0).stock(15).disponible(true).categoria(micro).build());
        productoRepo.guardar(Producto.builder().nombre("Intel i5").descripcion("6 núcleos, 12 hilos").precio(150000.0).stock(10).disponible(true).categoria(micro).build());
        productoRepo.guardar(Producto.builder().nombre("Intel i7").descripcion("12 núcleos, 20 hilos").precio(280000.0).stock(8).disponible(true).categoria(micro).build());
        productoRepo.guardar(Producto.builder().nombre("Ryzen 5").descripcion("6 núcleos, 12 hilos").precio(130000.0).stock(12).disponible(true).categoria(micro).build());
        productoRepo.guardar(Producto.builder().nombre("Ryzen 7").descripcion("8 núcleos, 16 hilos").precio(210000.0).stock(7).disponible(true).categoria(micro).build());

        // MOTHERBOARDS
        productoRepo.guardar(Producto.builder().nombre("ASUS Prime").descripcion("Intel 12va gen").precio(120000.0).stock(10).disponible(true).categoria(mother).build());
        productoRepo.guardar(Producto.builder().nombre("Gigabyte B550M").descripcion("AM4 Ryzen").precio(110000.0).stock(14).disponible(true).categoria(mother).build());
        productoRepo.guardar(Producto.builder().nombre("MSI B450").descripcion("AM4 Ryzen").precio(105000.0).stock(9).disponible(true).categoria(mother).build());
        productoRepo.guardar(Producto.builder().nombre("ASRock Z690").descripcion("Intel 12va gen").precio(220000.0).stock(5).disponible(true).categoria(mother).build());
        productoRepo.guardar(Producto.builder().nombre("ASUS ROG").descripcion("AM4 Gaming").precio(180000.0).stock(6).disponible(true).categoria(mother).build());

        // MEMORIAS RAM
        productoRepo.guardar(Producto.builder().nombre("Corsair 8GB").descripcion("3200MHz").precio(25000.0).stock(20).disponible(true).categoria(memorias).build());
        productoRepo.guardar(Producto.builder().nombre("Corsair 16GB").descripcion("3200MHz").precio(45000.0).stock(18).disponible(true).categoria(memorias).build());
        productoRepo.guardar(Producto.builder().nombre("HyperX 16GB").descripcion("3600MHz").precio(50000.0).stock(15).disponible(true).categoria(memorias).build());
        productoRepo.guardar(Producto.builder().nombre("Kingston 8GB").descripcion("5200MHz").precio(60000.0).stock(10).disponible(true).categoria(memorias).build());
        productoRepo.guardar(Producto.builder().nombre("Kingston 16GB").descripcion("5600MHz").precio(90000.0).stock(8).disponible(true).categoria(memorias).build());

        // PERIFÉRICOS
        productoRepo.guardar(Producto.builder().nombre("Logitech G203").descripcion("Mouse gamer RGB").precio(15000.0).stock(25).disponible(true).categoria(perifericos).build());
        productoRepo.guardar(Producto.builder().nombre("Redragon K552").descripcion("Teclado mecánico").precio(30000.0).stock(20).disponible(true).categoria(perifericos).build());
        productoRepo.guardar(Producto.builder().nombre("HyperX Cloud").descripcion("Auriculares gamer").precio(35000.0).stock(12).disponible(true).categoria(perifericos).build());
        productoRepo.guardar(Producto.builder().nombre("Logitech C270").descripcion("Webcam HD").precio(20000.0).stock(10).disponible(true).categoria(perifericos).build());
        productoRepo.guardar(Producto.builder().nombre("Redragon M607").descripcion("Mouse gamer 7200 DPI").precio(12000.0).stock(18).disponible(true).categoria(perifericos).build());

        // FUENTES
        productoRepo.guardar(Producto.builder().nombre("Corsair CV550").descripcion("550W 80+ Bronze").precio(35000.0).stock(10).disponible(true).categoria(fuentes).build());
        productoRepo.guardar(Producto.builder().nombre("EVGA 600 BR").descripcion("600W 80+ Bronze").precio(40000.0).stock(8).disponible(true).categoria(fuentes).build());
        productoRepo.guardar(Producto.builder().nombre("Gigabyte P650B").descripcion("650W 80+ Bronze").precio(45000.0).stock(7).disponible(true).categoria(fuentes).build());
        productoRepo.guardar(Producto.builder().nombre("Thermaltake 700W").descripcion("700W 80+ White").precio(50000.0).stock(6).disponible(true).categoria(fuentes).build());
        productoRepo.guardar(Producto.builder().nombre("Corsair RM750").descripcion("750W 80+ Gold").precio(80000.0).stock(5).disponible(true).categoria(fuentes).build());

        System.out.println("Datos iniciales de hardware cargados correctamente.");

    }
}
