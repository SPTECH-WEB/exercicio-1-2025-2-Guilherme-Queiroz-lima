package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    Usuario findByEmail(String email);
    List<Usuario> findByDataNascimentoAfter(LocalDate dataNascimento);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);



}
