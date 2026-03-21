package com.projeto.usuario.infraestructure.repository;

import com.projeto.usuario.infraestructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*Herdando: Não é obrigatorio implementar todos os métodos.
* Implemnetar: Obrigatorio implementar todos os métodos.*/
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    boolean existsByNome(String nome);

    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

}
