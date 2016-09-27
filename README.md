## EP de Redes

**Discentes**
Mario Concilio - 7578842
Richard Santana - 7137541

## Para gerar o executável do projeto:

Rode o comando:

###Linux ou MacOs
```shellscript
./mvnw clean package
```
###Windows
```shellscript
mvnw.cmd clean package
```

Será gerado um arquivo na pasta target/ chamado **ach2026-1.0-SNAPSHOT-jar-with-dependencies.jar**

Então rode o comando na pasta raiz do projeto:

```shellscript
java -jar target/ach2026-1.0-SNAPSHOT-jar-with-dependencies.jar
```
