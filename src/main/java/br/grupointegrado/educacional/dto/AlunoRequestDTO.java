package br.grupointegrado.educacional.dto;

import java.time.LocalDate;
import java.util.Date;

public record AlunoRequestDTO(String nome, String email, String matricula, LocalDate data_nascimento) {
}
