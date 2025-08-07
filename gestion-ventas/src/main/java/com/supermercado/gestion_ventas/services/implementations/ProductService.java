package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.exceptions.ServiceException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductInterfaz {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepositoryInterfaz repositoryProduct;

    @Autowired
    SaleProductRepositoryInterfaz repositorySaleProduct;

    @Override
    public List<ProductDTO> listAll() {//listar producto
        List<Product> productList = repositoryProduct.findAll();
        if (productList.isEmpty()) {
            log.info("INFO: No hay productos registrados en la base de datos.");
        }
        return productList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ProductDTO productTopSelling() {       //producto más vendido
        List<SaleProduct> product = repositorySaleProduct.findAll();
        Optional<SaleProduct> saleProductTop = product.stream()
                .max(Comparator.comparing(SaleProduct::getQuantity));
        if (saleProductTop.isEmpty()) {
            throw new ProductNotFoundException("INFO: No hay ventas registradas.");
        }
        Product productTop = saleProductTop.get().getProduct();
        if (productTop == null) {
            throw new ProductNotFoundException("INFO: El producto más vendido no está asociado correctamente.");
        }
        return convertToDTO(productTop);
    }

    @Override
    public ProductDTO create(ProductDTO p) { //crear producto
        try {
            Product productRecover = this.convertToOBJ(p);
            Product productSave = repositoryProduct.save(productRecover);
            log.info("Producto creado con éxito con ID: {}", productSave.getId());
            return this.convertToDTO(productSave);
        } catch (Exception e) {
            log.error("Error al intentar registrar el producto '{}'", p.getName(), e);
            throw new ServiceException("Error inesperado al registrar el producto.", e);
        }
    }

    @Override
    public ProductDTO update(Long id, ProductDTO p) {         // actualizar Produto

        // 1. Buscar el producto por ID. Si no existe, lanzar ProductNotFoundException.
        Product existingProduct = repositoryProduct.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no econtrado para actualizar."));

        // 2. Actualizar las propiedades del producto existente.
        existingProduct.setName(p.getName());
        existingProduct.setPrice(p.getPrice());
        existingProduct.setCategory(p.getCategory());

        // 3. Guardar el producto actualizado
        Product updateProduct = repositoryProduct.save(existingProduct);
        log.info("Producto con ID '{}' actualizado correctamente.",updateProduct.getId());
        return convertToDTO(updateProduct);
    }

    @Override
    public void delete(Long id) {       //borrar producto

        // Verifica si el producto existe antes de intentar borrarlo.
        if (!repositoryProduct.existsById(id)) {
            // Si no existe, lanza la excepción ProductNotFoundException
            throw new ProductNotFoundException("Producto con ID " + id + " no encontrado para eliminar.");
        }
        try {
            repositoryProduct.deleteById(id);
            log.info("Producto con ID {} eliminado correctamente.", id);
        } catch (Exception e) {
            // Forma ideal
            log.error("Error al intentar eliminar el producto con ID {}", id, e);
            throw new ServiceException("Error inesperado al eliminar el producto.", e);
        }
    }

    // Mapeos de DTO y OBJ

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