param(
    [int]$Port = 8756,
    [switch]$NoBrowser
)

$ErrorActionPreference = "Stop"

$root = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$prefix = "http://localhost:$Port/"
$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add($prefix)
$listener.Start()

Write-Host "Layout editor server running at $prefix"
Write-Host "Serving repo root: $root"
Write-Host "Press Ctrl+C to stop."

if (-not $NoBrowser) {
    Start-Process ($prefix + "tools/layout-editor.html")
}

function Get-ContentType([string]$path) {
    switch ([System.IO.Path]::GetExtension($path).ToLowerInvariant()) {
        ".html" { "text/html; charset=utf-8" }
        ".js" { "application/javascript; charset=utf-8" }
        ".css" { "text/css; charset=utf-8" }
        ".json" { "application/json; charset=utf-8" }
        ".png" { "image/png" }
        ".jpg" { "image/jpeg" }
        ".jpeg" { "image/jpeg" }
        ".svg" { "image/svg+xml" }
        default { "application/octet-stream" }
    }
}

while ($listener.IsListening) {
    $ctx = $listener.GetContext()
    $req = $ctx.Request
    $res = $ctx.Response

    try {
        $path = $req.Url.AbsolutePath.TrimStart("/")

        if ($req.HttpMethod -eq "POST" -and $path -eq "api/save-layout") {
            $reader = New-Object System.IO.StreamReader($req.InputStream, $req.ContentEncoding)
            $content = $reader.ReadToEnd()
            $reader.Dispose()

            $targetKey = $req.QueryString["target"]
            if ([string]::IsNullOrWhiteSpace($targetKey)) {
                $targetKey = "default"
            }

            switch ($targetKey) {
                "default" {
                    $target = Join-Path $root "src/main/generated/assets/modernwarpmenu/layouts/layout.json"
                }
                "ultrawide" {
                    $target = Join-Path $root "src/main/resources/resourcepacks/ultra_wide_layout/assets/modernwarpmenu/layouts/layout.json"
                }
                default {
                    $res.StatusCode = 400
                    $bytes = [System.Text.Encoding]::UTF8.GetBytes("Invalid save target")
                    $res.OutputStream.Write($bytes, 0, $bytes.Length)
                    continue
                }
            }

            Set-Content -Path $target -Value $content -Encoding UTF8

            $bytes = [System.Text.Encoding]::UTF8.GetBytes("{""ok"":true}")
            $res.StatusCode = 200
            $res.ContentType = "application/json; charset=utf-8"
            $res.OutputStream.Write($bytes, 0, $bytes.Length)
            continue
        }

        if ($req.HttpMethod -eq "GET") {
            if ([string]::IsNullOrWhiteSpace($path)) {
                $path = "tools/layout-editor.html"
            }

            $safePath = $path -replace "/", [System.IO.Path]::DirectorySeparatorChar
            $fullPath = Join-Path $root $safePath

            if ((Test-Path $fullPath) -and (Get-Item $fullPath).PSIsContainer) {
                $fullPath = Join-Path $fullPath "index.html"
            }

            if (-not (Test-Path $fullPath -PathType Leaf)) {
                $res.StatusCode = 404
                $bytes = [System.Text.Encoding]::UTF8.GetBytes("Not Found")
                $res.OutputStream.Write($bytes, 0, $bytes.Length)
                continue
            }

            $bytes = [System.IO.File]::ReadAllBytes($fullPath)
            $res.StatusCode = 200
            $res.ContentType = Get-ContentType $fullPath
            $res.OutputStream.Write($bytes, 0, $bytes.Length)
            continue
        }

        $res.StatusCode = 405
        $bytes = [System.Text.Encoding]::UTF8.GetBytes("Method Not Allowed")
        $res.OutputStream.Write($bytes, 0, $bytes.Length)
    }
    catch {
        $res.StatusCode = 500
        $msg = "Internal Server Error: $($_.Exception.Message)"
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($msg)
        $res.OutputStream.Write($bytes, 0, $bytes.Length)
    }
    finally {
        $res.OutputStream.Close()
    }
}
