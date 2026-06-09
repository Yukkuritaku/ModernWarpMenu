#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import mimetypes
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import unquote, urlparse


ROOT = Path(__file__).resolve().parent.parent
DEFAULT_FILE = ROOT / "tools" / "layout-editor.html"
TARGET_JSON = ROOT / "src" / "main" / "generated" / "assets" / "modernwarpmenu" / "layouts" / "layout.json"


class Handler(BaseHTTPRequestHandler):
    server_version = "LayoutEditorServer/1.0"

    def _write(self, code: int, body: bytes, content_type: str = "text/plain; charset=utf-8") -> None:
        self.send_response(code)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def do_POST(self) -> None:  # noqa: N802
        parsed_url = urlparse(self.path)
        path = parsed_url.path.lstrip("/")
        if path != "api/save-layout":
            self._write(404, b"Not Found")
            return

        length = int(self.headers.get("Content-Length", "0"))
        payload = self.rfile.read(length)
        try:
            text = payload.decode("utf-8")
            json.loads(text)
            target = "default"
            if parsed_url.query:
                for item in parsed_url.query.split("&"):
                    if item.startswith("target="):
                        target = item.split("=", 1)[1].strip().lower() or "default"
                        break

            if target == "ultrawide":
                target_file = ROOT / "src" / "main" / "resources" / "resourcepacks" / "ultra_wide_layout" / "assets" / "modernwarpmenu" / "layouts" / "layout.json"
            elif target == "default":
                target_file = TARGET_JSON
            else:
                self._write(400, b'{"ok":false,"error":"Invalid save target"}', "application/json; charset=utf-8")
                return

            target_file.write_text(text, encoding="utf-8")
            self._write(200, b'{"ok":true}', "application/json; charset=utf-8")
        except Exception as exc:  # pragma: no cover
            msg = f'{{"ok":false,"error":"{str(exc).replace(chr(34), "")}"}}'.encode("utf-8")
            self._write(500, msg, "application/json; charset=utf-8")

    def do_GET(self) -> None:  # noqa: N802
        raw = urlparse(self.path).path
        req_path = unquote(raw).lstrip("/")
        if req_path == "":
            file_path = DEFAULT_FILE
        else:
            file_path = (ROOT / req_path).resolve()

        try:
            file_path.relative_to(ROOT)
        except ValueError:
            self._write(403, b"Forbidden")
            return

        if file_path.is_dir():
            file_path = file_path / "index.html"

        if not file_path.is_file():
            self._write(404, b"Not Found")
            return

        mime, _ = mimetypes.guess_type(str(file_path))
        if not mime:
            mime = "application/octet-stream"
        if mime.startswith("text/") or mime in ("application/javascript", "application/json"):
            mime = f"{mime}; charset=utf-8"

        self._write(200, file_path.read_bytes(), mime)


def main() -> None:
    parser = argparse.ArgumentParser(description="ModernWarpMenu layout editor server")
    parser.add_argument("--port", type=int, default=8756)
    args = parser.parse_args()

    httpd = ThreadingHTTPServer(("127.0.0.1", args.port), Handler)
    print(f"Layout editor server running at http://localhost:{args.port}/tools/layout-editor.html")
    print(f"Serving repo root: {ROOT}")
    print("Press Ctrl+C to stop.")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        httpd.server_close()


if __name__ == "__main__":
    main()
