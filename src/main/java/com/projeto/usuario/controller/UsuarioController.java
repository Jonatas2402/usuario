package com.projeto.usuario.controller;

import com.projeto.usuario.business.UsuarioService;
import com.projeto.usuario.infraestructure.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> salvaUsuario(@RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Usuario> buscaPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @PutMapping
    public ResponseEntity<Object> atualizaUsuario(@RequestParam("email") String email, @RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.atualizaUsuario(email, usuario));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletarPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
