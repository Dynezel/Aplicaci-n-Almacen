package com.proyectoAlmacen.almacen.servicios;

import com.proyectoAlmacen.almacen.entidades.Producto;
import com.proyectoAlmacen.almacen.excepciones.Excepcion;
import com.proyectoAlmacen.almacen.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;


    public List<Producto> listarProductos(){
        return productoRepositorio.findAll();
    }


    public Producto buscarProductoPorId(Long id) throws Excepcion {
        Producto producto = productoRepositorio.findById(id).orElse(null);
        return producto;
    }

    @Transactional
    public Producto guardarProducto(String tipo, String nombre, String marca, double precio) throws Excepcion{

        Producto producto = new Producto();

        producto.setTipo(tipo);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setPrecio(precio);
        return productoRepositorio.save(producto);
    }


    public void eliminarProducto(Producto producto) throws Excepcion {
        productoRepositorio.delete(producto);
    }


    public void modificarProducto(Long id, String tipo, String nombre, String marca, double precio) throws Excepcion {
        Optional<Producto> respuesta = productoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();


            producto.setNombre(nombre);

            producto.setMarca(marca);

            producto.setPrecio(precio);


            productoRepositorio.save(producto);

        }
    }

    public Producto getOne(Long id) {
        return productoRepositorio.getOne(id);
    }


}
