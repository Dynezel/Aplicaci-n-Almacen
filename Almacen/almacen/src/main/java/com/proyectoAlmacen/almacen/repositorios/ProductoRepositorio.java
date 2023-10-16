package com.proyectoAlmacen.almacen.repositorios;

import com.proyectoAlmacen.almacen.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
}
