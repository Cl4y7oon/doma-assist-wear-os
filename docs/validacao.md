# Validação - Doma Assist

Ambiente de execução: Windows 11, Android Studio, emulador Wear OS Small Round com Android 11/API 30. Validação realizada em 05/10/2026.

## Resultados confirmados

| Verificação | Resultado |
| --- | --- |
| Construção do APK debug | Aprovada |
| Testes unitários | 3 testes, 0 falhas |
| Análise estática Android Lint | 0 erros; avisos de atualização das dependências de testes |
| Testes instrumentados no relógio | 3 testes, 0 falhas |
| Recurso watch e alto-falante integrado | Confirmados pelo teste no dispositivo |
| Extração de notificação expandida e rejeição de conteúdo vazio | Confirmadas |
| Notificação publicada no Android e recebida por NotificationListenerService | Confirmada, sem inserir conteúdo diretamente no armazenamento |
| Síntese de fala em português | Mecanismo inicializado; reprodução com callbacks START/DONE |
| Interface de lista, botão de voz e telas de conteúdo | Executadas e capturadas no AVD |
| Capturas pela interface do Android Studio | Quatro imagens salvas pelo Take Screenshot e uma imagem de contexto da IDE |
| Comandos de leitura, mensagem, alerta, instruções e comando desconhecido | Executados por transcrição de teste explícita |
| Pausa/retomada automática | Estados persistidos e conferidos no emulador |

Evidências: `validacao-instrumentada.txt`, `validacao-tts.txt` e `capturas/`. Os testes unitários e o Lint também geram relatórios em `app/build/reports/`, que não são versionados por serem reproduzíveis.

## Alcance da validação no ambiente de simulação

A página 19 permite ambiente de simulação ou wearable real. Os pontos abaixo descrevem o alcance das evidências, sem impor aquisição de hardware ou etapas físicas adicionais à entrega final.

- O encaminhamento de uma transcrição simulada **não comprova reconhecimento acústico**. A integração com o reconhecedor do sistema está implementada; a demonstração valida o processamento dos comandos por texto debug. A validação acústica não foi realizada.
- A imagem API 30 não oferece Activity para autorização de NotificationListenerService. A suíte de testes e o roteiro de desenvolvimento concedem esse acesso pelo comando do Android. O aplicativo não concede acesso a si mesmo.
- Os callbacks START/DONE confirmam execução pelo mecanismo de TTS. Não foi realizada avaliação humana da qualidade do som ou da compreensão das instruções.
- Não houve conexão de fone Bluetooth físico. Detecção A2DP, callback de conexão/desconexão e intent estão implementados; as evidências não comprovam uma conexão A2DP real.
- As imagens `10` a `13` foram salvas pelo Take Screenshot do Android Studio em 05/10/2026; `09` mostra o diálogo de captura na IDE. As imagens `01` a `08` foram obtidas por ADB. Os cenários foram preparados pelo modo debug; a captura foi feita pela interface da IDE.
- Nenhum smartphone foi conectado ou pareado. O procedimento da microatividade 5, de apoio, não foi executado no ambiente usado. A captura pelo app complementar não integra as evidências produzidas; as capturas desta entrega são da IDE e de ADB, com origem identificada.
- Os alertas são exercícios acadêmicos e não recebem eventos de emergência de um sistema externo.
- A postagem de uma notificação demonstrativa é real dentro do sistema Android, mas o conteúdo é fictício e identificado como teste.

O projeto implementa as funções de software do Trabalho Prático e foi demonstrado no ambiente de simulação permitido pelo roteiro. Projeto e documentação PDF estão no GitHub. A entrega é concluída com o envio do link na Sala de Aula Virtual. Não se declara execução integral de todas as microatividades nem validação acústica ou Bluetooth físico.
