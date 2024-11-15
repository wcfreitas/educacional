CREATE TABLE notas (
    id INT PRIMARY KEY,
    matricula_id INT,
    disciplina_id INT,
    nota DECIMAL(5, 2),
    data_lancamento DATE,
    FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);
