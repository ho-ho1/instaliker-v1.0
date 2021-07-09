# InstaLiker

### Prerequisites
Install Chocolatey (Windows)
```
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
```

Install prerequisites
```
choco install javaruntime --yes
choco install git --yes
choco install maven --yes
choco install intellijidea-community --yes
```
### Configure
Open: src/test/resources/config.properties

```
login.username=[instagramUsername]
login.password.base64=[instagramPasswordBase64Encoded]
```

### Run
Run InstaLiker

```mvn verify```
