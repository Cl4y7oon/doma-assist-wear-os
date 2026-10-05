# Revisão da entrega - Doma Assist

Aluno: CLAYTON LUIS MEIRELES GONÇALVES. Matrícula: 202501537006.
Revisão: 05/10/2026, com leitura das 22 páginas do enunciado, inspeção do código, testes no AVD e conferência do repositório público.

## Conclusão da revisão

As funcionalidades do Trabalho Prático da empresa Doma (páginas 18-22) estão implementadas. A solução foi demonstrada em Wear OS Small Round/API 30, pela alternativa de simulação autorizada na página 19. Código, PDF, capturas e relatórios estão publicados em https://github.com/Cl4y7oon/doma-assist-wear-os.

A resposta a comandos usa o reconhecedor de fala do sistema. Os testes comprovam o encaminhamento das transcrições, sem comprovar captação acústica. A detecção e o acompanhamento de Bluetooth A2DP estão implementados, sem evidência de conexão a fone físico. Essas diferenças são limites da validação, não funcionalidades fictícias nem obrigações de comprar dispositivos.

## O que o documento pede para entregar

Na página 22, a entrega consiste em armazenar o projeto em repositório Git, anexar a documentação PDF no Git e enviar o link ao tutor na Sala de Aula Virtual, aba Trabalhos, dentro do prazo. Os dois primeiros itens estão atendidos; o envio do link na Sala Virtual conclui a entrega.

Não há modelo de relatório, padrão ABNT, quantidade de páginas, exigência de vídeo ou publicação em loja. O PDF entregue contém identificação acadêmica, contextualização, rastreabilidade, organização da solução, capturas, instruções de execução e resultados. O APK debug é um complemento para facilitar a demonstração.

## Microatividade 5 e hardware

A página 1 diferencia as microatividades de apoio da entrega esperada do Trabalho Prático. A página 17 explica a captura pelo app complementar e a inclui entre os objetivos didáticos. Esse procedimento não foi executado, pois o ambiente usado não tem smartphone pareado. Foram entregues capturas reais pela IDE, identificadas como tal.

A página 19 admite simulação ou dispositivo real; o documento não exige aquisição de relógio, smartphone ou fone. Por isso, foi corrigida a orientação anterior que condicionava a entrega a testes físicos e à captura complementar. Não se afirma execução de 100% de todas as microatividades: distingue-se a solução final implementada das demonstrações não realizadas.

## Conferências realizadas

- Código completo, recursos, scripts, Gradle Wrapper e configuração de compilação publicados.
- Manifest Wear OS, permissões do roteiro, lista, botões e Activity conferidos.
- Consulta de saídas, callback dinâmico e intent Bluetooth com os parâmetros do enunciado conferidos.
- Leitura de mensagens, recebimento de notificação, TTS, comandos, alertas e instruções conferidos no código e nas evidências.
- Compilação debug e Android Lint aprovados; três testes unitários registrados e três testes instrumentados aprovados novamente no emulador.
- Treze capturas preservadas, com distinção entre ADB, Take Screenshot e contexto da IDE.
- PDF identificado com nome e matrícula, com revisão visual das páginas após as correções.
- Repositório público e arquivos de entrega conferidos; PDF publicado deve corresponder ao arquivo local da revisão.

Rastreabilidade detalhada: `docs/requisitos.md`. Alcance dos testes: `docs/validacao.md`. Próxima etapa de entrega: enviar o link ao tutor na Sala de Aula Virtual.
