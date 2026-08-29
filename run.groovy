def process = "cmd.exe /c .\\gradlew.bat lwjgl3:run".execute()
process.consumeProcessOutput(System.out, System.err)
process.waitForOrKill(10000)