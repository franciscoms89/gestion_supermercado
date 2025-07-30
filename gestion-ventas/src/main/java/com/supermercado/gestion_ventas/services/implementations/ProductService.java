package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductInterfaz {

    @Autowired
    ProductRepositoryInterfaz repository;


    @Override
    public List<ProductDTO> listAll() {//listar producto
        List<Product> productList = repository.findAll();
        if (productList.isEmpty()) {
            System.out.println("INFO: No hay productos registrados en la base de datos.");
        }
        return productList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO create(ProductDTO p) { //crear producto
        try {
            Product productRecover = this.convertToOBJ(p);
            Product productSave = repository.save(productRecover);
            System.out.println("INFO: Producto creado con éxito con ID: " + productSave.getId());
            return this.convertToDTO(productSave);
        }catch (Exception e){
            System.err.println("ERROR: Error al intentar registrar el producto '" + p.getName() + "'. Causa: " + e.getMessage());
            throw new RuntimeException("Error inesperado al registrar el producto.");
        }
    }

    @Override
    public ProductDTO update(Long id, ProductDTO p) {         // actualizar Produto

        // 1. Buscar el producto por ID. Si no existe, lanzar ProductNotFoundException.
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no econtrado para actualizar."));

        // 2. Actualizar las propiedades del producto existente.
        existingProduct.setName(p.getName());
        existingProduct.setPrice(p.getPrice());
        existingProduct.setCategory(p.getCategory());

        // 3. Guardar el producto actualizado
        Product updateProduct = repository.save(existingProduct);
        System.out.println("INFO: Producto con ID " + updateProduct.getId() + " actualizado correctamente.");
        return convertToDTO (updateProduct);
    }

    @Override
    public void delete(Long id) {       //borrar producto

        // Verifica si el producto existe antes de intentar borrarlo.
        if (!repository.existsById(id)) {
            // Si no existe, lanza la excepción ProductNotFoundException
            throw new ProductNotFoundException("Producto con ID " + id + " no encontrado para eliminar.");
        }
        try {
            repository.deleteById(id);
            System.out.println("INFO: Producto con ID " + id + " eliminado correctamente.");
        }catch (Exception e){
            System.err.println("ERROR: Error al intentar eliminar el producto con ID: " + id + ". Causa: " + e.getMessage());
            throw new RuntimeException("Error inesperado al eliminar el producto.");
        }

    }

    /// Todo: mapear objectos
    @Override
    public ProductDTO convertToDTO(Product p) {         //metodos para mapear OBJ a DTO
        return new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getCategory());
    }

    @Override
    public Product convertToOBJ(ProductDTO p) {            //metodos para mapear DTO a OBJ
        Product product = new Product();

        if (p.getId() != null) {
            product.setId(p.getId());
        }
        product.setName(p.getName());
        product.setPrice(p.getPrice());
        product.setCategory(p.getCategory());
        return product;
    }
}
