# InstaLiker

### Prerequisites
Install Chocolatey (Windows)
```
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
```

Install prerequisites
```
choco install javaruntime
choco install git
choco install maven
```

### Run
Run InstaLiker

```mvn verify```