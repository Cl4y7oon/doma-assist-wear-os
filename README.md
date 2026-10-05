# Doma Assist - Wear OS

Trabalho prático individual da disciplina **DGT2816 - Interação com sensores de smartphones e wearables**. Aplicativo de assistência por áudio para os funcionários da empresa fictícia Doma, desenvolvido conforme o enunciado da disciplina.

## Entrega

- Projeto Android completo em Kotlin, com Gradle Wrapper.
- APK de demonstração: `entrega/doma-assist-debug.apk`.
- Documentação: `output/pdf/documentacao-doma-assist.pdf`.
- Capturas reais do emulador: `docs/capturas/`.
- Rastreabilidade do enunciado: `docs/requisitos.md`.
- Resultados e limites dos testes: `docs/validacao.md`.
- Revisão da entrega e interpretação do roteiro: `docs/revisao-enunciado.md`.

O trabalho foi desenvolvido e demonstrado no **emulador Wear OS**, alternativa expressamente permitida na página 19. Não é necessário possuir um relógio físico para reproduzir esta entrega. A página 22 pede o projeto e a documentação PDF no Git, com envio do link ao tutor. O enunciado não estabelece modelo de relatório, quantidade de páginas ou padrão ABNT; o PDF organiza identificação, objetivo, implementação, evidências, reprodução e resultados.

## Funcionalidades

1. Identifica alto-falante integrado e fone Bluetooth A2DP com `AudioManager.getDevices(GET_DEVICES_OUTPUTS)`.
2. Detecta conexão e desconexão com `registerAudioDeviceCallback`, inclusive durante leitura automática em segundo plano.
3. Abre as configurações Bluetooth com os parâmetros apresentados no roteiro.
4. Lê mensagens demonstrativas, instruções e alertas de segurança em português usando Text-to-Speech.
5. Recebe notificações pelo serviço do Android, guarda somente a última notificação e permite ouvi-la, apagá-la ou pausar a leitura automática. A leitura automática começa desativada.
6. Abre o reconhecimento de fala do sistema e encaminha o resultado para os comandos abaixo.
7. Oferece simulação de transcrição e notificação de teste **somente em builds de depuração**. Isso não representa reconhecimento acústico real.

Os textos de treinamento e emergência são explicitamente demonstrativos. O aplicativo não é um sistema operacional de segurança ou de emergência.

## Ambiente

- Android Studio com suporte Kotlin integrado; JDK 17 ou 21 para o Gradle.
- Gradle 8.11.1, Android Gradle Plugin 8.9.2 e Kotlin 2.1.20, fixados no projeto.
- Plataforma de compilação API 35 e Build Tools 35.0.1. Mínimo e alvo do aplicativo: **API 30**, conforme o roteiro. Compilar com uma plataforma mais recente não muda o Android mínimo.
- Emulador **Wear OS Small Round/API 30**, imagem `system-images;android-30;android-wear;x86`.
- Flutter SDK está instalado por constar na lista de materiais. A implementação usa APIs Android nativas, como os exemplos Kotlin/Java do próprio enunciado; não há módulo Flutter.

## Abrir no Android Studio

1. Em **Open**, selecione esta pasta, que contém `settings.gradle.kts`.
2. Aguarde a sincronização. Em **Settings > Build, Execution, Deployment > Build Tools > Gradle**, selecione JDK 17 ou 21. O runtime da IDE pode ser mais recente que o runtime compatível com Gradle 8.11.1.
3. Confira o caminho do SDK. `local.properties` é específico da máquina e não entra no Git.
4. Em **Tools > Device Manager**, inicie `Wear_OS_Small_Round_API_30`.
5. Selecione o módulo `app`, o relógio e **Run**.

Para reproduzir esta configuração em outra máquina, instale também as APIs e Build Tools indicadas acima no SDK Manager e crie o AVD Small Round/API 30.

Após instalar ou alterar Java e SDK, abra uma nova sessão do terminal para carregar as variáveis de ambiente atualizadas. Nesta máquina, a validação usa JDK 21.

## Compilar e instalar pelo terminal

```powershell
.\gradlew.bat :app:assembleDebug
.\scripts\executar.ps1
```

O script presume `emulator-5554`; utilize `-Dispositivo` para selecionar outro emulador. A instalação requer `adb` e o SDK configurados.

## Notificações e privacidade

Toque em **Autorizar notificações**, leia a explicação e conceda acesso no sistema, se essa tela existir. Sem autorização, as mensagens demonstrativas, alertas e instruções continuam disponíveis.

**A imagem Wear OS API 30 usada no trabalho não expõe a tela de autorização.** Exclusivamente para o AVD de desenvolvimento, autorize o serviço pelo computador:

```powershell
adb -s emulator-5554 shell cmd notification allow_listener br.com.doma.assist/br.com.doma.assist.DomaNotificationListener
```

Depois, abra novamente o aplicativo. Para revogar:

```powershell
adb -s emulator-5554 shell cmd notification disallow_listener br.com.doma.assist/br.com.doma.assist.DomaNotificationListener
```

Não é um mecanismo de autorização executado pelo aplicativo. Relógios físicos precisam disponibilizar a autorização correspondente ou ser configurados de maneira apropriada pelo desenvolvedor. A compatibilidade em hardware real ainda precisa ser conferida.

O texto fica no armazenamento privado do aplicativo, sem backup e sem envio para servidor. Não há coleta de sinais corporais, contatos, localização ou histórico de notificações. `BODY_SENSORS` e `WAKE_LOCK` estão declaradas conforme a microatividade 3; não se solicita acesso aos sensores corporais porque eles não são usados. A tela permanece acesa somente enquanto a Activity está visível.

## Comandos de voz

| Comando | Resultado |
| --- | --- |
| ler notificação | Lê a última notificação legível recebida |
| repetir | Repete a última fala reproduzida |
| mensagem | Lê a mensagem demonstrativa da Doma |
| alerta de segurança | Reproduz o exercício de segurança |
| instruções | Reproduz as orientações de treinamento |
| pausar | Pausa a leitura automática |
| retomar | Ativa a leitura automática após autorização |
| Bluetooth | Abre a configuração de conexão |

Use **Falar comando** para iniciar o reconhecedor do sistema. Se o serviço não existir ou não retornar uma fala, o app informa o ocorrido. O próprio reconhecedor administra sua captura de microfone; o app não grava áudio diretamente.

## Testes e demonstração

```powershell
.\scripts\testar.ps1
```

Esse roteiro exige um emulador Wear OS e habilita o listener apenas para o teste. Executa testes de comandos, análise estática, construção e testes instrumentados, inclusive uma notificação publicada no sistema e recebida pelo serviço.

Para o modo de depuração, role a lista até **Teste por texto (debug)**. Digite um comando ou publique **Notificação demo**. Também é possível disparar uma transcrição explicitamente simulada:

```powershell
adb -s emulator-5554 shell am start -n br.com.doma.assist/.MainActivity --es comandoTeste instrucoes
adb -s emulator-5554 shell am start -n br.com.doma.assist/.MainActivity --ez notificacaoTeste true
```

O modo de teste e esses parâmetros não executam ações nos builds de release. Não comprovam o uso real do microfone.

## Evidências de execução e microatividade de captura

As capturas `10-ide-inicio.png`, `11-ide-notificacao.png`, `12-ide-alerta.png` e `13-ide-instrucoes.png` foram obtidas e salvas pelo recurso **Take Screenshot** do Android Studio, com o AVD integrado ao painel **Running Devices**. `09-ide-contexto.png` registra a janela da IDE e o diálogo de captura. As imagens `01` a `08` são evidências anteriores obtidas por ADB.

Para reproduzir a captura pelo Android Studio: execute o app; em **Running Devices**, utilize **Take Screenshot** e salve a imagem em `docs/capturas/`. A posição do botão pode variar por versão da IDE. Alternativamente, o emulador oferece o botão de câmera.

A página 1 apresenta as microatividades como apoio e identifica o Trabalho Prático posterior como a entrega esperada. A microatividade 5 (página 17) explica a captura pelo app complementar no smartphone. Esse procedimento não foi executado nesta entrega em emulador; não há captura atribuída ao app complementar. O roteiro final (páginas 18-22) aceita simulação e não exige a compra de smartphone, relógio ou fone. A captura complementar continua sendo um objetivo didático citado na página 1, sem ser confundida com uma evidência já produzida ou com um requisito de aquisição de hardware.

Para outra captura de apoio pelo ADB:

```powershell
.\scripts\capturar.ps1 -Nome minha-captura
```

## Entrega acadêmica

Repositório público: https://github.com/Cl4y7oon/doma-assist-wear-os

Aluno: **CLAYTON LUIS MEIRELES GONÇALVES**. Matrícula: **202501537006**.

O projeto e o PDF estão publicados neste repositório público. Para concluir o envio, compartilhe o link acima na **Sala de Aula Virtual > Trabalhos** com o tutor, respeitando o prazo da disciplina. Não há exigência de publicar APK na loja, de entregar vídeo ou de seguir um modelo específico de PDF no documento fornecido. O APK e os relatórios de testes são arquivos adicionais de apoio.

## Referências oficiais

- [Áudio em Wear OS](https://developer.android.com/training/wearables/apps/audio)
- [Entrada de voz](https://developer.android.com/training/wearables/user-input/voice)
- [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)
- [Compatibilidade do Android Gradle Plugin 8.9](https://developer.android.com/build/releases/agp-8-9-0-release-notes)

Os exemplos do enunciado foram adaptados: `FEATURE_AUDIO_OUTPUT` verifica o recurso do dispositivo e `GET_DEVICES_OUTPUTS` seleciona as saídas em `getDevices()`. O `context` é armazenado corretamente no helper, evitando a referência fora de escopo presente no exemplo didático.
