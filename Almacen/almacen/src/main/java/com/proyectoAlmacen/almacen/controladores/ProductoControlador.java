package com.proyectoAlmacen.almacen.controladores;

import com.proyectoAlmacen.almacen.entidades.Producto;
import com.proyectoAlmacen.almacen.excepciones.Excepcion;
import com.proyectoAlmacen.almacen.servicios.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
public class ProductoControlador {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductoControlador.class);
    @Autowired
    private ProductoServicio productoServicio;



    @GetMapping("/")
    public String index(ModelMap modelo) {

        List<Producto> producto = productoServicio.listarProductos();
        modelo.addAttribute("producto", producto);

        return "index.html";
    }


    @GetMapping("/lista/Noticias")
    public String obtenerNoticias(ModelMap modelo) {

        List<Producto> producto = productoServicio.listarProductos();
        modelo.addAttribute("producto", producto);

        return "noticia_lista.html";
    }




    @GetMapping("/publicar")
    public String mostrarFormularioProducto(ModelMap modelo) {
        Producto producto = new Producto();  // Crea una instancia de Producto
        modelo.addAttribute("producto", producto);  // Agrega el producto al modelo con el nombre "producto"
        return "noticia_form";
    }

    @PostMapping("/crear")
    public String procesarFormulario(@ModelAttribute("producto") Producto producto, ModelMap modelo) {
        try {
            System.out.println("Tipo: " + producto.getTipo());
            productoServicio.guardarProducto(producto.getTipo(), producto.getNombre(), producto.getMarca(), producto.getPrecio());
            modelo.put("exito", "El producto se cargó correctamente!");
        } catch (Excepcion ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/crear";
        }
        return "redirect:/";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("producto", productoServicio.getOne(id));
        return "noticia_modificar";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, String tipo, String nombre, String marca, int precio, ModelMap modelo) {
        try {
            productoServicio.modificarProducto(id, tipo, nombre, marca, precio);

            return "redirect:../";
        } catch (Excepcion ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_modificar.html";
        }
    }




}

//eliminar
//@GetMapping("/{id}")
//public String eliminar(@PathVariable Noticia noticia, ModelMap modelo) throws Excepcion{
//noticiaServicio.eliminarNoticia(noticia);

// return "autor_eliminar.html";
//}

