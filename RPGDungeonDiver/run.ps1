$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot
$outputDirectory = Join-Path $projectRoot "build\classes"
$sourceFiles = @(Get-ChildItem (Join-Path $projectRoot "src") -Recurse -Filter "*.java" |
    ForEach-Object { $_.FullName })

if ($sourceFiles.Count -eq 0) {
    throw "No Java source files were found under src."
}

New-Item -ItemType Directory -Force $outputDirectory | Out-Null
& javac -encoding UTF-8 -d $outputDirectory $sourceFiles
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -cp $outputDirectory rpgdungeon.Main
exit $LASTEXITCODE