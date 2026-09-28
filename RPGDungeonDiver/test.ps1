$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot
$outputDirectory = Join-Path $projectRoot "build\test-classes"
$sourceFiles = @(Get-ChildItem (Join-Path $projectRoot "src") -Recurse -Filter "*.java" |
    ForEach-Object { $_.FullName })
$testFiles = @(Get-ChildItem (Join-Path $projectRoot "test") -Recurse -Filter "*.java" |
    ForEach-Object { $_.FullName })

if ($sourceFiles.Count -eq 0 -or $testFiles.Count -eq 0) {
    throw "Java source or test files are missing."
}

New-Item -ItemType Directory -Force $outputDirectory | Out-Null
& javac -encoding UTF-8 -d $outputDirectory $sourceFiles $testFiles
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -ea -cp $outputDirectory rpgdungeon.GameLogicTest
exit $LASTEXITCODE