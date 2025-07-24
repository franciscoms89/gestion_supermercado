package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
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

    //relaciones con otros servicios
    SaleInterfaz sai;

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
            new Response("Se creo el producto correctamente",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
            return convertToDTO(productSave);

        }catch (Exception e) {
            new Response("No se pudo registrar el producto",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
            throw new RuntimeException(e);
        }


    }

    @Override
    public Response update(Long id, ProductDTO p) {      // actualizar Produto
        Optional<Product> exist = repository.findById(id);
        if(exist.isPresent()) {
            Product product = new Product();
            product.setId(id);
            product.setName(p.getName());
            product.setPrice(p.getPrice());
            product.setCategory(p.getCategory());

            Product productUpdate = repository.save(product);


            return  new Response("Se actualizo correctamente el producto" + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        }
        else {

            new Response("No se pudo actualizar el producto",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }
        return null;
    }

    @Override
    public Response delete(Long id) {             //borrar producto

        try {
            repository.deleteById(id);
            return new Response("se elimino la Compra" + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        } catch (Exception e) {
            return new Response("no se pudo eliminar la compra" + id,
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }


    }


    ///Todo: mapear objectos
    @Override
    public ProductDTO convertToDTO(Product p)          //metodos para mapear OBJ a DTO
    {
        Set<SaleDTO> sales = new HashSet<>();
        return new ProductDTO(p.getId(),p.getName(),p.getPrice(), p.getCategory(),sales);
    }

    @Override
    public Product convertToOBJ(ProductDTO p)             //metodos para mapear DTO a OBJ
    {
        Set<Sale> sales = p.getSales() == null || p.getSales().isEmpty() ? new HashSet<>() : p.getSales().stream().map(sai::convertToOBJ).collect(Collectors.toSet());
        return new Product(p.getId(), p.getName(), p.getPrice(), p.getCategory(), sales);
    }
}
