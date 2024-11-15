CREATE TABLE turmas (
    id INT PRIMARY KEY,
    ano INT,
    semestre INT,
    curso_id INT,
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);
