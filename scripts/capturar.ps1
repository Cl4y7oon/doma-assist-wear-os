param([string]$Nome = 'captura-manual', [string]$Dispositivo = 'emulator-5554')
$ErrorActionPreference = 'Stop'
if ($Nome -notmatch '^[a-zA-Z0-9_-]+$') { throw 'Use apenas letras, números, hífen e sublinhado no nome.' }
Set-Location -LiteralPath (Split-Path -Parent $PSScriptRoot)
$sdkDir = [Environment]::GetEnvironmentVariable('ANDROID_HOME', 'User')
if (-not $sdkDir) { $sdkDir = "$env:LOCALAPPDATA/Android/Sdk" }
$adbPath = Join-Path $sdkDir 'platform-tools/adb.exe'
& $adbPath -s $Dispositivo shell screencap -p /sdcard/doma-captura.png
if ($LASTEXITCODE -ne 0) { throw 'A captura falhou.' }
& $adbPath -s $Dispositivo pull /sdcard/doma-captura.png "docs/capturas/$Nome.png"
if ($LASTEXITCODE -ne 0) { throw 'Não foi possível copiar a captura.' }
