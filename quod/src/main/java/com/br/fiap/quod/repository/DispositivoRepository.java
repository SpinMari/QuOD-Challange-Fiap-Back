//package com.br.fiap.quod.repository;
//
//import com.br.fiap.quod.domain.Dispositivo;
//
//
//import java.util.List;
//
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//public interface DispositivoRepository extends MongoRepository<Dispositivo, String> {
//    List<Dispositivo> findByModelo(String modelo);
//    List<Dispositivo> findByFabricante(String fabricante);
//    List<Dispositivo> findByStatus(String status);
//    List<Dispositivo> findByFabricanteAndModelo(String fabricante, String modelo);
//}
//
