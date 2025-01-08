# CrudAGISAv3

Este projeto é parte da Atividade 3 do Laboratório de Banco de Dados, ministrado pelo Prof. Leandro Colevati na FATEC-ZL. O objetivo é desenvolver um protótipo do sistema acadêmico AGIS, permitindo o encerramento de semestre, inserção de notas parciais, controle de faltas e geração de relatórios em PDF.

## Funcionalidades

- **Inserção de Notas Parciais:** Permite registrar as notas das avaliações para cada disciplina, considerando os pesos específicos de cada avaliação.
- **Cálculo de Médias e Status:** Calcula automaticamente a média ponderada das notas e determina o status do aluno (Aprovado, Exame ou Reprovado) com base na média.
- **Controle de Faltas:** Exibe a quantidade de faltas por aluno em cada disciplina, totaliza as faltas e determina o status de aprovação com base na frequência (mínimo de 75% de presença).
- **Geração de Relatórios em PDF:** Gera relatórios em PDF das notas e frequências dos alunos, facilitando a documentação e análise dos dados.

## Tecnologias Utilizadas

- **Java**
- **Spring Framework (Spring Web, Spring Data)**
- **JSP e JSTL**
- **SQL Server**
- **Maven**

## Pré-requisitos

- **Java 11** ou superior
- **Maven 3.6.3** ou superior
- **SQL Server** configurado e em execução

## Como Executar o Projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/JonathanOliveiraCustodio/CrudAGISAv3.git
