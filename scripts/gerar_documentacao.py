"""Gera a documentação acadêmica com evidências reais do projeto.

Executar com Python e reportlab.
"""
from pathlib import Path
from xml.sax.saxutils import escape
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, Image,
)
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_LEFT
from reportlab.lib.pagesizes import A4

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "output/pdf/documentacao-doma-assist.pdf"
OUT.parent.mkdir(parents=True, exist_ok=True)
NAVY = colors.HexColor("#101C25")
TEAL = colors.HexColor("#237D69")
PALE = colors.HexColor("#EAF5F1")
GRAY = colors.HexColor("#435562")
styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="BodyDoma", fontName="Helvetica", fontSize=10.5,
    leading=15, textColor=NAVY, spaceAfter=9))
styles.add(ParagraphStyle(name="SmallDoma", parent=styles["BodyDoma"], fontSize=8.4, leading=12))
styles.add(ParagraphStyle(name="TitleDoma", fontName="Helvetica-Bold", fontSize=32,
    leading=38, textColor=NAVY, spaceAfter=16))
styles.add(ParagraphStyle(name="HeadingDoma", fontName="Helvetica-Bold", fontSize=18,
    leading=23, textColor=TEAL, spaceAfter=13))
styles.add(ParagraphStyle(name="SubDoma", fontName="Helvetica-Bold", fontSize=12,
    leading=17, textColor=NAVY, spaceBefore=9, spaceAfter=7))
story = []
def p(text, style="BodyDoma"):
    return Paragraph(text, styles[style])
def add(text, style="BodyDoma"):
    story.append(p(text, style))
def heading(text): add(text, "HeadingDoma")
def sub(text): add(text, "SubDoma")
def grid(headers, rows, widths):
    data = [[p(escape(x), "SmallDoma") for x in headers]]
    data += [[p(escape(str(x)), "SmallDoma") for x in row] for row in rows]
    t = Table(data, colWidths=widths, repeatRows=1, hAlign="LEFT")
    t.setStyle(TableStyle([
        ("BACKGROUND", (0,0), (-1,0), PALE),
        ("ROWBACKGROUNDS", (0,1), (-1,-1), [colors.white, colors.HexColor("#F6F8F9")]),
        ("VALIGN", (0,0), (-1,-1), "TOP"),
        ("LEFTPADDING", (0,0), (-1,-1), 8), ("RIGHTPADDING", (0,0), (-1,-1), 8),
        ("TOPPADDING", (0,0), (-1,-1), 7), ("BOTTOMPADDING", (0,0), (-1,-1), 7),
        ("LINEBELOW", (0,0), (-1,0), 0.6, TEAL),
    ]))
    story.append(t)

add("DGT2816 / TRABALHO PRÁTICO INDIVIDUAL", "SmallDoma")
story.append(Spacer(1, 48))
add("DOMA<br/>ASSIST", "TitleDoma")
add("Assistência por áudio em um relógio Wear OS", "HeadingDoma")
add("Aplicativo acadêmico para comunicação e orientação de funcionários com necessidades especiais, oferecendo informações por voz para pessoas com deficiência visual.")
story.append(Spacer(1, 22))
grid(["Projeto", "Configuração"], [
    ("Plataforma", "Wear OS Small Round / Android 11 / API 30"),
    ("Implementação", "Kotlin, Activity nativa, ListView e Button"),
    ("Funções", "Mensagens, notificações, comandos, alertas e instruções por áudio"),
    ("Entrega", "Código, APK debug, testes, capturas e esta documentação"),
    ("Data", "05/10/2026"),
], [110, 389])
story.append(Spacer(1, 24))
add('Repositório público: <link href="https://github.com/Cl4y7oon/doma-assist-wear-os" color="#237D69">github.com/Cl4y7oon/doma-assist-wear-os</link>')
add("Aluno: CLAYTON LUIS MEIRELES GONÇALVES<br/>Matrícula: 202501537006", "SmallDoma")
add("Fonte dos requisitos: Trabalho Prático _ DGT2816 Interação com sensores de smartphones e wearebles.pdf, 22 páginas.", "SmallDoma")
story.append(PageBreak())

heading("1. Objetivo e requisitos")
add("A empresa fictícia Doma necessita de assistência por áudio: leitura de mensagens e notificações, resposta a comandos, treinamento e alertas de segurança. O aplicativo reúne essas funções em uma interface simples para relógio.")
grid(["Requisito do roteiro", "Realização"], [
    ("Android Studio e emulador", "Ambiente instalado; AVD Small Round/API 30 executado."),
    ("Projeto Wear OS, mínimo API 30", "Módulo app; recurso watch obrigatório; minSdk e targetSdk 30."),
    ("Lista, botão e Activity", "MainActivity em Kotlin, activity_main.xml com ListView e botões nativos."),
    ("BODY_SENSORS / WAKE_LOCK", "Declaradas como solicitado. Não há coleta de sinais corporais nem solicitação dessa permissão em execução."),
    ("Alto-falante / Bluetooth A2DP", "AudioHelper usa FEATURE_AUDIO_OUTPUT e getDevices(GET_DEVICES_OUTPUTS)."),
    ("Dispositivos conectados/removidos", "AudioDeviceCallback permanece registrado na Application e atualiza a interface."),
    ("Configuração Bluetooth", "Intent ACTION_BLUETOOTH_SETTINGS e extras apresentados no enunciado."),
    ("Reprodução de áudio", "Text-to-Speech em português, foco de áudio, repetição e interrupção."),
    ("Mensagens e notificações", "Mensagem demo e NotificationListenerService para texto recebido pelo Android."),
    ("Comandos de voz", "Reconhecedor do sistema; interpretação do resultado em CommandRouter."),
    ("Alertas e instruções", "Textos demonstrativos de segurança e treinamento, identificados como exercício."),
    ("Git e PDF", "Projeto, evidências e documentação publicados no repositório público."),
], [150,349])
story.append(Spacer(1,10))
add("A página 19 permite simulação Wear OS ou dispositivo real. A solução utiliza o emulador. Flutter foi instalado por constar nos materiais; o aplicativo segue as APIs nativas e exemplos Kotlin da prática final. A Activity do tutorial inicial em Java foi adaptada para Kotlin.", "SmallDoma")
story.append(PageBreak())

heading("2. Organização e funcionamento")
grid(["Arquivo", "Responsabilidade"], [
    ("MainActivity.kt", "Lista, botões, telas de conteúdo, configuração Bluetooth e reconhecimento de fala."),
    ("AudioHelper.kt", "Consulta de saídas e detecção dinâmica de conexão/desconexão."),
    ("SpeechPlayer.kt", "Inicializa TTS, seleciona português, solicita foco de áudio, fala e repete."),
    ("DomaNotificationListener.kt", "Recebe notificações, extrai texto e publica a atualização interna."),
    ("DomaApplication.kt", "Mantém áudio e preferências durante a vida do processo."),
    ("CommandRouter.kt", "Normaliza acentos/pontuação e encaminha comandos conhecidos."),
], [173,326])
sub("Fluxo de notificações")
add("Notificação publicada no Android → serviço autorizado → extração de título e texto → armazenamento privado da última mensagem → atualização da interface → leitura automática, se habilitada pelo usuário.")
add("Notificações permanentes, resumos de grupo e notificações sem texto legível são ignoradas. A leitura automática começa pausada. O usuário pode ativá-la, interrompê-la e apagar o último texto. O aplicativo não envia notificações a um servidor nem conserva um histórico completo.")
sub("Fluxo de voz")
add("Botão Falar comando → Activity de reconhecimento do sistema → resultado textual → CommandRouter → ação. Não há gravação contínua ou escuta permanente. Se o serviço estiver indisponível ou não retornar uma frase, a interface informa o ocorrido.")
grid(["Comandos", "Ação"], [
    ("ler notificação / repetir", "Ler a última notificação ou repetir a última fala."),
    ("mensagem / instruções", "Ouvir conteúdo demonstrativo e orientações de treinamento."),
    ("alerta de segurança", "Ouvir um exercício de segurança; não é uma emergência real."),
    ("pausar / retomar / Bluetooth", "Controlar leitura automática ou abrir a conexão de áudio."),
], [190,309])
story.append(PageBreak())

heading("3. Evidências da interface")
add("Capturas reais do aplicativo no AVD Wear OS Small Round/API 30, salvas pelo recurso Take Screenshot do Android Studio no painel Running Devices. Cenários preparados pelo modo debug. A origem das imagens é a IDE; não o app complementar descrito na microatividade de apoio.", "SmallDoma")
def screenshots(items):
    data = []
    for i in range(0,len(items),2):
        row=[]
        for filename, caption in items[i:i+2]:
            row.append([Image(str(ROOT / "docs/capturas" / filename), width=204, height=204), Spacer(1,6), p(caption, "SmallDoma")])
        while len(row)<2: row.append("")
        data.append(row)
    t=Table(data,colWidths=[249.5,249.5],hAlign="LEFT")
    t.setStyle(TableStyle([("VALIGN",(0,0),(-1,-1),"TOP"),
        ("TOPPADDING",(0,0),(-1,-1),8),("BOTTOMPADDING",(0,0),(-1,-1),12)]))
    story.append(t)
screenshots([
    ("10-ide-inicio.png", "Tela inicial: saída de áudio, botão de voz e lista de opções."),
    ("11-ide-notificacao.png", "Texto de notificação demonstrativa realmente recebida pelo serviço do sistema."),
    ("12-ide-alerta.png", "Exercício de segurança; o restante do texto é acessível por rolagem."),
    ("13-ide-instrucoes.png", "Treinamento em texto e TTS; o restante é acessível por rolagem."),
])
story.append(PageBreak())

heading("3.1. Registro da captura na IDE")
add("Android Studio com o projeto aberto, o relógio no painel Running Devices e o diálogo Screenshot of Wear OS Small Round API 30. A imagem registra o procedimento usado para salvar as quatro capturas da página anterior.")
story.append(Image(str(ROOT / "docs/capturas/09-ide-contexto.png"), width=499, height=499*912/1536))
story.append(Spacer(1,12))
add("As imagens foram salvas inicialmente na Área de Trabalho pelo botão Save da IDE e copiadas, sem alteração, para docs/capturas/. As capturas anteriores por ADB também foram preservadas, com sua origem identificada no README.", "SmallDoma")
sub("Escopo das microatividades e da entrega")
add("Na página 1, as microatividades são apresentadas como apoio, e a entrega esperada é o Trabalho Prático descrito depois delas, nas páginas 18-22. A microatividade 5 explica outra forma de captura, pelo app complementar no smartphone; esse procedimento não foi executado no ambiente usado.", "SmallDoma")
add("A captura complementar permanece um objetivo didático citado, sem ser apresentada como realizada. O roteiro final admite simulação e não exige aquisição de hardware nem repete essa captura como arquivo obrigatório de entrega. As evidências deste projeto documentam a alternativa de simulação.", "SmallDoma")
story.append(PageBreak())

heading("4. Construção e reprodução")
add("Abrir esta pasta no Android Studio, sincronizar o Gradle, selecionar JDK 17 ou 21 para a construção e iniciar o AVD no Device Manager. O runtime da IDE pode ser mais recente do que o runtime escolhido para o Gradle.")
grid(["Componente", "Versão usada"], [
    ("Gradle Wrapper", "8.11.1, com verificação SHA-256 da distribuição"),
    ("Android Gradle Plugin", "8.9.2"), ("Kotlin", "2.1.20"),
    ("SDK de compilação / Build Tools", "API 35 / 35.0.1"),
    ("Android mínimo e alvo", "API 30 / API 30"),
    ("Dispositivo de teste", "Wear OS Small Round, imagem Wear OS 3 x86 / API 30"),
], [212,287])
sub("Comandos de construção e teste")
add("No PowerShell, executar <b>.\\scripts\\executar.ps1</b> para compilar, instalar e abrir. Executar <b>.\\scripts\\testar.ps1</b> para testes unitários, Lint e testes instrumentados. O roteiro de testes é restrito a emuladores Wear OS e habilita acesso às notificações para a demonstração.")
sub("Autorização e compatibilidade")
add("A imagem API 30 testada não possui Activity para a autorização de notificações. No AVD, conceder acesso explicitamente com:<br/><b>adb -s emulator-5554 shell cmd notification allow_listener<br/>br.com.doma.assist/br.com.doma.assist.DomaNotificationListener</b>")
add("O aplicativo não executa essa concessão. Em dispositivos que oferecem a tela, o botão Autorizar notificações abre a configuração do sistema após explicar o acesso. Em um relógio físico, verificar a disponibilidade da autorização e do serviço de reconhecimento.")
sub("Demonstração no emulador")
add("Para repetir os cenários, abra Teste por texto (debug) no menu, digite um comando ou publique Notificação demo. As ações permitem verificar mensagens, notificações, instruções e alertas com TTS. Para registrar a tela, utilize Running Devices > Take Screenshot. O modo debug valida transcrições e não representa captação acústica.")
story.append(PageBreak())

heading("5. Resultados, limites e entrega")
grid(["Verificação", "Resultado comprovado"], [
    ("APK debug e Android Lint", "Compilação aprovada; 0 erros de Lint."),
    ("Testes de comandos", "3 testes unitários aprovados: normalização, desconhecidos e pausa/retomada."),
    ("Testes no relógio", "3 testes instrumentados aprovados: hardware, extração de texto e listener real."),
    ("TTS em português", "Mecanismo conectado e callbacks START/DONE registrados."),
    ("Pausa e retomada", "Estados de leitura automática conferidos no emulador."),
    ("Interface", "Execução e capturas de início, notificação, alerta, instruções e mensagem."),
], [190,309])
sub("Alcance da validação")
add("A integração com o reconhecedor do sistema e os comandos está implementada; os testes validam transcrições, sem comprovar captação acústica. Detecção A2DP e callback estão implementados, sem teste de fone físico. Não houve avaliação humana do som ou captura pelo app complementar. Esses são limites das evidências, sem exigência de aquisição de dispositivos para a alternativa de simulação prevista na página 19.")
add("O modo de simulação é limitado a builds debug. Alertas e instruções são exemplos acadêmicos; não há integração com sistemas reais de emergência. As limitações estão registradas para que a evidência entregue não seja confundida com validação em hardware real.")
sub("Arquivos e envio")
add("A página 22 pede projeto e documentação PDF no Git, ambos publicados no repositório público indicado na capa. Código em app/; PDF em output/pdf/; capturas em docs/capturas/; APK de apoio em entrega/; relatórios, rastreabilidade e revisão em docs/. A entrega é concluída com o envio do link ao tutor pela Sala de Aula Virtual, aba Trabalhos, dentro do prazo.")
add("O enunciado não fornece modelo de relatório, padrão ABNT ou número de páginas. Este PDF organiza identificação, objetivo, implementação, evidências, reprodução e resultados. APK e relatórios de testes são complementos; não há exigência de loja de aplicativos ou vídeo.", "SmallDoma")
sub("Referências")
for title,url in [
    ("Áudio em Wear OS", "https://developer.android.com/training/wearables/apps/audio"),
    ("Entrada de voz", "https://developer.android.com/training/wearables/user-input/voice"),
    ("NotificationListenerService", "https://developer.android.com/reference/android/service/notification/NotificationListenerService"),
    ("Android Gradle Plugin 8.9", "https://developer.android.com/build/releases/agp-8-9-0-release-notes"),
]: add(f'<link href="{url}" color="#237D69">{escape(title)}</link>', "SmallDoma")

def decorate(canvas, doc):
    canvas.setStrokeColor(TEAL); canvas.setLineWidth(1)
    canvas.line(48, 800, 547, 800)
    canvas.setFont("Helvetica",8); canvas.setFillColor(GRAY)
    canvas.drawString(48, 814, "DOMA ASSIST / DOCUMENTAÇÃO DO PROJETO")
    canvas.drawString(48,28,"DGT2816 • Trabalho individual • 05/10/2026")
    canvas.drawRightString(547,28,str(doc.page))

doc=SimpleDocTemplate(str(OUT),pagesize=A4,leftMargin=48,rightMargin=48,
    topMargin=57,bottomMargin=48,title="Doma Assist - Documentação do projeto Wear OS",
    author="CLAYTON LUIS MEIRELES GONÇALVES",subject="Trabalho prático DGT2816")
doc.build(story,onFirstPage=decorate,onLaterPages=decorate)
print(OUT)
