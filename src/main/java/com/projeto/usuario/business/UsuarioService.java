package com.projeto.usuario.business;

import com.projeto.usuario.business.DTO.UsuarioDTO;
import com.projeto.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.infraestructure.entity.Usuario;
import com.projeto.usuario.infraestructure.exceptions.ConflitException;
import com.projeto.usuario.infraestructure.exceptions.ResourceNotFoundException;
import com.projeto.usuario.infraestructure.repository.UsuarioRepository;
import com.projeto.usuario.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        /*Converte de usuario dto para usuario entity*/
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExiste(email);
            if (existe) {
                throw new ConflitException("Email já cadastrado.");
            }
        } catch (ConflitException e) {
            throw new ConflitException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Email não encontrado " + email));
    }

    public void deletarPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
    public UsuarioDTO atualizaDadosDeUsuario(String token, UsuarioDTO usuarioDTO){
        /*Buscamos o email do usuario através do token JWT(Tiradno a obrigatoriedade
        * do email*/
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        /*Criptografia de senha*/
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()): null);
        /*Faz uma busca pelo usuário no banco de dados*/
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não localizado."));

        /*Aqui foi mesclado os dados recebidos na requisição os dados DTO
        * com os dados do banco de dados */
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        /*Salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO*/
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
