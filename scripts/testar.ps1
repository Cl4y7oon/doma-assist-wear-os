param([string]$Dispositivo = 'emulator-5554')
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath (Split-Path -Parent $PSScriptRoot)
$sdkDir = [Environment]::GetEnvironmentVariable('ANDROID_HOME', 'User')
if (-not $sdkDir) { $sdkDir = "$env:LOCALAPPDATA/Android/Sdk" }
$adbPath = Join-Path $sdkDir 'platform-tools/adb.exe'
if ($Dispositivo -notlike 'emulator-*') { throw 'Este roteiro habilita notificações para teste: utilize apenas o emulador.' }
$isWatch = & $adbPath -s $Dispositivo shell pm list features
if ($LASTEXITCODE -ne 0 -or $isWatch -notcontains 'feature:android.hardware.type.watch') { throw 'Selecione um emulador Wear OS.' }
if (-not $env:JAVA_HOME) { $env:JAVA_HOME = [Environment]::GetEnvironmentVariable('JAVA_HOME', 'User') }
& ./gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleDebugAndroidTest --console=plain
if ($LASTEXITCODE -ne 0) { throw 'A validação ou compilação falhou.' }
& $adbPath -s $Dispositivo install -r app/build/outputs/apk/debug/app-debug.apk
if ($LASTEXITCODE -ne 0) { throw 'A instalação do app falhou.' }
& $adbPath -s $Dispositivo install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
if ($LASTEXITCODE -ne 0) { throw 'A instalação dos testes falhou.' }
$result = & $adbPath -s $Dispositivo shell am instrument -w br.com.doma.assist.test/androidx.test.runner.AndroidJUnitRunner
$reportText = ($result -join "`n").Trim() + "`n"
[IO.File]::WriteAllText((Join-Path (Get-Location) 'docs/validacao-instrumentada.txt'), $reportText, [Text.UTF8Encoding]::new($false))
if ($LASTEXITCODE -ne 0 -or ($result -join "`n") -notmatch 'OK \(3 tests\)') { throw 'Os testes no relógio falharam. Confira o relatório.' }
& $adbPath -s $Dispositivo shell am start -n br.com.doma.assist/.MainActivity
Write-Output 'Comandos, análise estática e 3 testes no relógio aprovados.'
