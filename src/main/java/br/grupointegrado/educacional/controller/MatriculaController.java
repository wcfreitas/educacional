package br.grupointegrado.educacional.controller;

import br.grupointegrado.educacional.dto.MatriculaRequestDTO;
import br.grupointegrado.educacional.model.Matricula;
import br.grupointegrado.educacional.repository.AlunoRepository;
import br.grupointegrado.educacional.repository.MatriculaRepository;
import br.grupointegrado.educacional.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    // Inscrição de aluno em uma turma
    @PostMapping
    public ResponseEntity<?> inscreverAluno(@RequestBody MatriculaRequestDTO dto) {
        var aluno = alunoRepository.findById(dto.alunoId());
        var turma = turmaRepository.findById(dto.turmaId());

        if (aluno.isEmpty()) {
            return ResponseEntity.badRequest().body("Aluno não encontrado.");
        }

        if (turma.isEmpty()) {
            return ResponseEntity.badRequest().body("Turma não encontrada.");
        }

        // Verificar se o aluno já está matriculado na turma
        if (matriculaRepository.existsByAlunoIdAndTurmaId(dto.alunoId(), dto.turmaId())) {
            return ResponseEntity.badRequest().body("Aluno já está matriculado nesta turma.");
        }

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno.get());
        matricula.setTurma(turma.get());

        matriculaRepository.save(matricula);
        return ResponseEntity.ok("Aluno matriculado com sucesso!");
    }

    // Listar matrículas de um aluno
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<?> listarPorAluno(@PathVariable Integer alunoId) {
        var matriculas = matriculaRepository.findByAlunoId(alunoId);
        if (matriculas.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma matrícula encontrada para o aluno.");
        }
        return ResponseEntity.ok(matriculas);
    }
}
