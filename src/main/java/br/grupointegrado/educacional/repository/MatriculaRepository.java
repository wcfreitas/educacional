package br.grupointegrado.educacional.repository;

import br.grupointegrado.educacional.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByTurmaId(Integer turmaId);

    List<Matricula> findByAlunoId(Integer alunoId);

    boolean existsByAlunoIdAndTurmaId(Integer integer, Integer integer1);
}
