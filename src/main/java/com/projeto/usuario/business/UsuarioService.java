package com.projeto.usuario.business;

import com.projeto.usuario.infraestructure.entity.Usuario;
import com.projeto.usuario.infraestructure.exceptions.ConflitException;
import com.projeto.usuario.infraestructure.exceptions.ResourceNotFoundException;
import com.projeto.usuario.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvaUsuario(Usuario usuario) {
        try {
            emailExiste(usuario.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioRepository.save(usuario);
        } catch (ConflitException e) {
            throw new ConflitException("Email já cadastrado" + e.getCause());
        }

    }
    /*Método que valida email, e retorna erro caso haja.*/
    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExiste(email);
            if (existe){
                throw new ConflitException("Email já cadastrado.");
            }
        }catch (ConflitException e){
            throw new ConflitException("Email já cadastrado" + e.getCause());
        }
    }

    /*Método que verifica se o email existe*/
    public boolean verificaEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    /*Método que faz a busca do usuário por email*/
    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Email não encontrado " + email));
    }
    /*Método que busca usuário por id*/
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ID não encontrado")
        );
    }

    public void deletarPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
    public Usuario atualizaUsuario(String email, Usuario usuario){
        Usuario user = usuarioRepository.findByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Email não encontrado"));

        user.setNome(usuario.getNome());
        user.setEmail(usuario.getEmail());
        user.setSenha(usuario.getPassword());
        user.setEnderecos(usuario.getEnderecos());
        user.setTelefones(usuario.getTelefones());

        return usuarioRepository.save(user);
    }
}
