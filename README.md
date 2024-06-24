### CifraCombinada
A classe `CifraCombinada` é armazena a chave usada e implementa o métodos de cifragem por substituição e transposição.

#### Métodos Implementados:
- `byte[] cifraSubstituicao(dados: byte[], chave: String)`: Cifra o array de bytes recebido com base na chave usando o método de substituição.
- `byte[] decifraSubstituicao(dados: byte[], chave: String)`: Decifra o array de bytes recebido com base na chave usando o método de substituição.
- `byte[] cifraTransposicao(dados: byte[], chave: String)`: Cifra o array de bytes recebido com base na chave usando o método de transposição.
- `byte[] decifraTransposicao(dados: byte[], chave: String)`: Decifra o array de bytes recebido com base na chave usando o método de transposição.
- `byte[] cifraCombinada(dados: byte[], chave: String)`: Cifra o array de bytes recebido com base na chave usando ambos os métodos de cifragem listados acima, sendo primeiramente substituição seguida de transposição.
- `byte[] decifraTransposicao(dados: byte[], chave: String)`: Decifra o array de bytes recebido com base na chave usando ambos os métodos de cifragem listados acima, na ordem contrária que foram cifrados.

### Questionário
- Há uma função de cifragem em todas as classes de entidades, envolvendo pelo menos duas operações diferentes e usando uma chave criptográfica? Sim
- Uma das operações de cifragem é baseada na substituição e a outra na transposição? Sim
- O trabalho está funcionando corretamente? Sim
- O trabalho está completo? Sim
- O trabalho é original e não a cópia de um trabalho de um colega? Sim
