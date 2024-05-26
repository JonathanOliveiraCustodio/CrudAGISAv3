--USE master
--CREATE DATABASE SistemaAGISAV3
--GO
USE SistemaAGISAV3
GO
-- VIEW OK
CREATE VIEW v_listar_cursos AS
SELECT 
    codigo,
    nome,
    cargaHoraria,
    sigla,
    ultimaNotaENADE,
    turno,
    periodo_matricula_inicio,
    periodo_matricula_fim 
FROM 
    curso;
GO

-- VIEW OK
CREATE VIEW v_listar_alunos AS
SELECT a.CPF, a.nome, a.nomeSocial, a.dataNascimento, a.telefoneContato,
a.emailPessoal, a.emailCorporativo, a.dataConclusao2Grau, a.instituicaoConclusao2Grau, 
a.pontuacaoVestibular, a.posicaoVestibular, a.anoIngresso, a.semestreIngresso, 
a.semestreAnoLimiteGraduacao, a.RA, c.codigo AS curso, c.nome AS nomeCurso
FROM aluno a JOIN curso c ON a.curso = c.codigo
GO
-- VIEW OK
CREATE VIEW v_listar_professores AS
SELECT 
    codigo,
    nome,
    titulacao
FROM 
    professor;
GO
-- VIEW OK
CREATE VIEW v_listar_disciplinas AS
SELECT
    d.codigo,
    d.nome,
    d.horasSemanais,
    d.horarioInicio,
    d.semestre,
    d.diaSemana,
    p.codigo AS codigoProfessor,
    c.codigo AS codigoCurso
FROM
    disciplina d
INNER JOIN
    professor p ON d.codigoProfessor = p.codigo
INNER JOIN
    curso c ON d.codigoCurso = c.codigo;
GO

-- VIEW OK
CREATE VIEW v_listar_conteudos AS
SELECT
    c.codigo,
    c.nome,
    c.descricao,
    d.codigo AS codigoDisciplina
FROM
    conteudo c
INNER JOIN
    disciplina d ON c.codigoDisciplina = d.codigo;
GO
-- Function Telefone Ok
CREATE FUNCTION fn_listar_telefones(@cpfAluno CHAR(11))
RETURNS TABLE
AS
RETURN
(
    SELECT 
	a.CPF AS aluno, 
	a.nome, 
	t.numero, 
	t.tipo 
    FROM aluno a 
    JOIN telefone t ON a.CPF = t.aluno 
    WHERE a.CPF = @cpfAluno
);
GO
-- Function Telefone Ok
CREATE FUNCTION fn_consultar_telefone_aluno(
    @cpfAluno CHAR(11),
    @numeroTelefone CHAR(12)
)
RETURNS TABLE
AS
RETURN
(
    SELECT a.CPF AS aluno, 
	a.nome, 
	t.numero, 
	t.tipo 
    FROM aluno a 
    JOIN telefone t ON a.CPF = t.aluno 
    WHERE a.CPF = @cpfAluno AND t.numero = @numeroTelefone
);
GO

CREATE VIEW v_listarCurso AS
SELECT codigo, nome, cargaHoraria, sigla, ultimaNotaENADE, turno,periodo_matricula_inicio,
        periodo_matricula_fim  FROM curso
GO
CREATE VIEW v_periodoMatricula AS
SELECT TOP 1 periodo_matricula_inicio, periodo_matricula_fim FROM curso ORDER BY codigo ASC
GO

CREATE PROCEDURE sp_validatitulacao 
	@titulacao VARCHAR(50),
	@valido BIT OUTPUT
AS
BEGIN
   IF(@titulacao = 'Doutor' OR @titulacao = 'Mestre' OR @titulacao = 'Especialista')
   BEGIN 
       SET @valido =1
   END
   ELSE
   BEGIN 
       SET @valido = 0
   END
END
GO

-- Procedure OK
CREATE PROCEDURE sp_iud_professor 
    @acao CHAR(1), 
    @codigo INT, 
    @nome VARCHAR(100), 
    @titulacao VARCHAR(50),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    DECLARE @tit_valido BIT
    
    IF (@acao = 'I')
    BEGIN
        EXEC sp_validatitulacao @titulacao, @tit_valido OUTPUT
  
        IF @tit_valido = 0
        BEGIN
            SET @saida = 'Titula��o inv�lida'
            RETURN
        END
        
        IF EXISTS (SELECT 1 FROM professor WHERE codigo = @codigo)
        BEGIN
            SET @saida = 'C�digo de professor j� existe.'
            RETURN
        END
        
        INSERT INTO professor VALUES (@codigo, @nome, @titulacao)
        SET @saida = 'Professor inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        EXEC sp_validatitulacao @titulacao, @tit_valido OUTPUT
        
        IF @tit_valido = 0
        BEGIN
            SET @saida = 'Titula��o inv�lida'
            RETURN
        END    
        
        UPDATE professor SET nome = @nome, titulacao = @titulacao WHERE codigo = @codigo
        SET @saida = 'Professor alterado com sucesso'
    END
    ELSE IF (@acao = 'D')     
    BEGIN
        DELETE FROM professor WHERE codigo = @codigo
        SET @saida = 'Professor exclu�do com sucesso'
    END
END
GO
 --Procedure OK
CREATE PROCEDURE sp_iud_disciplina
    @acao CHAR(1),
    @codigo INT,
    @codigoCurso INT,
    @codigoProfessor INT,
    @horasSemanais INT,
    @semestre INT,
    @horarioInicio VARCHAR(10),
    @diaSemana VARCHAR(20),
    @nome VARCHAR(100),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO disciplina (codigo, codigoCurso, codigoProfessor, horasSemanais, semestre, horarioInicio, diaSemana, nome)
        VALUES (@codigo, @codigoCurso, @codigoProfessor, @horasSemanais, @semestre, @horarioInicio, @diaSemana, @nome)
        SET @saida = 'Disciplina inserida com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE disciplina
        SET codigoCurso = @codigoCurso, codigoProfessor = @codigoProfessor, horasSemanais = @horasSemanais, semestre = @semestre,
            horarioInicio = @horarioInicio, diaSemana = @diaSemana, nome = @nome
        WHERE codigo = @codigo
        SET @saida = 'Disciplina alterada com sucesso'
    END
    ELSE IF (@acao = 'D')
    BEGIN
        DELETE FROM disciplina WHERE codigo = @codigo
        
        SET @saida = 'Disciplina exclu�da com sucesso'
    END
    ELSE
    BEGIN
        RAISERROR('Opera��o inv�lida', 16, 1)
        RETURN
    END
END
GO
-- Procedure OK
CREATE PROCEDURE sp_iud_curso 
    @acao CHAR(1), 
    @codigo INT, 
    @nome VARCHAR(100), 
    @cargaHoraria INT,
    @sigla VARCHAR(10),
    @ultimaNotaENADE DECIMAL(5,2),
    @turno VARCHAR(20),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    DECLARE @validoCodigo BIT
    -- Verificar se o c�digo � v�lido
    IF (@codigo >= 0 AND @codigo <= 100)
    BEGIN
        SET @validoCodigo = 1
    END
    ELSE
    BEGIN
        SET @validoCodigo = 0
    END

    IF (@acao = 'I')
    BEGIN
        -- Inserir curso
        IF @validoCodigo = 1
        BEGIN
            INSERT INTO curso (codigo, nome, cargaHoraria, sigla, ultimaNotaENADE, turno) 
            VALUES (@codigo, @nome, @cargaHoraria, @sigla, @ultimaNotaENADE, @turno)
            SET @saida = 'Curso inserido com sucesso'
        END
        ELSE
        BEGIN
            RAISERROR('C�digo de curso inv�lido', 16, 1)
            RETURN
        END
    END
    ELSE IF (@acao = 'U')
    BEGIN
        -- Atualizar curso
        IF @validoCodigo = 1
        BEGIN
            UPDATE curso SET nome = @nome, cargaHoraria = @cargaHoraria, 
            sigla = @sigla, ultimaNotaENADE = @ultimaNotaENADE, turno = @turno 
            WHERE codigo = @codigo
            SET @saida = 'Curso alterado com sucesso'
        END
        ELSE
        BEGIN
            RAISERROR('C�digo de curso inv�lido', 16, 1)
            RETURN
        END
    END
    ELSE BEGIN
    IF (@acao = 'D')
        DELETE FROM curso WHERE codigo = @codigo;
        SET @saida = 'Curso exclu�do com sucesso.';
    END
END
GO
--Procedure OK
CREATE PROCEDURE sp_validaCPF 
    @CPF CHAR(11),
    @valido BIT OUTPUT
AS
BEGIN
    DECLARE @primeiroDigito INT;
    DECLARE @segundoDigito INT;
    DECLARE @i INT;
    DECLARE @soma INT;
    DECLARE @resto INT;
    SET @valido = 0; -- Inicializa como inv�lido por padr�o
    -- Verifica��o se o CPF cont�m apenas d�gitos num�ricos
    IF @CPF NOT LIKE '%[^0-9]%' AND @CPF NOT IN ('00000000000', '11111111111', '22222222222', '33333333333', '44444444444', '55555555555', '66666666666', '77777777777', '88888888888', '99999999999')
    BEGIN
        -- C�lculo do primeiro d�gito verificador
        SET @soma = 0;
        SET @i = 10;
        WHILE @i >= 2
        BEGIN
            SET @soma = @soma + (CAST(SUBSTRING(@CPF, 11 - @i, 1) AS INT) * @i);
            SET @i = @i - 1;
        END;
        SET @resto = @soma % 11;
        SET @primeiroDigito = IIF(@resto < 2, 0, 11 - @resto);

        -- C�lculo do segundo d�gito verificador
        SET @soma = 0;
        SET @i = 11;
        SET @CPF = @CPF + CAST(@primeiroDigito AS NVARCHAR(1));
        WHILE @i >= 2
        BEGIN
            SET @soma = @soma + (CAST(SUBSTRING(@CPF, 12 - @i, 1) AS INT) * @i);
            SET @i = @i - 1;
        END;
        SET @resto = @soma % 11;
        SET @segundoDigito = IIF(@resto < 2, 0, 11 - @resto);

        -- Verifica��o dos d�gitos verificadores
        IF LEN(@CPF) = 11 AND SUBSTRING(@CPF, 10, 1) = CAST(@primeiroDigito AS NVARCHAR(1)) AND SUBSTRING(@CPF, 11, 1) = CAST(@segundoDigito AS NVARCHAR(1))
        BEGIN
            SET @valido = 1; -- CPF v�lido
        END;
    END;
END;
GO
--Procedure OK
CREATE PROCEDURE sp_ValidarIdade 
    @dataNascimento DATE,
    @valido BIT OUTPUT
AS
BEGIN
    SET @valido = 0;
    IF DATEDIFF(YEAR, @dataNascimento, GETDATE()) < 16 
    BEGIN
        SET @valido = 0; -- Idade inv�lida
    END
    ELSE
    BEGIN
        SET @valido = 1; -- Idade v�lida
    END;
END;
GO
--Procedure OK
CREATE PROCEDURE sp_CalcularDataLimiteGraduacao 
    @anoIngresso INT,
    @dataLimiteGraduacao DATE OUTPUT
AS
BEGIN
    SET @dataLimiteGraduacao = DATEADD(YEAR, 5, DATEFROMPARTS(@anoIngresso, 1, 1));
END;
GO
CREATE PROCEDURE sp_GerarRA 
    @AnoIngresso INT,
    @SemestreIngresso INT,
    @RA VARCHAR(10) OUTPUT
AS
BEGIN
    SET @RA = CAST(@AnoIngresso AS VARCHAR(4)) + CAST(@SemestreIngresso AS VARCHAR(2)) + RIGHT('0000' + CAST(CAST(RAND() * 10000 AS INT) AS VARCHAR), 4);
END;
GO
--PROCEDURE OK
CREATE PROCEDURE sp_iud_aluno 
    @acao CHAR(1), 
    @CPF VARCHAR(11), 
    @RA INT OUTPUT,
    @anoIngresso INT,
    @dataConclusao2Grau DATE,
    @dataNascimento DATE,
    @emailCorporativo VARCHAR(100),
    @emailPessoal VARCHAR(100),
    @instituicaoConclusao2Grau VARCHAR(100),
    @nome VARCHAR(100),
    @nomeSocial VARCHAR(100),
    @pontuacaoVestibular DECIMAL(5,2),
    @posicaoVestibular INT,
    @semestreAnoLimiteGraduacao DATE OUTPUT,
    @semestreIngresso INT,
    @telefoneContato VARCHAR(20),
    @curso INT,
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    DECLARE @validoCPF BIT;
    DECLARE @validoIdade BIT;

    -- Verificar se o aluno existe
    IF (UPPER(@acao) <> 'I') AND (NOT EXISTS (SELECT 1 FROM aluno WHERE CPF = @CPF))
    BEGIN
        SET @saida = 'Aluno n�o encontrado.';
        RETURN;
    END;

    -- Validar CPF
    EXEC sp_validaCPF @CPF, @validoCPF OUTPUT;

    IF @validoCPF = 0
    BEGIN
        SET @saida = 'CPF inv�lido.';
        RETURN;
    END;

    -- Validar Idade
    EXEC sp_ValidarIdade @dataNascimento, @validoIdade OUTPUT;

    IF @validoIdade = 0
    BEGIN
        SET @saida = 'Idade inv�lida para ingresso.';
        RETURN;
    END;

    -- Calcular data limite de gradua��o
    EXEC sp_CalcularDataLimiteGraduacao @anoIngresso, @semestreAnoLimiteGraduacao OUTPUT;

    -- Gerar RA
    EXEC sp_GerarRA @anoIngresso, @semestreIngresso, @RA OUTPUT;

    -- Inserir, atualizar ou deletar aluno
    IF (UPPER(@acao) = 'I')
    BEGIN
        INSERT INTO aluno (CPF, RA, anoIngresso, dataConclusao2Grau, dataNascimento, emailCorporativo, emailPessoal, 
            instituicaoConclusao2Grau, nome, nomeSocial, pontuacaoVestibular, posicaoVestibular, semestreAnoLimiteGraduacao, 
            semestreIngresso, telefoneContato, curso)
        VALUES (@CPF, @RA, @anoIngresso, @dataConclusao2Grau, @dataNascimento, @emailCorporativo, @emailPessoal, 
            @instituicaoConclusao2Grau, @nome, @nomeSocial, @pontuacaoVestibular, @posicaoVestibular, @semestreAnoLimiteGraduacao, 
            @semestreIngresso, @telefoneContato, @curso);
        SET @saida = 'Aluno inserido com sucesso.';
    END
    ELSE IF (UPPER(@acao) = 'U')
    BEGIN
        UPDATE aluno SET 
            nome = @nome, 
            nomeSocial = @nomeSocial, 
            dataNascimento = @dataNascimento,
            telefoneContato = @telefoneContato, 
            emailPessoal = @emailPessoal,
            emailCorporativo = @emailCorporativo,
            dataConclusao2Grau = @dataConclusao2Grau, 
            instituicaoConclusao2Grau = @instituicaoConclusao2Grau,
            pontuacaoVestibular = @pontuacaoVestibular, 
            posicaoVestibular = @posicaoVestibular,
            anoIngresso = @anoIngresso, 
            semestreIngresso = @semestreIngresso,
            semestreAnoLimiteGraduacao = @semestreAnoLimiteGraduacao, 
            curso = @curso
        WHERE CPF = @CPF;
        SET @saida = 'Aluno atualizado com sucesso.';
    END
    ELSE IF (UPPER(@acao) = 'D')
    BEGIN
        DELETE FROM aluno WHERE CPF = @CPF;
        SET @saida = 'Aluno deletado com sucesso.';
    END
    ELSE
    BEGIN
        SET @saida = 'Opera��o inv�lida.';
        RETURN;
    END;
END;
GO
-- Porcedure OK
CREATE PROCEDURE sp_iud_conteudo 
 @acao CHAR(1), 
 @codigo INT, 
 @nome VARCHAR(100), 
 @descricao VARCHAR(100),
 @codigoDisciplina INT,
 @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO conteudo (codigo, nome, descricao, codigoDisciplina) 
        VALUES (@codigo, @nome, @descricao, @codigoDisciplina)
        SET @saida = 'Conte�do inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE conteudo 
        SET nome = @nome, descricao = @descricao, codigoDisciplina = @codigoDisciplina
        WHERE codigo = @codigo
        SET @saida = 'Conte�do alterado com sucesso'
    END
    ELSE IF (@acao = 'D')
    BEGIN
        DELETE FROM conteudo WHERE codigo = @codigo
        SET @saida = 'Conte�do exclu�do com sucesso'
    END
    ELSE
    BEGIN
        RAISERROR('Opera��o inv�lida', 16, 1)
        RETURN
    END
END
GO
CREATE PROCEDURE sp_u_periodomatricula 
 @periodo_inicio DATE, 
 @periodo_fim DATE,
 @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@periodo_inicio IS NOT NULL AND @periodo_fim IS NOT NULL)
    BEGIN
        UPDATE curso SET periodo_matricula_inicio = @periodo_inicio, periodo_matricula_fim = @periodo_fim
        SET @saida = 'Per�odo de matr�cula alterado com sucesso'
    END
    ELSE
    BEGIN
        RAISERROR('Datas inv�lidas', 16, 1)
        RETURN
    END
END
GO
CREATE PROCEDURE sp_nova_matricula
	@codigo_aluno	CHAR(11),
	@saida VARCHAR(100) OUTPUT
AS
BEGIN
	DECLARE @semestre INT
	SELECT @semestre = MAX(semestre) FROM matricula WHERE codigoAluno = @codigo_aluno
	IF @semestre IS NULL
	BEGIN
		SET @semestre = 1
	END
	ELSE
	BEGIN
		SET @semestre = @semestre + 1
	END
	DECLARE @codigo INT
	SELECT @codigo = MAX(codigo) FROM matricula
	SET @codigo = @codigo + 1

	INSERT INTO matricula VALUES (@codigo, @codigo_aluno, GETDATE(), @semestre)
END
GO
CREATE PROCEDURE sp_matricular_disciplina
	@codigo_disciplina	INT,
	@codigo_matricula	INT,
	@saida VARCHAR(100) OUTPUT
AS
BEGIN
	DECLARE @horario_inicio INT, @horas_semanais INT, @dia_semana VARCHAR(100)
	SELECT @horario_inicio = (CAST(SUBSTRING(horarioInicio, 1, CHARINDEX(':', horarioInicio) - 1) AS INT) + 1) FROM disciplina WHERE codigo = @codigo_disciplina
	SELECT @horas_semanais = horasSemanais FROM disciplina WHERE codigo = @codigo_disciplina
	SELECT @dia_semana = diaSemana FROM disciplina WHERE codigo = @codigo_disciplina

	DECLARE @conflito INT
	SELECT @conflito = COUNT(d.codigo) FROM matriculaDisciplina m JOIN disciplina d ON m.codigoDisciplina = d.codigo WHERE m.CodigoMatricula = @codigo_matricula AND d.diaSemana = @dia_semana AND
	((CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT) >= @horario_inicio AND (@horario_inicio + @horas_semanais) >= CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT)) OR
	((CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT) + d.horasSemanais) >= @horario_inicio AND (@horario_inicio + @horas_semanais) >= (CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT) + d.horasSemanais)) OR
	(CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT) <= @horario_inicio AND (@horario_inicio + @horas_semanais) <= (CAST(SUBSTRING(d.horarioInicio, 1, CHARINDEX(':', d.horarioInicio) - 1) AS INT) + d.horasSemanais)))
	
	IF @conflito > 0
	BEGIN
		RAISERROR('Um ou mais hor�rios de disciplinas apresentam conflitos', 16, 1)
        RETURN
	END
	ELSE
	BEGIN
		INSERT INTO matriculaDisciplina VALUES (@codigo_matricula, @codigo_disciplina, 'Cursando', 0.0)
	END
END
GO

-- Procedure OK
CREATE PROCEDURE sp_iud_telefone
 @acao CHAR(1), 
 @aluno VARCHAR(100), 
 @numero VARCHAR(100), 
 @tipo VARCHAR(100),
 @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO telefone VALUES (@aluno, @numero, @tipo) 
        SET @saida = 'Telefone inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE telefone 
        SET tipo = @tipo
        WHERE aluno = @aluno AND numero = @numero
        SET @saida = 'Telefone alterado com sucesso'
    END
    ELSE IF (@acao = 'D')
    BEGIN
        DELETE FROM telefone WHERE aluno = @aluno AND numero = @numero
        SET @saida = 'Telefone exclu�do com sucesso'
    END
    ELSE
    BEGIN
        RAISERROR('Opera��o inv�lida', 16, 1)
        RETURN
    END
END
GO
CREATE FUNCTION fn_Lista_Chamada_Disciplina
(
    @codigoDisciplina INT,
    @dataChamada DATE
)
RETURNS TABLE
AS
RETURN
(
    SELECT lc.codigo, 
           lc.codigoMatricula, 
           lc.codigoDisciplina, 
           lc.dataChamada, 
           lc.presenca1, 
           lc.presenca2,
           lc.presenca3,
           lc.presenca4, 
           a.nome AS nomeAluno,
           d.nome AS nomeDisciplina
    FROM listaChamada lc
    JOIN matricula M ON lc.codigoMatricula = m.codigo
    JOIN aluno a ON M.codigoAluno = a.CPF
    JOIN disciplina d ON lc.codigoDisciplina = d.codigo
    WHERE lc.codigoDisciplina = @codigoDisciplina
    AND lc.dataChamada = @dataChamada
);
GO
--SELECT * FROM eliminacoes

--SELECT * FROM fn_Lista_Chamada_Disciplina(1001,'2024-04-02');
CREATE PROCEDURE sp_iud_listaChamada
    @acao CHAR(1),
    @codigo INT,
    @codigoMatricula INT,
    @codigoDisciplina INT,
    @dataChamada DATE,
    @presenca1 INT,
    @presenca2 INT,
    @presenca3 INT,
    @presenca4 INT,
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO listaChamada (codigo, codigoMatricula, codigoDisciplina, dataChamada, presenca1, presenca2, presenca3, presenca4)
        VALUES (@codigo, @codigoMatricula, @codigoDisciplina, @dataChamada, @presenca1, @presenca2, @presenca3, @presenca4)
        SET @saida = 'Registro de chamada inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE listaChamada
        SET codigoMatricula = @codigoMatricula, codigoDisciplina = @codigoDisciplina, dataChamada = @dataChamada,
            presenca1 = @presenca1, presenca2 = @presenca2, presenca3 = @presenca3, presenca4 = @presenca4
        WHERE codigo = @codigo
        SET @saida = 'Registro de chamada alterado com sucesso'
    END
    ELSE
    BEGIN
        RAISERROR('Opera��o inv�lida', 16, 1)
        RETURN
    END
END
GO

CREATE FUNCTION fn_lista_eliminacoes()
RETURNS TABLE
AS
RETURN
(
    SELECT e.codigo,
           e.codigoMatricula,
           e.codigoDisciplina,
           e.dataEliminacao,
           e.status,
           e.nomeInstituicao,
           a.nome AS nomeAluno,
           d.nome AS nomeDisciplina,
           c.nome AS nomeCurso
    FROM eliminacoes e
    JOIN matricula M ON e.codigoMatricula = M.codigo
    JOIN aluno a ON M.codigoAluno = a.CPF
    JOIN disciplina d ON e.codigoDisciplina = d.codigo
    JOIN curso c ON a.curso = c.codigo
    WHERE e.status = 'Em an�lise'
);
GO
--SELECT * FROM fn_lista_eliminacoes();
--SELECT * FROM eliminacoes();
CREATE FUNCTION fn_lista_eliminacoes_por_RA (@RA INT)
RETURNS TABLE
AS
RETURN
(
    SELECT e.codigo,
           e.codigoMatricula,
           e.codigoDisciplina,
           e.dataEliminacao,
           e.status,
           e.nomeInstituicao,
           a.nome AS nomeAluno,
		   a.RA,
           d.nome AS nomeDisciplina,
           c.nome AS nomeCurso
    FROM eliminacoes e
    JOIN matricula M ON e.codigoMatricula = M.codigo
    JOIN aluno a ON M.codigoAluno = a.CPF
    JOIN disciplina d ON e.codigoDisciplina = d.codigo
    JOIN curso c ON a.curso = c.codigo
    WHERE a.RA = @RA
);
GO
--SELECT * FROM fn_lista_eliminacoes_por_RA(20167890);

CREATE FUNCTION fn_buscar_eliminacao (@codigo INT)
RETURNS TABLE
AS
RETURN
(
    SELECT e.codigo,
           e.codigoMatricula,
           e.codigoDisciplina,
           e.dataEliminacao,
           e.status,
           e.nomeInstituicao,
           a.nome AS nomeAluno,
		   a.CPF AS cpfAluno,
           d.nome AS nomeDisciplina,
           c.nome AS nomeCurso,
		   c.codigo AS codigoCurso
    FROM eliminacoes e
    JOIN matricula M ON e.codigoMatricula = M.codigo
    JOIN aluno a ON M.codigoAluno = a.CPF
    JOIN disciplina d ON e.codigoDisciplina = d.codigo
    JOIN curso c ON a.curso = c.codigo
    WHERE e.codigo = @codigo
);
GO
CREATE FUNCTION fn_listar_disciplinas_RA (@RA INT)
RETURNS TABLE
AS
RETURN
(
    SELECT nomeDisciplina, codigoDisciplina, codigoMatricula
    FROM (
        SELECT d.nome AS nomeDisciplina,
               d.codigo AS codigoDisciplina,
               m.codigo AS codigoMatricula,
               ROW_NUMBER() OVER (PARTITION BY d.codigo ORDER BY m.codigo) AS row_num
        FROM aluno a
        INNER JOIN matricula m ON a.CPF = m.codigoAluno
        INNER JOIN curso c ON a.curso = c.codigo
        INNER JOIN disciplina d ON c.codigo = d.codigoCurso
        WHERE a.RA = @RA
    ) AS disciplinas_numero_linha
    WHERE row_num = 1
);


--SELECT * FROM fn_listar_disciplinas_RA(20169012);

--SELECT * FROM matricula

GO
CREATE FUNCTION fn_consultar_aluno_RA (@RA INT)
RETURNS TABLE
AS
RETURN
(
    SELECT a.CPF, 
           a.nome, 
           a.nomeSocial, 
           a.dataNascimento, 
           a.telefoneContato, 
           a.emailPessoal, 
           a.emailCorporativo, 
           a.dataConclusao2Grau, 
           a.instituicaoConclusao2Grau, 
           a.pontuacaoVestibular, 
           a.posicaoVestibular, 
           a.anoIngresso, 
           a.semestreIngresso, 
           a.semestreAnoLimiteGraduacao, 
           a.RA, 
           c.codigo AS codigoCurso, 
           c.nome AS nomeCurso 
    FROM aluno a 
    JOIN curso c ON a.curso = c.codigo 
    WHERE a.RA = @RA
);
GO
CREATE PROCEDURE sp_inserir_eliminacao
    @codigoMatricula INT,
    @codigoDisciplina INT,
    @nomeInstituicao VARCHAR(255),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Inserir a nova elimina��o na tabela
    INSERT INTO eliminacoes (codigoMatricula, codigoDisciplina, dataEliminacao, status, nomeInstituicao)
    VALUES (@codigoMatricula, @codigoDisciplina, GETDATE(), 'Em an�lise', @nomeInstituicao);

    -- Definir a mensagem de sa�da
    SET @saida = 'Elimina��o inserida com sucesso.';
END;
GO

CREATE FUNCTION fn_listar_disciplinas_professor
(
    @codigo_professor INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT d.codigo AS codigoDisciplina, d.nome AS nomeDisciplina, d.horasSemanais, SUBSTRING(d.horarioInicio, 1, 5) AS horarioInicio, d.semestre, d.diaSemana, p.nome AS nomeProfessor, c.nome AS nomeCurso 
    FROM disciplina d 
    JOIN professor p ON d.codigoProfessor = p.codigo 
    JOIN curso c ON d.codigoCurso = c.codigo 
    WHERE p.codigo = @codigo_professor
);
GO
CREATE FUNCTION fn_listar_lista_chamada_datas (@codigoDisciplina INT)
RETURNS TABLE
AS
RETURN
(
    SELECT dataChamada
    FROM listaChamada
    WHERE codigoDisciplina = @codigoDisciplina
    GROUP BY dataChamada
);
GO
CREATE PROCEDURE sp_update_eliminacao
    @acao CHAR(1),
    @codigo INT,
    @codigoMatricula INT,
    @codigoDisciplina INT,
    @dataEliminacao DATE,
    @status VARCHAR(30),
    @nomeInstituicao VARCHAR(255),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'U')
    BEGIN
        UPDATE eliminacoes
        SET codigoMatricula = @codigoMatricula,
            codigoDisciplina = @codigoDisciplina,
            dataEliminacao = @dataEliminacao,
            status = @status,
            nomeInstituicao = @nomeInstituicao
        WHERE codigo = @codigo;

        SET @saida = 'Registro de elimina��o atualizado com sucesso';
    END
    ELSE
    BEGIN
        RAISERROR('Opera��o inv�lida', 16, 1);
        RETURN;
    END
END
GO
CREATE TRIGGER t_matricula_primeiro_semestre ON aluno
FOR INSERT
AS
BEGIN
	DECLARE @codigo_curso INT,
			@cpf CHAR(11),
			@codigo_matricula INT,
			@codigo_disciplina INT
	SELECT @codigo_curso = curso FROM INSERTED
	SELECT @cpf = cpf FROM INSERTED
	SELECT @codigo_matricula = ((SELECT MAX(codigo) FROM matricula) + 1 )
	INSERT INTO matricula VALUES (@codigo_matricula, @cpf, GETDATE(), 1)
	DECLARE c CURSOR FOR SELECT codigo FROM disciplina WHERE semestre = 1 AND codigoCurso = @codigo_curso
	OPEN c
	FETCH NEXT FROM c INTO @codigo_disciplina
	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO matriculaDisciplina VALUES (@codigo_matricula, @codigo_disciplina, 'Cursando', 0.00)
		FETCH NEXT FROM c INTO @codigo_disciplina
	END
	CLOSE c;
    DEALLOCATE c;
END
GO
CREATE FUNCTION fn_cabecalho_historico(@cpf CHAR(11))
RETURNS TABLE
AS
RETURN
(
    SELECT a.RA, a.nome, c.nome AS curso, m.dataMatricula, a.pontuacaoVestibular, a.posicaoVestibular  
	FROM aluno a, curso c, matricula m
	WHERE a.CPF = @cpf AND
	a.CPF = m.codigoAluno AND
	a.curso = c.codigo
);
GO
CREATE FUNCTION fn_corpo_historico(@cpf CHAR(11))
RETURNS TABLE
AS
RETURN
(
	SELECT d.codigo, d.nome, p.nome AS professor, md.notaFinal, 
		SUM(
			CASE WHEN lc.presenca1 = 0 THEN 1 ELSE 0 END +
			CASE WHEN lc.presenca2 = 0 THEN 1 ELSE 0 END +
			CASE WHEN lc.presenca3 = 0 THEN 1 ELSE 0 END +
			CASE WHEN lc.presenca4 = 0 THEN 1 ELSE 0 END
		) AS faltas
	FROM aluno a
	JOIN matricula m ON a.CPF = m.codigoAluno
	JOIN matriculaDisciplina md ON md.CodigoMatricula = m.codigo
	JOIN disciplina d ON d.codigo = md.codigoDisciplina
	JOIN professor p ON p.codigo = d.codigoProfessor
	LEFT JOIN listaChamada lc ON
	(lc.codigoMatricula = m.codigo AND lc.codigoDisciplina = d.codigo)
	WHERE a.CPF = @cpf AND md.situacao = 'Aprovado'
	GROUP BY d.codigo, d.nome, p.nome, md.notaFinal
);
GO
CREATE PROCEDURE sp_nova_chamada
    @codigo_disciplina INT
AS
BEGIN
    DECLARE @matricula INT,
			@codigo INT,
			@numero_aulas INT
	SELECT @numero_aulas = d.horasSemanais FROM disciplina d WHERE d.codigo = @codigo_disciplina
    DECLARE c CURSOR FOR
        SELECT m.codigo
        FROM matriculaDisciplina md, matricula m
        WHERE md.codigoDisciplina = @codigo_disciplina AND m.codigo = md.CodigoMatricula AND md.situacao = 'Cursando'
    OPEN c
    FETCH NEXT FROM c INTO @matricula
    WHILE @@FETCH_STATUS = 0
    BEGIN
		SELECT @codigo = (MAX(codigo) + 1) FROM listaChamada
		IF @codigo IS NULL
		BEGIN
			SET @codigo = 1
		END
        INSERT INTO listaChamada VALUES (@codigo, @matricula, @codigo_disciplina, GETDATE(), 1, 1, (CASE WHEN @numero_aulas > 2 THEN 1 ELSE NULL END), (CASE WHEN @numero_aulas > 3 THEN 1 ELSE NULL END))
        FETCH NEXT FROM c INTO @matricula
    END
    CLOSE c
    DEALLOCATE c
END
GO

INSERT INTO curso (codigo, nome, cargaHoraria, sigla, ultimaNotaENADE, turno, periodo_matricula_inicio, periodo_matricula_fim) 
VALUES 
(1, 'Administra��o de Empresas', 4000, 'ADM', 7.8, 'Matutino', '2024-01-01', '2025-01-01'),
(2, 'Engenharia Civil', 4500, 'ENG CIV', 8.5, 'Vespertino', '2024-01-01', '2025-01-01'),
(3, 'Direito', 4000, 'DIR', 8.2, 'Noturno', '2024-01-01', '2025-01-01'),
(4, 'Medicina', 6000, 'MED', 9.3, 'Integral', '2024-01-01', '2025-01-01'),
(5, 'Ci�ncia da Computa��o', 3600, 'CC', 8.9, 'Matutino', '2024-01-01', '2025-01-01'),
(6, 'Psicologia', 4200, 'PSI', 8.0, 'Vespertino', '2024-01-01', '2025-01-01'),
(7, 'Administra��o P�blica', 3800, 'ADM PUB', 7.5, 'Noturno', '2024-01-01', '2025-01-01'),
(8, 'Engenharia El�trica', 4800, 'ENG ELE', 8.7, 'Integral', '2024-01-01', '2025-01-01'),
(9, 'Gastronomia', 3200, 'GAS', 7.0, 'Matutino', '2024-01-01', '2025-01-01'),
(10, 'Arquitetura e Urbanismo', 4200, 'ARQ', 8.4, 'Vespertino', '2024-01-01', '2025-01-01'),
(11, 'Analise e Desenvolvimento de Sistemas', 4200, 'ADS', 8.4, 'Vespertino', '2024-01-01', '2024-01-01');
GO
INSERT INTO aluno (CPF, nome, nomeSocial, dataNascimento, telefoneContato, emailPessoal, emailCorporativo, dataConclusao2Grau, instituicaoConclusao2Grau, pontuacaoVestibular, posicaoVestibular, anoIngresso, semestreIngresso, semestreAnoLimiteGraduacao, RA, curso)
VALUES
('55312103020', 'Jo�o Silva', 'Jo�o Social', '1998-05-15', '123456789', 'joao@email.com', 'joao@empresa.com', '2016-12-20', 'Escola Estadual ABC', 8.75, 25, 2016, 1, '2020-12-31', 2016456, 1),
('86462326034', 'Maria Santos', 'NULL', '1999-09-22', '987654321', 'maria@email.com', 'maria@empresa.com', '2017-05-10', 'Escola Municipal XYZ', 8.50, 30, 2017, 1, '2021-12-31', 20174567, 2),
('39112829072', 'Jos� Oliveira', 'NULL', '1997-02-10', '987123456', 'jose@email.com', 'jose@empresa.com', '2016-08-30', 'Col�gio Particular QRS', 9.00, 15, 2016, 2, '2020-12-31', 2016378, 1),
('39590327060', 'Ana Souza', 'NULL', '2000-11-05', '654987321', 'ana@email.com', 'ana@empresa.com', '2017-11-28', 'Escola Estadual XYZ', 8.25, 40, 2017, 1, '2021-12-31', 2016789, 2),
('09129892031', 'Pedro Lima', 'NULL', '1996-07-30', '987123654', 'pedro@email.com', 'pedro@empresa.com', '2016-04-12', 'Col�gio Municipal DEF', 8.90, 20, 2016, 2, '2020-12-31', 20167890, 1),
('89125916068', 'Juliana Castro', 'NULL', '1999-03-18', '654321987', 'juliana@email.com', 'juliana@empresa.com', '2017-09-03', 'Col�gio Estadual LMN', 8.80, 10, 2017, 1, '2021-12-31', 2016901, 2),
('97006247063', 'Lucas Almeida', 'NULL', '1998-12-25', '321987654', 'lucas@email.com', 'lucas@empresa.com', '2016-10-05', 'Escola Particular GHI', 8.70, 35, 2016, 2, '2020-12-31', 20169012, 1),
('12697967044', 'Carla Pereira', 'NULL', '2001-04-08', '987321654', 'carla@email.com', 'carla@empresa.com', '2017-12-15', 'Col�gio Municipal OPQ', 8.45, 50, 2017, 1, '2021-12-31', 201690123, 2),
('29180596096', 'Marcos Fernandes', 'NULL', '1997-10-20', '654321789', 'marcos@email.com', 'marcos@empresa.com', '2016-06-18', 'Escola Estadual RST', 8.95, 5, 2016, 1, '2020-12-31', 201634, 1),
('30260403040', 'Aline Rocha', 'NULL', '2000-01-12', '321654987', 'aline@email.com', 'aline@empresa.com', '2017-08-20', 'Col�gio Particular UVW', 8.60, 45, 2017, 2, '2021-12-31', 2017450, 2);
GO
-- Professores
INSERT INTO professor (codigo, nome, titulacao) VALUES
(1, 'Carlos Silva', 'Doutor'),
(2, 'Ana Santos', 'Mestre'),
(3, 'Paulo Oliveira', 'Doutor'),
(4, 'Maria Costa', 'Mestre'),
(5, 'Fernanda Mendes', 'Doutor'),
(6, 'Jos� Pereira', 'Mestre'),
(7, 'Aline Almeida', 'Doutor'),
(8, 'Rafael Ramos', 'Mestre'),
(9, 'Juliana Fernandes', 'Doutor'),
(10, 'Marcos Sousa', 'Mestre'),
(11, 'Larissa Lima', 'Doutor'),
(12, 'Gustavo Gomes', 'Mestre'),
(13, 'Camila Oliveira', 'Doutor'),
(14, 'Pedro Barbosa', 'Mestre'),
(15, 'Patr�cia Martins', 'Doutor'),
(16, 'Lucas Santos', 'Mestre'),
(17, 'Tatiane Costa', 'Doutor'),
(18, 'Felipe Oliveira', 'Mestre'),
(19, 'Amanda Lima', 'Doutor'),
(20, 'Daniel Pereira', 'Mestre'),
(21, 'Carla Almeida', 'Doutor'),
(22, 'Renato Ramos', 'Mestre'),
(23, 'Laura Fernandes', 'Doutor'),
(24, 'Marcelo Sousa', 'Mestre'),
(25, 'Sandra Lima', 'Doutor'),
(26, 'Diego Gomes', 'Mestre'),
(27, 'Vanessa Oliveira', 'Doutor'),
(28, 'Thiago Barbosa', 'Mestre'),
(29, 'Erika Martins', 'Doutor'),
(30, 'Rodrigo Santos', 'Mestre'),
(31, 'Patricia Costa', 'Doutor'),
(32, 'Fernando Oliveira', 'Mestre'),
(33, 'Luciana Lima', 'Doutor'),
(34, 'Giovanni Pereira', 'Mestre'),
(35, 'Bruna Almeida', 'Doutor'),
(36, 'Renan Ramos', 'Mestre'),
(37, 'Jessica Fernandes', 'Doutor'),
(38, 'Marcela Sousa', 'Mestre'),
(39, 'Luciano Lima', 'Doutor'),
(40, 'Roberta Oliveira', 'Mestre'),
(41, 'Thales Barbosa', 'Doutor'),
(42, 'Vanessa Martins', 'Mestre'),
(43, 'Guilherme Santos', 'Doutor'),
(44, 'Fabiana Costa', 'Mestre'),
(45, 'Leandro Oliveira', 'Doutor'),
(46, 'Nathalia Gomes', 'Mestre'),
(47, 'Vitoria Lima', 'Doutor'),
(48, 'Ricardo Pereira', 'Mestre'),
(49, 'Carolina Almeida', 'Doutor'),
(50, 'Matheus Ramos', 'Mestre');
GO
-- Disciplinas Curso 1
INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1001,'Introdu��o � Administra��o', 4, '13:00', 1, 'Segunda-feira', 1, 1),
(1002,'Contabilidade Financeira', 2, '13:00', 1, 'Ter�a-feira', 2, 1),
(1003,'Economia Empresarial', 4, '13:00', 1, 'Quarta-feira', 3, 1),
(1004,'Gest�o de Pessoas', 2, '13:00', 1, 'Quinta-feira', 4, 1),
(1005,'Marketing e Vendas', 4, '13:00', 1, 'Sexta-feira', 5, 1),
(1006,'Empreendedorismo', 2, '13:00', 1, 'S�bado', 6, 1),
(1007,'Gest�o Estrat�gica', 4, '13:00', 2, 'Segunda-feira', 7, 1),
(1008,'Log�stica Empresarial', 2, '13:00', 2, 'Ter�a-feira', 8, 1),
(1009,'Direito Empresarial', 4, '13:00', 2, 'Quarta-feira', 9, 1),
(1010,'Finan�as Corporativas', 2, '13:00', 2, 'Quinta-feira', 10, 1),
(1011,'Gest�o de Projetos', 4, '13:00', 2, 'Sexta-feira', 1, 1),
(1012,'Comportamento Organizacional', 2, '13:00', 2, 'S�bado', 2, 1),
(1013,'Gest�o de Qualidade', 4, '14:50', 3, 'Segunda-feira', 3, 1),
(1014,'Administra��o de Produ��o', 2, '14:50', 3, 'Ter�a-feira', 4, 1),
(1015,'Comunica��o Empresarial', 4, '14:50', 3, 'Quarta-feira', 5, 1),
(1016,'Tecnologia da Informa��o', 2, '14:50', 3, 'Quinta-feira', 6, 1),
(1017,'Gest�o Ambiental', 4, '14:50', 3, 'Sexta-feira', 7, 1),
(1018,'�tica e Responsabilidade Social', 2, '14:50', 3, 'S�bado', 8, 1),
(1019,'Estrat�gias de Marketing', 4, '14:50', 4, 'Segunda-feira', 1, 1),
(1020,'Finan�as Corporativas Avan�adas', 2, '14:50', 4, 'Ter�a-feira', 2, 1),
(1021,'Gest�o de Projetos Empresariais', 4, '14:50', 4, 'Quarta-feira', 3, 1),
(1022,'Com�rcio Internacional', 2, '14:50', 4, 'Quinta-feira', 4, 1),
(1023,'Empreendedorismo Corporativo', 4, '14:50', 4, 'Sexta-feira', 5, 1),
(1024,'Gest�o de Opera��es', 2, '14:50', 4, 'S�bado', 6, 1),
(1025,'Gest�o de Riscos', 4, '16:40', 5, 'Segunda-feira', 7, 1),
(1026,'Marketing Digital', 2, '16:40', 5, 'Ter�a-feira', 8, 1),
(1027,'�tica nos Neg�cios', 4, '16:40', 5, 'Quarta-feira', 9, 1),
(1028,'Gest�o da Inova��o', 2, '16:40', 5, 'Quinta-feira', 10, 1),
(1029,'Gest�o de Carreira', 4, '16:40', 5, 'Sexta-feira', 1, 1),
(1030,'Empreendedorismo Social', 2, '16:40', 5, 'S�bado', 2, 1),
(1031,'Negocia��o Empresarial', 4, '16:40', 6, 'Segunda-feira', 3, 1),
(1032,'Gest�o de Conflitos', 2, '16:40', 6, 'Ter�a-feira', 4, 1),
(1033,'Gest�o de Mudan�as Organizacionais', 4, '16:40', 6, 'Quarta-feira', 5, 1),
(1034,'An�lise de Investimentos', 2, '16:40', 6, 'Quinta-feira', 6, 1),
(1035,'Consultoria Empresarial', 4, '16:40', 6, 'Sexta-feira', 7, 1),
(1036,'Projeto Integrador em Administra��o', 2, '16:40', 6, 'S�bado', 8, 1);
GO

--Curso 3
INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1037,'Introdu��o ao Estudo do Direito', 4, '13:00', 1, 'Segunda-feira', 1, 3),
(1038,'Teoria Geral do Estado', 2, '13:00', 1, 'Ter�a-feira', 2, 3),
(1039,'Hist�ria do Direito', 4, '13:00', 1, 'Quarta-feira', 3, 3),
(1040,'Filosofia do Direito', 2, '13:00', 1, 'Quinta-feira', 4, 3),
(1041,'Direito Civil I', 4, '13:00', 1, 'Sexta-feira', 5, 3),
(1042,'Direito Penal I', 2, '13:00', 1, 'S�bado', 6, 3), 
(1043,'Direito Constitucional I', 4, '13:00', 2, 'Segunda-feira', 7, 3),
(1044,'Direito Administrativo I', 2, '13:00', 2, 'Ter�a-feira', 8, 3),
(1045,'Direito Processual Civil I', 4, '13:00', 2, 'Quarta-feira', 9, 3),
(1046,'Direito Empresarial I', 2, '13:00', 2, 'Quinta-feira', 10, 3),
(1047,'Direito do Trabalho I', 4, '13:00', 2, 'Sexta-feira', 11, 3),
(1048,'Direito Internacional P�blico', 2, '13:00', 2, 'S�bado', 12, 3),
(1049,'Direito Constitucional II', 4, '14:50', 3, 'Segunda-feira', 13, 3),
(1050,'Direito Tribut�rio I', 2, '14:50', 3, 'Ter�a-feira', 14, 3),
(1051,'Direito Processual Penal I', 4, '14:50', 3, 'Quarta-feira', 15, 3),
(1052,'Direito Internacional Privado', 2, '14:50', 3, 'Quinta-feira', 16, 3),
(1053,'Direito do Consumidor', 4, '14:50', 3, 'Sexta-feira', 17, 3),
(1054,'Direito Ambiental', 2, '14:50', 3, 'S�bado', 18, 3),
(1055,'Direito Civil II', 4, '14:50', 4, 'Segunda-feira', 19, 3),
(1056,'Direito Processual Civil II', 2, '14:50', 4, 'Ter�a-feira', 20, 3),
(1057,'Direito Penal II', 4, '14:50', 4, 'Quarta-feira', 21, 3),
(1058,'Direito Processual Penal II', 2, '14:50', 4, 'Quinta-feira', 22, 3),
(1059,'Direito Administrativo II', 4, '14:50', 4, 'Sexta-feira', 23, 3),
(1060,'Direito Tribut�rio II', 2, '14:50', 4, 'S�bado', 24, 3),
(1061,'Direito Civil III', 4, '16:40', 5, 'Segunda-feira', 25, 3),
(1062,'Direito Processual Civil III', 2, '16:40', 5, 'Ter�a-feira', 26, 3),
(1063,'Direito Penal III', 4, '16:40', 5, 'Quarta-feira', 27, 3),
(1064,'Direito Processual Penal III', 2, '16:40', 5, 'Quinta-feira', 28, 3),
(1065,'Direito do Trabalho II', 4, '16:40', 5, 'Sexta-feira', 29, 3),
(1066,'Direito Previdenci�rio', 2, '16:40', 5, 'S�bado', 30, 3),
(1067,'Direito Civil IV', 4, '16:40', 6, 'Segunda-feira', 31, 3),
(1068,'Direito Processual Civil IV', 2, '16:40', 6, 'Ter�a-feira', 32, 3),
(1069,'Direito Penal IV', 4, '16:40', 6, 'Quarta-feira', 33, 3),
(1070,'Direito Processual Penal IV', 2, '16:40', 6, 'Quinta-feira', 34, 3),
(1071,'Direito Empresarial II', 4, '16:40', 6, 'Sexta-feira', 35, 3),
(1072,'Direito da Fam�lia e Sucess�es', 2, '16:40', 6, 'S�bado', 36, 3);
GO
--Disciplinas Curso 4

INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1073,'Anatomia Humana I', 4, '13:00', 1, 'Segunda-feira', 1, 4),
(1074,'Bioqu�mica M�dica I', 2, '13:00', 1, 'Ter�a-feira', 2, 4),
(1075,'Biologia Celular e Molecular', 4, '13:00', 1, 'Quarta-feira', 3, 4),
(1076,'Embriologia', 2, '13:00', 1, 'Quinta-feira', 4, 4),
(1077,'Histologia', 4, '13:00', 1, 'Sexta-feira', 5, 4),
(1078,'Fisiologia Humana I', 2, '13:00', 1, 'S�bado', 6, 4),
(1079,'Anatomia Humana II', 4, '13:00', 2, 'Segunda-feira', 7, 4),
(1080,'Bioqu�mica M�dica II', 2, '13:00', 2, 'Ter�a-feira', 8, 4),
(1081,'Gen�tica M�dica', 4, '13:00', 2, 'Quarta-feira', 9, 4),
(1082,'Imunologia', 2, '13:00', 2, 'Quinta-feira', 10, 4),
(1083,'Parasitologia', 4, '13:00', 2, 'Sexta-feira', 11, 4),
(1084,'Microbiologia', 2, '13:00', 2, 'S�bado', 12, 4),
(1085,'Patologia Geral', 4, '14:50', 3, 'Segunda-feira', 13, 4),
(1086,'Farmacologia', 2, '14:50', 3, 'Ter�a-feira', 14, 4),
(1087,'Epidemiologia', 4, '14:50', 3, 'Quarta-feira', 15, 4),
(1088,'Semiologia M�dica', 2, '14:50', 3, 'Quinta-feira', 16, 4),
(1089,'Psicologia M�dica', 4, '14:50', 3, 'Sexta-feira', 17, 4),
(1090,'Bioestat�stica', 2, '14:50', 3, 'S�bado', 18, 4),
(1091,'Patologia Especial', 4, '14:50', 4, 'Segunda-feira', 19, 4),
(1092,'Farmacologia Cl�nica', 2, '14:50', 4, 'Ter�a-feira', 20, 4),
(1093,'Sa�de P�blica', 4, '14:50', 4, 'Quarta-feira', 21, 4),
(1094,'Medicina Preventiva e Social', 2, '14:50', 4, 'Quinta-feira', 22, 4),
(1095,'Medicina Legal', 4, '14:50', 4, 'Sexta-feira', 23, 4),
(1096,'�tica M�dica', 2, '14:50', 4, 'S�bado', 24, 4), 
(1097,'Cl�nica M�dica I', 4, '16:40', 5, 'Segunda-feira', 25, 4),
(1098,'Cl�nica Cir�rgica I', 2, '16:40', 5, 'Ter�a-feira', 26, 4),
(1099,'Pediatria', 4, '16:40', 5, 'Quarta-feira', 27, 4),
(1100,'Ginecologia e Obstetr�cia', 2, '16:40', 5, 'Quinta-feira', 28, 4),
(1101,'Sa�de Mental', 4, '16:40', 5, 'Sexta-feira', 29, 4),
(1102,'Medicina de Fam�lia e Comunidade', 2, '16:40', 5, 'S�bado', 30, 4),
(1103,'Cl�nica M�dica II', 4, '16:40', 6, 'Segunda-feira', 31, 4),
(1104,'Cl�nica Cir�rgica II', 2, '16:40', 6, 'Ter�a-feira', 32, 4),
(1105,'Ortopedia e Traumatologia', 4, '16:40', 6, 'Quarta-feira', 33, 4),
(1106,'Urologia', 2, '16:40', 6, 'Quinta-feira', 34, 4),
(1107,'Oftalmologia', 4, '16:40', 6, 'Sexta-feira', 35, 4),
(1108,'Otorrinolaringologia', 2, '16:40', 6, 'S�bado', 36, 4);
GO
-- Curso 5 
-- Semestre 1
INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1109,'Introdu��o � Programa��o', 4, '13:00', 1, 'Segunda-feira', 1, 5),
(1110,'Algoritmos e Estrutura de Dados', 2, '13:00', 1, 'Ter�a-feira', 2, 5),
(1111,'C�lculo I', 4, '13:00', 1, 'Quarta-feira', 3, 5),
(1112,'L�gica Matem�tica', 2, '13:00', 1, 'Quinta-feira', 4, 5),
(1113,'Arquitetura de Computadores', 4, '13:00', 1, 'Sexta-feira', 5, 5),
(1114,'Ingl�s T�cnico', 2, '13:00', 1, 'S�bado', 6, 5),
(1115,'Estruturas de Dados Avan�adas', 4, '13:00', 2, 'Segunda-feira', 7, 5),
(1116,'Programa��o Orientada a Objetos', 2, '13:00', 2, 'Ter�a-feira', 8, 5),
(1117,'C�lculo II', 4, '13:00', 2, 'Quarta-feira', 9, 5),
(1118,'Redes de Computadores', 2, '13:00', 2, 'Quinta-feira', 10, 5),
(1119,'Banco de Dados', 4, '13:00', 2, 'Sexta-feira', 11, 5),
(1120,'�tica e Cidadania', 2, '13:00', 2, 'S�bado', 12, 5),
(1121,'Sistemas Operacionais', 4, '14:50', 3, 'Segunda-feira', 13, 5),
(1122,'Engenharia de Software I', 2, '14:50', 3, 'Ter�a-feira', 14, 5),
(1123,'C�lculo III', 4, '14:50', 3, 'Quarta-feira', 15, 5),
(1124,'Gest�o de Projetos de TI', 2, '14:50', 3, 'Quinta-feira', 16, 5),
(1125,'Seguran�a da Informa��o', 4, '14:50', 3, 'Sexta-feira', 17, 5),
(1126,'Empreendedorismo', 2, '14:50', 3, 'S�bado', 18, 5),
(1127,'Programa��o Web', 4, '14:50', 4, 'Segunda-feira', 19, 5),
(1128,'Engenharia de Software II', 2, '14:50', 4, 'Ter�a-feira', 20, 5),
(1129,'Matem�tica Discreta', 4, '14:50', 4, 'Quarta-feira', 21, 5),
(1130,'Intelig�ncia Artificial', 2, '14:50', 4, 'Quinta-feira', 22, 5),
(1131,'Computa��o Gr�fica', 4, '14:50', 4, 'Sexta-feira', 23, 5),
(1132,'Trabalho de Conclus�o de Curso I', 2, '14:50', 4, 'S�bado', 24, 5),
(1133,'Programa��o Paralela e Distribu�da', 4, '16:40', 5, 'Segunda-feira', 25, 5),
(1134,'Minera��o de Dados', 2, '16:40', 5, 'Ter�a-feira', 26, 5),
(1135,'Computa��o em Nuvem', 4, '16:40', 5, 'Quarta-feira', 27, 5),
(1136,'Gest�o de Tecnologia da Informa��o', 2, '16:40', 5, 'Quinta-feira', 28, 5),
(1137,'Desenvolvimento Mobile', 4, '16:40', 5, 'Sexta-feira', 29, 5),
(1138,'Est�gio Supervisionado', 2, '16:40', 5, 'S�bado', 30, 5),
(1139,'Computa��o Qu�ntica', 4, '16:40', 6, 'Segunda-feira', 31, 5),
(1140,'�tica em Tecnologia da Informa��o', 2, '16:40', 6, 'Ter�a-feira', 32, 5),
(1141,'T�picos Avan�ados em Ci�ncia da Computa��o', 4, '16:40', 6, 'Quarta-feira', 33, 5),
(1142,'Sistemas Distribu�dos', 2, '16:40', 6, 'Quinta-feira', 34, 5),
(1143,'Projeto de Sistemas', 4, '16:40', 6, 'Sexta-feira', 35, 5),
(1144,'Trabalho de Conclus�o de Curso II', 2, '16:40', 6, 'S�bado', 36, 5);
GO
-- Disciplinas Curso 6

INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1145,'Introdu��o � Psicologia', 4, '13:00', 1, 'Segunda-feira', 1, 6),
(1146,'Psicologia Geral', 2, '14:50', 1, 'Ter�a-feira', 2, 6),
(1147,'Teorias da Personalidade', 4, '13:00', 1, 'Quarta-feira', 3, 6),
(1148,'Neuroci�ncia e Comportamento', 2, '14:50', 1, 'Quinta-feira', 4, 6),
(1149,'Metodologia Cient�fica em Psicologia', 4, '13:00', 1, 'Sexta-feira', 5, 6),
(1150,'�tica e Deontologia em Psicologia', 2, '14:50', 1, 'S�bado', 6, 6), 
(1151,'Psicologia do Desenvolvimento', 4, '13:00', 2, 'Segunda-feira', 7, 6),
(1152,'Psicopatologia Geral', 2, '14:50', 2, 'Ter�a-feira', 8, 6),
(1153,'Psicologia Social', 4, '13:00', 2, 'Quarta-feira', 9, 6),
(1154,'Psicologia Organizacional e do Trabalho', 2, '14:50', 2, 'Quinta-feira', 10, 6),
(1155,'Avalia��o Psicol�gica', 4, '13:00', 2, 'Sexta-feira', 11, 6),
(1156,'Psicologia Jur�dica', 2, '14:50', 2, 'S�bado', 12, 6),
(1157,'Psicologia da Sa�de', 4, '13:00', 3, 'Segunda-feira', 13, 6),
(1158,'Psicoterapia Breve', 2, '14:50', 3, 'Ter�a-feira', 14, 6),
(1159,'Psicologia Escolar e Educacional', 4, '13:00', 3, 'Quarta-feira', 15, 6),
(1160,'Psicologia Hospitalar', 2, '14:50', 3, 'Quinta-feira', 16, 6),
(1161,'Psicologia do Esporte', 4, '13:00', 3, 'Sexta-feira', 17, 6),
(1162,'Psicologia Ambiental', 2, '14:50', 3, 'S�bado', 18, 6),
(1163,'Psicologia do Envelhecimento', 4, '13:00', 4, 'Segunda-feira', 19, 6),
(1164,'Psicologia da Fam�lia', 2, '14:50', 4, 'Ter�a-feira', 20, 6),
(1165,'Psicologia Comunit�ria', 4, '13:00', 4, 'Quarta-feira', 21, 6),
(1166,'Psicologia da Arte', 2, '14:50', 4, 'Quinta-feira', 22, 6),
(1167,'Psicologia Transpessoal', 4, '13:00', 4, 'Sexta-feira', 23, 6),
(1168,'Psicologia do Tr�nsito', 2, '14:50', 4, 'S�bado', 24, 6),
(1169,'Neuropsicologia', 4, '13:00', 5, 'Segunda-feira', 25, 6),
(1170,'Psicologia Hospitalar', 2, '14:50', 5, 'Ter�a-feira', 26, 6),
(1171,'Psicologia Cl�nica Infantil', 4, '13:00', 5, 'Quarta-feira', 27, 6),
(1172,'Psicoterapia de Grupo', 2, '14:50', 5, 'Quinta-feira', 28, 6),
(1173,'Psicologia do Esporte', 4, '13:00', 5, 'Sexta-feira', 29, 6),
(1174,'Psicologia Organizacional', 2, '14:50', 5, 'S�bado', 30, 6),
(1175,'Psicologia da Personalidade', 4, '13:00', 6, 'Segunda-feira', 31, 6),
(1176,'Psicologia Anal�tica', 2, '14:50', 6, 'Ter�a-feira', 32, 6),
(1177,'Psicologia do Desenvolvimento', 4, '13:00', 6, 'Quarta-feira', 33, 6),
(1178,'Psicologia Social', 2, '14:50', 6, 'Quinta-feira', 34, 6),
(1179,'Psicologia Cl�nica', 4, '13:00', 6, 'Sexta-feira', 35, 6),
(1180,'Psicologia do Trabalho', 2, '14:50', 6, 'S�bado', 36, 6);
GO
-- Disciplinas Curso 7

INSERT INTO disciplina (codigo,nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1181,'Introdu��o � Administra��o P�blica', 4, '13:00', 1, 'Segunda-feira', 1, 7),
(1182,'Gest�o de Pol�ticas P�blicas', 2, '14:50', 1, 'Ter�a-feira', 2, 7),
(1183,'Administra��o Financeira e Or�ament�ria', 4, '13:00', 1, 'Quarta-feira', 3, 7),
(1184,'Gest�o de Pessoas no Setor P�blico', 2, '14:50', 1, 'Quinta-feira', 4, 7),
(1185,'Direito Administrativo', 4, '13:00', 1, 'Sexta-feira', 5, 7),
(1186,'�tica na Administra��o P�blica', 2, '14:50', 1, 'S�bado', 6, 7),
(1187,'Planejamento e Gest�o Estrat�gica', 4, '13:00', 2, 'Segunda-feira', 7, 7),
(1188,'Administra��o de Recursos Humanos no Setor P�blico', 2, '14:50', 2, 'Ter�a-feira', 8, 7),
(1189,'Licita��es e Contratos Administrativos', 4, '13:00', 2, 'Quarta-feira', 9, 7),
(1190,'Gest�o da Qualidade no Setor P�blico', 2, '14:50', 2, 'Quinta-feira', 10, 7),
(1191,'Legisla��o Tribut�ria Municipal', 4, '13:00', 2, 'Sexta-feira', 11, 7),
(1192,'Gest�o Ambiental no Setor P�blico', 2, '14:50', 2, 'S�bado', 12, 7),
(1193,'Desenvolvimento Econ�mico e Social', 4, '13:00', 3, 'Segunda-feira', 13, 7),
(1194,'Elabora��o e Avalia��o de Projetos Sociais', 2, '14:50', 3, 'Ter�a-feira', 14, 7),
(1195,'Contabilidade P�blica', 4, '13:00', 3, 'Quarta-feira', 15, 7),
(1196,'Marketing Governamental', 2, '14:50', 3, 'Quinta-feira', 16, 7),
(1197,'Gest�o de Crises e Emerg�ncias', 4, '13:00', 3, 'Sexta-feira', 17, 7),
(1198,'Governan�a e Transpar�ncia', 2, '14:50', 3, 'S�bado', 18, 7),
(1199,'Gest�o de Projetos no Setor P�blico', 4, '13:00', 4, 'Segunda-feira', 19, 7),
(1200,'Gest�o da Inova��o no Setor P�blico', 2, '14:50', 4, 'Ter�a-feira', 20, 7),
(1201,'Pol�ticas P�blicas de Sa�de', 4, '13:00', 4, 'Quarta-feira', 21, 7),
(1202,'Pol�ticas Educacionais', 2, '14:50', 4, 'Quinta-feira', 22, 7),
(1203,'Planejamento Urbano e Regional', 4, '13:00', 4, 'Sexta-feira', 23, 7),
(1204,'Gest�o de Servi�os P�blicos', 2, '14:50', 4, 'S�bado', 24, 7),
(1205,'�tica e Responsabilidade Social', 4, '13:00', 5, 'Segunda-feira', 25, 7),
(1206,'Comunica��o no Setor P�blico', 2, '14:50', 5, 'Ter�a-feira', 26, 7),
(1207,'Or�amento P�blico', 4, '13:00', 5, 'Quarta-feira', 27, 7),
(1208,'Pol�ticas de Seguran�a P�blica', 2, '14:50', 5, 'Quinta-feira', 28, 7),
(1209,'Gest�o de Tecnologia da Informa��o no Setor P�blico', 4, '13:00', 5, 'Sexta-feira', 29, 7),
(1210,'Pol�ticas de Assist�ncia Social', 2, '14:50', 5, 'S�bado', 30, 7),
(1211,'Direito Administrativo Avan�ado', 4, '13:00', 6, 'Segunda-feira', 31, 7),
(1212,'Gest�o de Conflitos e Negocia��o', 2, '14:50', 6, 'Ter�a-feira', 32, 7),
(1213,'Sustentabilidade e Responsabilidade Socioambiental', 4, '13:00', 6, 'Quarta-feira', 33, 7),
(1214,'Inova��o e Governo Aberto', 2, '14:50', 6, 'Quinta-feira', 34, 7),
(1215,'Direito e Legisla��o Aplicada � Administra��o P�blica', 4, '13:00', 6, 'Sexta-feira', 35, 7),
(1216,'Pol�ticas Culturais e de Turismo', 2, '14:50', 6, 'S�bado', 36, 7);
GO
-- Disciplinas Curso 8
INSERT INTO disciplina (codigo,nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1217,'Circuitos El�tricos I', 4, '13:00', 1, 'Segunda-feira', 1, 8),
(1218,'Eletromagnetismo', 2, '14:50', 1, 'Ter�a-feira', 2, 8),
(1219,'Eletr�nica Anal�gica I', 4, '13:00', 1, 'Quarta-feira', 3, 8),
(1220,'Programa��o para Engenharia', 2, '14:50', 1, 'Quinta-feira', 4, 8),
(1221,'C�lculo I', 4, '13:00', 1, 'Sexta-feira', 5, 8),
(1222,'Introdu��o � Engenharia El�trica', 2, '14:50', 1, 'S�bado', 6, 8),
(1223,'Circuitos El�tricos II', 4, '13:00', 2, 'Segunda-feira', 7, 8),
(1224,'Eletr�nica Anal�gica II', 2, '14:50', 2, 'Ter�a-feira', 8, 8),
(1225,'M�quinas El�tricas', 4, '13:00', 2, 'Quarta-feira', 9, 8),
(1226,'C�lculo II', 2, '14:50', 2, 'Quinta-feira', 10, 8),
(1227,'F�sica Aplicada � Engenharia', 4, '13:00', 2, 'Sexta-feira', 11, 8),
(1228,'Desenho T�cnico', 2, '14:50', 2, 'S�bado', 12, 8),
(1229,'Instala��es El�tricas', 4, '13:00', 3, 'Segunda-feira', 13, 8),
(1230,'Controle e Automa��o', 2, '14:50', 3, 'Ter�a-feira', 14, 8),
(1231,'Materiais El�tricos', 4, '13:00', 3, 'Quarta-feira', 15, 8),
(1232,'Equa��es Diferenciais', 2, '14:50', 3, 'Quinta-feira', 16, 8),
(1233,'Teoria Eletromagn�tica', 4, '13:00', 3, 'Sexta-feira', 17, 8),
(1234,'F�sica III', 2, '14:50', 3, 'S�bado', 18, 8),
(1235,'Sistemas de Energia', 4, '13:00', 4, 'Segunda-feira', 19, 8),
(1236,'Eletr�nica de Pot�ncia', 2, '14:50', 4, 'Ter�a-feira', 20, 8),
(1237,'Eletromagnetismo Aplicado', 4, '13:00', 4, 'Quarta-feira', 21, 8),
(1238,'M�todos Num�ricos', 2, '14:50', 4, 'Quinta-feira', 22, 8),
(1239,'Sinais e Sistemas', 4, '13:00', 4, 'Sexta-feira', 23, 8),
(1240,'Mec�nica dos Fluidos', 2, '14:50', 4, 'S�bado', 24, 8),
(1241,'Prote��o de Sistemas El�tricos', 4, '13:00', 5, 'Segunda-feira', 25, 8),
(1242,'Sistemas Digitais', 2, '14:50', 5, 'Ter�a-feira', 26, 8),
(1243,'Convers�o de Energia', 4, '13:00', 5, 'Quarta-feira', 27, 8),
(1244,'Economia de Energia', 2, '14:50', 5, 'Quinta-feira', 28, 8),
(1245,'Engenharia Econ�mica', 4, '13:00', 5, 'Sexta-feira', 29, 8),
(1246,'Controle de Processos', 2, '14:50', 5, 'S�bado', 30, 8),
(1247,'Subesta��es El�tricas', 4, '13:00', 6, 'Segunda-feira', 31, 8),
(1248,'Efici�ncia Energ�tica', 2, '14:50', 6, 'Ter�a-feira', 32, 8),
(1249,'Automa��o Industrial', 4, '13:00', 6, 'Quarta-feira', 33, 8),
(1250,'Redes de Comunica��o', 2, '14:50', 6, 'Quinta-feira', 34, 8),
(1251,'Projeto Integrado de Engenharia El�trica', 4, '13:00', 6, 'Sexta-feira', 35, 8),
(1252,'Trabalho de Conclus�o de Curso', 2, '14:50', 6, 'S�bado', 36, 8);
GO
-- Disciplinas Curso 9

INSERT INTO disciplina (codigo,nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1253,'Introdu��o � Gastronomia', 4, '13:00', 1, 'Segunda-feira', 1, 9),
(1254,'Higiene e Seguran�a Alimentar', 2, '14:50', 1, 'Ter�a-feira', 2, 9),
(1255,'T�cnicas B�sicas de Culin�ria', 4, '13:00', 1, 'Quarta-feira', 3, 9),
(1256,'Gastronomia Brasileira', 2, '14:50', 1, 'Quinta-feira', 4, 9),
(1257,'Hist�ria da Gastronomia', 4, '13:00', 1, 'Sexta-feira', 5, 9),
(1258,'Nutri��o e Diet�tica', 2, '14:50', 1, 'S�bado', 6, 9),
(1259,'T�cnicas de Confeitaria', 4, '13:00', 2, 'Segunda-feira', 7, 9),
(1260,'Panifica��o', 2, '14:50', 2, 'Ter�a-feira', 8, 9),
(1261,'Cozinha Internacional', 4, '13:00', 2, 'Quarta-feira', 9, 9),
(1262,'Gastronomia Molecular', 2, '14:50', 2, 'Quinta-feira', 10, 9),
(1263,'Gest�o de Restaurantes e Bares', 4, '13:00', 2, 'Sexta-feira', 11, 9),
(1264,'Enologia', 2, '14:50', 2, 'S�bado', 12, 9), 
(1265,'Gastronomia Asi�tica', 4, '13:00', 3, 'Segunda-feira', 13, 9),
(1266,'Gastronomia Italiana', 2, '14:50', 3, 'Ter�a-feira', 14, 9),
(1267,'Gastronomia Francesa', 4, '13:00', 3, 'Quarta-feira', 15, 9),
(1268,'Gastronomia Espanhola', 2, '14:50', 3, 'Quinta-feira', 16, 9),
(1269,'Gastronomia Regional Brasileira', 4, '13:00', 3, 'Sexta-feira', 17, 9),
(1270,'Arte Culin�ria', 2, '14:50', 3, 'S�bado', 18, 9),
(1271,'Gastronomia Contempor�nea', 4, '13:00', 4, 'Segunda-feira', 19, 9),
(1272,'Gastronomia Molecular Avan�ada', 2, '14:50', 4, 'Ter�a-feira', 20, 9),
(1273,'Gastronomia Funcional', 4, '13:00', 4, 'Quarta-feira', 21, 9),
(1274,'Gastronomia Sustent�vel', 2, '14:50', 4, 'Quinta-feira', 22, 9),
(1275,'Gastronomia Cultural', 4, '13:00', 4, 'Sexta-feira', 23, 9),
(1276,'Empreendedorismo na Gastronomia', 2, '14:50', 4, 'S�bado', 24, 9),
(1277,'Gest�o de Custos em Gastronomia', 4, '13:00', 5, 'Segunda-feira', 25, 9),
(1278,'Gest�o de Qualidade em Servi�os de Alimenta��o', 2, '14:50', 5, 'Ter�a-feira', 26, 9),
(1279,'Gastronomia de Eventos', 4, '13:00', 5, 'Quarta-feira', 27, 9),
(1280,'Gastronomia Hospitalar', 2, '14:50', 5, 'Quinta-feira', 28, 9),
(1281,'Gastronomia Social e Comunit�ria', 4, '13:00', 5, 'Sexta-feira', 29, 9),
(1282,'Marketing Gastron�mico', 2, '14:50', 5, 'S�bado', 30, 9),
(1283,'Cozinha Internacional Avan�ada', 4, '13:00', 6, 'Segunda-feira', 31, 9),
(1284,'Gastronomia Molecular Avan�ada II', 2, '14:50', 6, 'Ter�a-feira', 32, 9),
(1285,'Gastronomia Criativa', 4, '13:00', 6, 'Quarta-feira', 33, 9),
(1286,'Cozinha Funcional', 2, '14:50', 6, 'Quinta-feira', 34, 9),
(1287,'Gastronomia Esportiva', 4, '13:00', 6, 'Sexta-feira', 35, 9),
(1288,'Planejamento de Card�pios', 2, '14:50', 6, 'S�bado', 36, 9);
GO
-- Disciplinas Curso 10
INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1289,'Introdu��o � Arquitetura', 4, '13:00', 1, 'Segunda-feira', 1, 10),
(1290,'Hist�ria da Arte', 2, '14:50', 1, 'Ter�a-feira', 2, 10),
(1291,'Desenho Arquitet�nico I', 4, '13:00', 1, 'Quarta-feira', 3, 10),
(1292,'Teoria da Arquitetura', 2, '14:50', 1, 'Quinta-feira', 4, 10),
(1293,'Geometria Descritiva', 4, '13:00', 1, 'Sexta-feira', 5, 10),
(1294,'Inform�tica Aplicada � Arquitetura', 2, '14:50', 1, 'S�bado', 6, 10),
(1295,'Constru��o Civil I', 4, '13:00', 2, 'Segunda-feira', 7, 10),
(1296,'Desenho Arquitet�nico II', 2, '14:50', 2, 'Ter�a-feira', 8, 10),
(1297,'Sociologia Urbana', 4, '13:00', 2, 'Quarta-feira', 9, 10),
(1298,'Topografia', 2, '14:50', 2, 'Quinta-feira', 10, 10),
(1299,'C�lculo Estrutural', 4, '13:00', 2, 'Sexta-feira', 11, 10),
(1300,'Est�tica e Paisagismo', 2, '14:50', 2, 'S�bado', 12, 10), 
(1301,'Constru��o Civil II', 4, '13:00', 3, 'Segunda-feira', 13, 10),
(1302,'Desenho Arquitet�nico III', 2, '14:50', 3, 'Ter�a-feira', 14, 10),
(1303,'Ecologia Urbana', 4, '13:00', 3, 'Quarta-feira', 15, 10),
(1304,'Materiais de Constru��o', 2, '14:50', 3, 'Quinta-feira', 16, 10),
(1305,'Estruturas de Concreto', 4, '13:00', 3, 'Sexta-feira', 17, 10),
(1306,'Projeto Arquitet�nico I', 2, '14:50', 3, 'S�bado', 18, 10),
(1307,'Instala��es Prediais', 4, '13:00', 4, 'Segunda-feira', 19, 10),
(1308,'Desenho Arquitet�nico IV', 2, '14:50', 4, 'Ter�a-feira', 20, 10),
(1309,'Planejamento Urbano', 4, '13:00', 4, 'Quarta-feira', 21, 10),
(1310,'Sistemas Estruturais', 2, '14:50', 4, 'Quinta-feira', 22, 10),
(1311,'Legisla��o Urbana', 4, '13:00', 4, 'Sexta-feira', 23, 10),
(1312,'Projeto Arquitet�nico II', 2, '14:50', 4, 'S�bado', 24, 10),
(1313,'Conforto Ambiental', 4, '13:00', 5, 'Segunda-feira', 25, 10),
(1314,'Desenho Assistido por Computador', 2, '14:50', 5, 'Ter�a-feira', 26, 10),
(1315,'Patrim�nio Hist�rico', 4, '13:00', 5, 'Quarta-feira', 27, 10),
(1316,'Projeto de Interiores', 2, '14:50', 5, 'Quinta-feira', 28, 10),
(1317,'Planejamento de Espa�os Urbanos', 4, '13:00', 5, 'Sexta-feira', 29, 10),
(1318,'Projeto de Urbanismo', 2, '14:50', 5, 'S�bado', 30, 10),
(1319,'Arquitetura Sustent�vel', 4, '13:00', 6, 'Segunda-feira', 31, 10),
(1320,'Projeto de Arquitetura III', 2, '14:50', 6, 'Ter�a-feira', 32, 10),
(1321,'Legisla��o e �tica Profissional', 4, '13:00', 6, 'Quarta-feira', 33, 10),
(1322,'Trabalho de Conclus�o de Curso I', 2, '14:50', 6, 'Quinta-feira', 34, 10),
(1323,'Gest�o de Projetos em Arquitetura', 4, '13:00', 6, 'Sexta-feira', 35, 10),
(1324,'Trabalho de Conclus�o de Curso II', 2, '14:50', 6, 'S�bado', 36, 10);
GO
-- Disciplinas Curso 11
INSERT INTO disciplina (codigo, nome, horasSemanais, horarioInicio, semestre, diaSemana, codigoProfessor, codigoCurso)
VALUES 
(1325,'Introdu��o � Programa��o', 4, '13:00', 1, 'Segunda-feira', 1, 11),
(1326,'Matem�tica Discreta', 2, '14:50', 1, 'Ter�a-feira', 2, 11),
(1327,'Algoritmos e Estrutura de Dados', 4, '13:00', 1, 'Quarta-feira', 3, 11),
(1328,'L�gica de Programa��o', 2, '14:50', 1, 'Quinta-feira', 4, 11),
(1329,'Banco de Dados', 4, '13:00', 1, 'Sexta-feira', 5, 11),
(1330,'Comunica��o e Express�o', 2, '14:50', 1, 'S�bado', 6, 11), 
(1331,'Programa��o Orientada a Objetos', 4, '13:00', 2, 'Segunda-feira', 7, 11),
(1332,'Estrutura de Dados Avan�ada', 2, '14:50', 2, 'Ter�a-feira', 8, 11),
(1333,'Desenvolvimento Web', 4, '13:00', 2, 'Quarta-feira', 9, 11),
(1334,'Redes de Computadores', 2, '14:50', 2, 'Quinta-feira', 10, 11),
(1335,'Engenharia de Software', 4, '13:00', 2, 'Sexta-feira', 11, 11),
(1336,'�tica e Legisla��o em Inform�tica', 2, '14:50', 2, 'S�bado', 12, 11),
(1337,'Desenvolvimento Mobile', 4, '13:00', 3, 'Segunda-feira', 13, 11),
(1338,'Sistemas Operacionais', 2, '14:50', 3, 'Ter�a-feira', 14, 11),
(1339,'An�lise e Projeto de Sistemas', 4, '13:00', 3, 'Quarta-feira', 15, 11),
(1340,'Gest�o de Projetos de TI', 2, '14:50', 3, 'Quinta-feira', 16, 11),
(1341,'Seguran�a da Informa��o', 4, '13:00', 3, 'Sexta-feira', 17, 11),
(1342,'Ingl�s T�cnico', 2, '14:50', 3, 'S�bado', 18, 11),
(1343,'Intelig�ncia Artificial', 4, '13:00', 4, 'Segunda-feira', 19, 11),
(1344,'Gest�o da Qualidade de Software', 2, '14:50', 4, 'Ter�a-feira', 20, 11),
(1345,'Teste de Software', 4, '13:00', 4, 'Quarta-feira', 21, 11),
(1346,'Governan�a de TI', 2, '14:50', 4, 'Quinta-feira', 22, 11),
(1347,'Empreendedorismo', 4, '13:00', 4, 'Sexta-feira', 23, 11),
(1348,'Projeto Integrador I', 2, '14:50', 4, 'S�bado', 24, 11), 
(1349,'Desenvolvimento de Jogos', 4, '13:00', 5, 'Segunda-feira', 25, 11),
(1350,'Big Data', 2, '14:50', 5, 'Ter�a-feira', 26, 11),
(1351,'Computa��o em Nuvem', 4, '13:00', 5, 'Quarta-feira', 27, 11),
(1352,'Programa��o Funcional', 2, '14:50', 5, 'Quinta-feira', 28, 11),
(1353,'Modelagem de Dados', 4, '13:00', 5, 'Sexta-feira', 29, 11),
(1354,'Projeto Integrador II', 2, '14:50', 5, 'S�bado', 30, 11),
(1355,'Blockchain', 4, '13:00', 6, 'Segunda-feira', 31, 11),
(1356,'DevOps', 2, '14:50', 6, 'Ter�a-feira', 32, 11),
(1357,'Arquitetura de Software', 4, '13:00', 6, 'Quarta-feira', 33, 11),
(1358,'Desenvolvimento �gil de Software', 2, '14:50', 6, 'Quinta-feira', 34, 11),
(1359,'Trabalho de Conclus�o de Curso I', 4, '13:00', 6, 'Sexta-feira', 35, 11),
(1360,'Trabalho de Conclus�o de Curso II', 2, '14:50', 6, 'S�bado', 36, 11);
GO
INSERT INTO matricula (codigo, codigoAluno, dataMatricula, semestre)
VALUES
(1, '55312103020', '2022-01-10', 1),
(2, '55312103020', '2022-07-28', 2),
(3, '86462326034', '2024-01-28', 1),
(4, '09129892031', '2024-07-28', 2),
(5, '39112829072', '2025-03-28', 1),
(6, '97006247063', '2024-03-28', 2),
(7, '39590327060', '2024-03-28', 1),
(8, '29180596096', '2021-01-1', 2),
(9, '30260403040', '2021-07-28', 3),
(10, '09129892031', '2024-07-28', 1),
(11, '97006247063', '2024-03-28', 1);
GO
INSERT INTO matriculaDisciplina (CodigoMatricula, codigoDisciplina, situacao, notaFinal)
VALUES
(1,1001,'Reprovado', 8.5),
(1,1002,'Reprovado', 5.0),
(1,1003,'Reprovado', 7.2),
(2,1001,'Aprovado', 4.8),
(2,1002,'Aprovado', 9.0),
(2,1003,'Aprovado', 6.5),
(3,1001,'Reprovado', 4.8),
(3,1002,'Reprovado', 9.0),
(3,1003,'Reprovado', 6.5),
(4,1001,'Aprovado', 4.8),
(4,1002,'Aprovado', 9.0),
(4,1003,'Em Curso', 6.5),
(5,1001,'Em Curso', 4.8),
(5,1002,'Em Curso', 9.0),
(5,1003,'Em Curso', 6.5),
(6,1001,'Em Curso', 4.8),
(6,1002,'Em Curso', 9.0),
(6,1003,'Em Curso', 6.5),
(7,1001,'Em Curso', 4.8),
(7,1002,'Em Curso', 9.0),
(7,1003,'Em Curso', 6.5),
(8,1001,'Em Curso', 4.8),
(8,1002,'Em Curso', 9.0),
(8,1003,'Em Curso', 6.5),
(9,1001,'Em Curso', 4.8),
(9,1002,'Em Curso', 9.0),
(9,1003,'Em Curso', 6.5),
(10,1001,'Em Curso', 4.8),
(10,1002,'Em Curso', 9.0),
(10,1003,'Em Curso', 6.5),
(11,1001,'Em Curso', 4.8),
(11,1002,'Em Curso', 9.0),
(11,1003,'Em Curso', 6.5);
GO
INSERT INTO conteudo (codigo, nome, descricao,codigoDisciplina)VALUES 
    (1, '�lgebra', 'Estudo dos n�meros e opera��es', 1003),
    (2, 'Geometria', 'Estudo das formas e dos espa�os', 1003),
    (3, 'C�lculo Diferencial', 'Estudo das taxas de varia��o', 1003),
    (4, 'C�lulas', 'Unidades b�sicas da vida', 1005),
    (5, 'Energia', 'Capacidade de realizar trabalho', 1005),
    (6, 'Evolu��o', 'Desenvolvimento das esp�cies ao longo do tempo', 1005),
    (7, 'Idade M�dia', 'Per�odo hist�rico entre os s�culos V e XV', 1001),
    (8, 'Revolu��o Industrial', 'Transforma��es econ�micas e sociais no s�culo XVIII', 1001),
    (9, 'Descobrimento do Brasil', 'Chegada dos portugueses em 1500', 1002),
    (10, 'Relevo Brasileiro', 'Caracter�sticas geogr�ficas do pa�s', 1002),
    (11, 'Literatura Brasileira', 'Produ��es liter�rias do Brasil', 1004),
    (12, 'Gram�tica', 'Estudo da estrutura e funcionamento da l�ngua', 1004),
    (13, 'Equa��es', 'Express�es matem�ticas com inc�gnitas', 1003),
    (14, 'Fisiologia', 'Estudo das fun��es dos organismos vivos', 1005),
    (15, 'Guerra Fria', 'Conflito pol�tico entre EUA e URSS', 1005),
    (16, 'Globaliza��o', 'Integra��o econ�mica e cultural mundial', 1004),
    (17, 'Morfologia', 'Estudo da estrutura das palavras', 1004),
    (18, 'Polin�mios', 'Express�es alg�bricas com v�rias vari�veis',1004),
    (19, 'Gen�tica', 'Estudo dos genes e hereditariedade', 1003),
    (20, 'Renascimento', 'Movimento cultural e art�stico do s�culo XVI', 1004);
GO
INSERT INTO eliminacoes (codigoMatricula, codigoDisciplina, dataEliminacao, status, nomeInstituicao)
VALUES 
(1, 1001, '2024-04-01', 'D', 'UNICSUL'),
(2, 1002, '2024-04-02', 'D', 'FATEC Zona Sul'),
(3, 1003, '2024-04-03', 'Em an�lise', 'UNINOVE'),
(4, 1001, '2024-04-04', 'D', 'UNICID'),
(4, 1003, '2024-04-04', 'D', 'ETEC'),
(5, 1002, '2024-04-05', 'Recusado', 'FATEC Zona Sul');
GO

INSERT INTO listaChamada (codigo, codigoMatricula, codigoDisciplina, dataChamada, presenca1, presenca2, presenca3, presenca4)
VALUES
(1, 1, 1001, '2024-04-01', 0, 1, 1, 1),
(2, 2, 1002, '2024-04-02', 1, 1, 1, 1),
(3, 3, 1003, '2024-04-03', 0, 1, 1, 1),
(4, 4, 1001, '2024-04-04', 1, 1, 1, 1),
(5, 5, 1002, '2024-04-05', 0, 0, 0, 0),
(6, 6, 1003, '2024-04-03', 0, 1, 1, 1),
(7, 7, 1001, '2024-04-04', 1, 1, 1, 1),
(8, 8, 1002, '2024-04-05', 0, 0, 0, 0),
(9, 9, 1003, '2024-04-03', 0, 1, 1, 1),
(10, 10, 1001, '2024-04-04', 1, 1, 1, 1),
(11, 11, 1002, '2024-04-05', 0, 0, 0, 0),
(12, 1, 1001, '2024-04-04', 1, 0, 1, 0),
(13, 2, 1002, '2024-04-04', 0, 1, 0, 1),
(14, 3, 1003, '2024-04-03', 1, 1, 1, 1),
(15, 4, 1001, '2024-04-02', 0, 0, 0, 0),
(16, 5, 1002, '2024-04-01', 1, 0, 1, 0),
(17, 6, 1003, '2024-04-01', 0, 1, 0, 1),
(18, 7, 1001, '2024-04-02', 1, 1, 1, 1),
(19, 8, 1002, '2024-04-03', 0, 0, 0, 0),
(20, 9, 1003, '2024-04-04', 1, 0, 1, 0),
(21, 10, 1001, '2024-04-05', 0, 1, 0, 1),
(22, 11, 1002, '2024-04-01', 1, 1, 1, 1),
(23, 2, 1003, '2024-04-02', 0, 0, 0, 0),
(24, 3, 1001, '2024-04-03', 1, 0, 1, 0),
(25, 4, 1002, '2024-04-04', 0, 1, 0, 1),
(26, 5, 1003, '2024-04-02', 1, 1, 1, 1),
(27, 6, 1001, '2024-04-01', 0, 0, 0, 0),
(28, 7, 1002, '2024-04-02', 1, 0, 1, 0),
(29, 8, 1003, '2024-04-03', 0, 1, 0, 1),
(30, 9, 1001, '2024-04-04', 1, 1, 1, 1),
(31, 1, 1002, '2024-04-05', 0, 0, 0, 0),
(32, 2, 1001, '2024-04-01', 0, 1, 0, 1),
(33, 3, 1002, '2024-04-02', 1, 0, 1, 0),
(34, 4, 1003, '2024-04-03', 0, 1, 0, 1),
(35, 5, 1001, '2024-04-04', 1, 0, 1, 0),
(36, 6, 1002, '2024-04-05', 0, 1, 0, 1),
(37, 7, 1003, '2024-04-01', 1, 0, 1, 0),
(38, 8, 1001, '2024-04-02', 0, 1, 0, 1),
(39, 9, 1002, '2024-04-03', 1, 0, 1, 0),
(40, 10, 1003, '2024-04-04', 0, 1, 0, 1),
(41, 1, 1001, '2024-04-05', 1, 0, 1, 0),
(42, 2, 1002, '2024-04-01', 0, 1, 0, 1),
(43, 3, 1003, '2024-04-02', 1, 0, 1, 0),
(44, 4, 1001, '2024-04-03', 0, 1, 0, 1),
(45, 5, 1002, '2024-04-04', 1, 0, 1, 0),
(46, 6, 1003, '2024-04-05', 0, 1, 0, 1),
(47, 7, 1001, '2024-04-01', 1, 0, 1, 0),
(48, 8, 1002, '2024-04-02', 0, 1, 0, 1),
(49, 9, 1003, '2024-04-03', 1, 0, 1, 0),
(50, 10, 1001, '2024-04-04', 0, 1, 0, 1),
(51, 1, 1002, '2024-04-05', 1, 0, 1, 0),
(52, 2, 1003, '2024-04-01', 0, 1, 0, 1),
(53, 3, 1001, '2024-04-02', 1, 0, 1, 0),
(54, 4, 1002, '2024-04-03', 0, 1, 0, 1),
(55, 5, 1003, '2024-04-04', 1, 0, 1, 0),
(56, 6, 1001, '2024-04-05', 0, 1, 0, 1),
(57, 7, 1002, '2024-04-01', 1, 0, 1, 0),
(58, 8, 1003, '2024-04-02', 0, 1, 0, 1),
(59, 9, 1001, '2024-04-03', 1, 0, 1, 0),
(60, 10, 1002, '2024-04-04', 0, 1, 0, 1);
GO