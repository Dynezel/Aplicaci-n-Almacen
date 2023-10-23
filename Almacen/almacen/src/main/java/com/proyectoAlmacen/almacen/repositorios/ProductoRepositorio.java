package com.proyectoAlmacen.almacen.repositorios;

import com.proyectoAlmacen.almacen.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContaining(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.fechaModificacion = :fechaModificacion")
    List<Producto> findByFechaModificacion(@Param("fechaModificacion") Date fechaModificacion);
}
