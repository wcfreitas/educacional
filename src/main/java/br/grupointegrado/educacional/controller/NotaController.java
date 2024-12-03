package br.grupointegrado.educacional.controller;

import br.grupointegrado.educacional.dto.NotaRequestDTO;
import br.grupointegrado.educacional.dto.NotaUpdateDTO;
import br.grupointegrado.educacional.model.Nota;
import br.grupointegrado.educacional.repository.NotaRepository;
import br.grupointegrado.educacional.repository.MatriculaRepository;
import br.grupointegrado.educacional.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    // Cadastrar uma nova nota
    @PostMapping
    public ResponseEntity<?> cadastrarNota(@RequestBody NotaRequestDTO dto) {
        var matricula = matriculaRepository.findById(dto.matriculaId());
        var disciplina = disciplinaRepository.findById(dto.disciplinaId());

        if (matricula.isEmpty()) {
            return ResponseEntity.badRequest().body("Matrícula não encontrada.");
        }

        if (disciplina.isEmpty()) {
            return ResponseEntity.badRequest().body("Disciplina não encontrada.");
        }

        Nota nota = new Nota();
        nota.setMatricula(matricula.get());
        nota.setDisciplina(disciplina.get());
        nota.setNota(BigDecimal.valueOf(dto.nota()));
        nota.setDataLancamento(LocalDate.now());

        notaRepository.save(nota);
        return ResponseEntity.ok("Nota cadastrada com sucesso!");
    }

    // Atualizar uma nota existente
    @PutMapping("/{notaId}")
    public ResponseEntity<?> atualizarNota(@PathVariable Integer notaId, @RequestBody NotaUpdateDTO dto) {
        Optional<Nota> notaOptional = notaRepository.findById(notaId);

        if (notaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Nota não encontrada.");
        }

        Nota nota = notaOptional.get();
        nota.setNota(BigDecimal.valueOf(dto.nota()));
        notaRepository.save(nota);

        return ResponseEntity.ok("Nota atualizada com sucesso!");
    }
}
