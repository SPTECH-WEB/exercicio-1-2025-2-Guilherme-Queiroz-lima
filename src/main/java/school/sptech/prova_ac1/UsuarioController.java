package school.sptech.prova_ac1;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {

        List<Usuario> usuariosCadastrados = usuarioRepository.findAll();

        if (usuariosCadastrados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuariosCadastrados);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();


    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {

        Usuario usuarioComIdIgual = usuarioRepository.findById(id).orElse(null);

        if (usuarioComIdIgual == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioComIdIgual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {


        Usuario usuarioComIdIgual = usuarioRepository.findById(id).orElse(null);
        if (usuarioComIdIgual == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.delete(usuarioComIdIgual);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam("nascimento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nascimento) {

        List<Usuario> usuariosComMesmaData = usuarioRepository.findByDataNascimentoAfter(nascimento);

        if (usuariosComMesmaData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuariosComMesmaData);


    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario
    ) {

        Optional<Usuario> usuarioExistenteOpt = usuarioRepository.findById(id);

        if (usuarioExistenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioExistente = usuarioExistenteOpt.get();


        if (usuarioRepository.existsByCpfAndIdNot(usuario.getCpf(), id) ||
                usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioExistente.setCpf(usuario.getCpf());
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setDataNascimento(usuario.getDataNascimento());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setSenha(usuario.getSenha());

        usuarioRepository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioExistente);
    }


}
