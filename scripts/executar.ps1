param([string]$Dispositivo = 'emulator-5554')
$ErrorActionPreference = 'Stop'
$projectDir = Split-Path -Parent $PSScriptRoot
Set-Location -LiteralPath $projectDir
$sdkDir = [Environment]::GetEnvironmentVariable('ANDROID_HOME', 'User')
if (-not $sdkDir) { $sdkDir = "$env:LOCALAPPDATA/Android/Sdk" }
$adbPath = Join-Path $sdkDir 'platform-tools/adb.exe'
if (-not (Test-Path -LiteralPath $adbPath)) { throw 'Configure o SDK Android primeiro.' }
if (-not $env:JAVA_HOME) { $env:JAVA_HOME = [Environment]::GetEnvironmentVariable('JAVA_HOME', 'User') }
& ./gradlew.bat :app:assembleDebug --console=plain
if ($LASTEXITCODE -ne 0) { throw 'A compilação falhou.' }
& $adbPath -s $Dispositivo install -r app/build/outputs/apk/debug/app-debug.apk
if ($LASTEXITCODE -ne 0) { throw 'A instalação falhou. Inicie o relógio no Device Manager.' }
& $adbPath -s $Dispositivo shell am start -n br.com.doma.assist/.MainActivity
if ($LASTEXITCODE -ne 0) { throw 'Não foi possível abrir o aplicativo.' }
