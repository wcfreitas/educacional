package br.grupointegrado.educacional.controller;

import br.grupointegrado.educacional.model.Matricula;
import br.grupointegrado.educacional.model.Nota;
import br.grupointegrado.educacional.repository.MatriculaRepository;
import br.grupointegrado.educacional.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    // Relatório de boletim por aluno
    @GetMapping("/boletim/aluno/{alunoId}")
    public ResponseEntity<?> boletimPorAluno(@PathVariable Integer alunoId) {
        List<Matricula> matriculas = matriculaRepository.findByAlunoId(alunoId);
        if (matriculas.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma matrícula encontrada para o aluno.");
        }

        var resultado = matriculas.stream().map(matricula -> {
            var notas = notaRepository.findByMatriculaId(matricula.getId());
            return new BoletimDTO(matricula, notas);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // DTO para encapsular os dados do boletim
    public static record BoletimDTO(Matricula matricula, List<Nota> notas) {
    }

    // Relatório de desempenho por turma
    @GetMapping("/desempenho/turma/{turmaId}")
    public ResponseEntity<?> desempenhoPorTurma(@PathVariable Integer turmaId) {
        List<Matricula> matriculas = matriculaRepository.findByTurmaId(turmaId);
        if (matriculas.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma matrícula encontrada para a turma.");
        }

        var resultado = matriculas.stream().map(matricula -> {
            var aluno = matricula.getAluno();
            var notas = notaRepository.findByMatriculaId(matricula.getId());
            return new DesempenhoAlunoDTO(aluno.getNome(), aluno.getMatricula(), notas);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // DTO para encapsular os dados de desempenho por aluno
    public static record DesempenhoAlunoDTO(String nomeAluno, String matricula, List<Nota> notas) {
    }

    // Relatório de desempenho por disciplina
    @GetMapping("/desempenho/disciplina/{disciplinaId}")
    public ResponseEntity<?> desempenhoPorDisciplina(@PathVariable Integer disciplinaId) {
        List<Nota> notas = notaRepository.findByDisciplinaId(disciplinaId);
        if (notas.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma nota encontrada para a disciplina.");
        }

        var resultado = notas.stream().map(nota -> {
            var aluno = nota.getMatricula().getAluno();
            return new DesempenhoDisciplinaDTO(aluno.getNome(), aluno.getMatricula(), nota.getNota());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // DTO para encapsular os dados de desempenho por disciplina
    public static record DesempenhoDisciplinaDTO(String nomeAluno, String matricula, java.math.BigDecimal nota) {
    }

}
