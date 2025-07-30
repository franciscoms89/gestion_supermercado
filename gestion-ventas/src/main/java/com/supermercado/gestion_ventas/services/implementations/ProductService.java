package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService  implements ProductInterfaz {

    @Autowired
    ProductRepositoryInterfaz repository;

    @Override
    public List<ProductDTO> listAll() {//listar producto
        List<Product> productList = repository.findAll();
        if(productList.isEmpty())
        {
            new Response("No tienes productos registrados",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }
        return productList.stream().map(this::convertToDTO)
                .toList();
    }

    @Override
    public ProductDTO create(ProductDTO p) {//crear producto
        try{
            Product productRecover = this.convertToOBJ(p);
            Product productSave = repository.save(productRecover);
            return convertToDTO(productSave);

        }catch (Exception e) {
            System.err.println("Error al registrar el producto: " + e.getMessage()); // Para logs
            throw new RuntimeException("No se pudo registrar el producto: " + e.getMessage(), e);
        }


    }

    @Override
    public Response update(Long id, ProductDTO p) {         // actualizar Produto

        // 1. Buscar el producto por ID. Si no existe, lanzar ProductNotFoundException.
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no econtrado para actualizar."));

        // 2. Actualizar las propiedades del producto existente.
        existingProduct.setName(p.getName());
        existingProduct.setPrice(p.getPrice());
        existingProduct.setCategory(p.getCategory());

        // 3. Guardar el producto actualizado
        Product updateProduct = repository.save(existingProduct);
        return new Response("Se actualizó correctamente el producto " + id,
                HttpStatus.ACCEPTED.value(),
        LocalDate.now());
    }

    @Override
    public Response delete(Long id) {       //borrar producto

        // Verifica si el producto existe antes de intentar borrarlo.
        if (!repository.existsById(id)){
            // Si no existe, lanza la excepción ProductNotFoundException
            throw new ProductNotFoundException("Producto con ID " + id + " no encontrado para eliminar.");
        }
        try{
            repository.deleteById(id);
            return new Response("Se eliminó el producto " + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        }catch (Exception e){
            // Para otros errores
            System.err.println("Error al eliminar el producto con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el producto " + id + ": " + e.getMessage(), e);
        }
    }
    ///Todo: mapear objectos
    @Override
    public ProductDTO convertToDTO(Product p) {         //metodos para mapear OBJ a DTO
        return new ProductDTO(p.getId(),p.getName(),p.getPrice(), p.getCategory());
    }

    @Override
    public Product convertToOBJ(ProductDTO p){            //metodos para mapear DTO a OBJ
        Product product = new Product();

        if(p.getId() != null){
            product.setId(p.getId());
        }
        product.setName(p.getName());
        product.setPrice(p.getPrice());
        product.setCategory(p.getCategory());
        return product;
    }
}
