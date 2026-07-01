## 🚀 Execução da aplicação

Para executar a aplicação, importe o projeto como um projeto Maven. Todas as dependências serão baixadas e configuradas automaticamente.

### 🧪 Testes

Os testes unitários e de funcionalidade estão localizados na pasta `test`.  
Para executá-los, basta rodar como testes na sua IDE ou via Maven, e todos os cenários serão executados automaticamente.

---

## ⚠️ Observações

Durante o desenvolvimento, foi utilizado o banco de dados em memória **H2**, onde os dados são persistidos apenas em tempo de execução.  
Ao encerrar a aplicação, todos os dados são perdidos automaticamente.

Para facilitar a execução de testes e autenticação, foram criados automaticamente dois usuários padrão:

- **Usuário:** `user`  
- **Usuário:** `admin`  
- **Senha para ambos:** `123456`

Ao iniciar a aplicação, basta realizar a autenticação com um desses usuários para gerar o token de acesso.
