# Rastreabilidade do enunciado

Fonte: `Trabalho Prático _ DGT2816 Interação com sensores de smartphones e wearebles.pdf`, 22 páginas.

Revisão em 05/10/2026. A página 1 apresenta as microatividades como apoio e identifica a prática posterior como a entrega esperada. O Trabalho Prático da empresa Doma ocupa as páginas 18-22. A página 19 permite **simulação Wear OS ou wearable real**; esta entrega utiliza o emulador.

| Item do enunciado | Implementação ou evidência | Situação |
| --- | --- | --- |
| Android Studio e emulador, páginas 2-10 | Instalação global e AVD Small Round | Preparado |
| Projeto Wear OS, mínimo API 30, páginas 11-12 | Módulo `app`, manifest de relógio e `minSdk=30` | Implementado |
| MainActivity, ListView e Button, páginas 12-13 | `MainActivity.kt`, `activity_main.xml`, lista e botões nativos | Implementado em Kotlin, como os exemplos da prática final |
| BODY_SENSORS, WAKE_LOCK e MAIN/LAUNCHER, página 13 | `AndroidManifest.xml` | Declarado; sensores corporais não acessados |
| Dependências Gradle, página 14 | Plugins e versões fixadas; Gradle Wrapper | Compilado |
| Small Round/API 30, páginas 15-16 | `Wear_OS_Small_Round_API_30` | Executado |
| Captura na IDE, objetivo da página 1 | `docs/capturas/09-ide-contexto.png` e imagens `10` a `13` | Concluída pelo Take Screenshot do Android Studio |
| Captura pelo app complementar, microatividade de apoio da página 17 | Procedimento no README | Não executada no ambiente usado; nenhuma imagem atribuída ao app complementar |
| Assistência aos funcionários Doma, página 18 | Mensagem, treinamento e exercício de segurança | Implementado |
| Alto-falante e Bluetooth A2DP, páginas 19-20 | `AudioHelper` | Detecção implementada; alto-falante verificado; sem teste de fone físico |
| getDevices e FEATURE_AUDIO_OUTPUT, página 19 | Verificação do recurso + `GET_DEVICES_OUTPUTS` | Implementado e executado |
| Conexão/desconexão dinâmica, páginas 20-21 | Callback persistente na Application e atualização da interface | Implementado; conexões físicas não simuladas como reais |
| Intent Bluetooth e extras, página 21 | `MainActivity.bluetooth()` | Implementado |
| Reprodução de áudio, página 22 | `SpeechPlayer` com português, foco de áudio e controles | Reprodução TTS concluída no emulador |
| Leitura de mensagens e notificações, página 22 | TTS + `DomaNotificationListener` | Mensagens demo e recebimento real de notificação de teste validados |
| Resposta a comandos de voz, página 22 | Reconhecedor do sistema + `CommandRouter` | Integração implementada; encaminhamento de transcrições validado; captação acústica não testada |
| Alertas de segurança e instruções, página 22 | Conteúdo demonstrativo identificado na própria fala | Implementado e executado |
| Projeto no Git e documentação PDF, página 22 | Repositório público e `output/pdf/documentacao-doma-assist.pdf` | Publicados |
| Link ao tutor pela Sala Virtual, página 22 | Link no README | Envio na Sala Virtual a realizar |

Flutter consta na lista de materiais e foi instalado. O procedimento de implementação e os exemplos do trabalho utilizam APIs Android nativas; por isso o produto é um projeto Wear OS em Kotlin, sem módulo Flutter. O projeto não usa um aplicativo de celular para substituir o wearable.

## Formato e alcance da entrega

A página 22 pede projeto e PDF no Git e envio do link ao tutor pela Sala Virtual. O documento não fornece modelo de relatório, padrão ABNT, número de páginas ou exigência de vídeo, publicação em loja ou aquisição de hardware. O PDF contém identificação, objetivo, implementação, capturas, reprodução e resultados; APK e relatórios de testes são complementos.

`ListaDeTarefas` é o nome do exemplo da microatividade 2. A solução da empresa Doma chama-se Doma Assist. A MainActivity utiliza Kotlin, como os exemplos da prática final, preservando lista, botões e configurações indicados no tutorial inicial em Java.

A captura complementar aparece como objetivo didático na página 1 e procedimento de apoio na página 17; o roteiro final não a repete como arquivo obrigatório de entrega. Essa leitura não significa que a microatividade foi executada nem que todos os objetivos didáticos foram comprovados. Os limites de validação acústica e A2DP são registrados sem transformar hardware físico em obrigação de compra.

Conclusão e conferência dos arquivos: `docs/revisao-enunciado.md`.
