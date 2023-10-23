package com.proyectoAlmacen.almacen.controladores;

import com.proyectoAlmacen.almacen.entidades.Producto;
import com.proyectoAlmacen.almacen.excepciones.Excepcion;
import com.proyectoAlmacen.almacen.repositorios.ProductoRepositorio;
import com.proyectoAlmacen.almacen.servicios.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;



import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductoControlador {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductoControlador.class);
    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private ProductoRepositorio productoRepositorio;


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

    @GetMapping("/buscarNombre")
    public String buscarProductosPorNombre(@RequestParam("nombre") String nombre, Model model) {
        List<Producto> productosEncontrados = productoRepositorio.findByNombreContaining(nombre);
        model.addAttribute("productos", productosEncontrados);
        return "producto_resultados";
    }

    @GetMapping("/buscarFecha")
    public String buscarPorFechaModificacion(@RequestParam("fechaModificacion") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaModificacion, Model model) {
        List<Producto> productosEncontrados = productoServicio.buscarPorFechaModificacion(fechaModificacion);
        model.addAttribute("productos", productosEncontrados);
        return "producto_resultados";
    }

    @GetMapping("/publicar")
    public String mostrarFormularioProducto(ModelMap modelo) {
        Producto producto = new Producto(); // Crea una instancia de Producto
        modelo.addAttribute("producto", producto);  // Agrega el producto al modelo con el nombre "producto"
        return "noticia_form";
    }

    @PostMapping("/crear")
    public String procesarFormulario(@ModelAttribute("producto") Producto producto, ModelMap modelo) {
        try {
            String tipo = producto.getTipo();
            String nombre = producto.getNombre();
            String marca = producto.getMarca();
            double precio = producto.getPrecio();
            int cantidad = producto.getCantidad();
            Optional<Double> peso = Optional.of(producto.getPeso());
            System.out.println("Tipo: " + producto.getTipo());
            switch (tipo) {
                case "mercaderia" : precio = precio * 1.3;
                case "fiambre" : precio = precio * 1.5;
                case "precioPorKilo" : {
                    Double divisor = 1000 / producto.getPeso();
                    Double multiplicacion = divisor * producto.getPeso();
                    Double precioPorKilo = (producto.getPrecio() * 1000) / producto.getPeso();
                    precio = precioPorKilo * 1.3;
                }
            if (cantidad > 1) {
                precio = (producto.getPrecio() / producto.getCantidad()) * 1.3;
            }
            }
            productoServicio.guardarProducto(tipo, nombre, marca, precio);
            modelo.put("exito", "El producto se cargó correctamente!");
        } catch (Excepcion ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_form";
        }
        return "redirect:/";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("producto", productoServicio.getOne(id));
        return "noticia_modificar";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, @ModelAttribute("producto") Producto producto, ModelMap modelo) {
        try {
            Producto productoExistente = productoServicio.getOne(id);
            String tipoAjuste = producto.getAjustePrecio();
            int cantidad = productoExistente.getCantidad();

            // Verifica el tipo de ajuste y actualiza el precio en consecuencia
            switch (tipoAjuste) {
                case "mercaderia" : productoExistente.setPrecio(producto.getPrecio() * 1.3);
                case "fiambre" : productoExistente.setPrecio(producto.getPrecio() * 1.5);
                case "precioPorKilo" : {
                    Double divisor = 1000 / producto.getPeso();
                    Double multiplicacion = divisor * producto.getPeso();
                    Double cosa = (producto.getPrecio() * 1000) / producto.getPeso();
                    productoExistente.setPrecio(cosa * 1.3);
                }
                default :
                    // Sin ajuste, usa el precio original
                        productoExistente.setPrecio(producto.getPrecio());
            }
            if (producto.getCantidad() > 1) {
                productoExistente.setPrecio(Math.round((producto.getPrecio() / producto.getCantidad()) * 1.3));
            }

            productoExistente.setTipo(producto.getTipo());
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setMarca(producto.getMarca());

            productoServicio.modificarProducto(id, productoExistente.getTipo(), productoExistente.getNombre(), productoExistente.getMarca(), productoExistente.getPrecio());
         return "redirect:/";
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

