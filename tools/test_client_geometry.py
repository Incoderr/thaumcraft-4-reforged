"""Run the scanner's pure geometry regressions using the cached JOML dependency."""
import os
from pathlib import Path
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parent.parent
JOML_ROOT = Path(os.environ.get("GRADLE_USER_HOME", Path.home() / ".gradle")) / "caches/modules-2/files-2.1/org.joml/joml"
JOML = sorted(JOML_ROOT.glob("*/*/joml-*.jar"))[-1]
JAVA_HOME = os.environ.get("JAVA_HOME")
JAVA = str(Path(JAVA_HOME) / "bin/java") if JAVA_HOME else shutil.which("java")
JAVAC = str(Path(JAVA_HOME) / "bin/javac") if JAVA_HOME else shutil.which("javac")
with tempfile.TemporaryDirectory(prefix="thaumcraft-geometry-") as folder:
    sources = [ROOT / "src/main/java/thaumcraft/client/geom" / f"{name}.java" for name in ["QuadSink", "ClippedQuadSink", "ProjectedWindow", "EldritchSurfaceGeometry"]]
    sources.append(ROOT / "tools/tests/ClientGeometryTest.java")
    subprocess.run([JAVAC, "-cp", str(JOML), "-d", folder, *map(str, sources)], check=True)
    subprocess.run([JAVA, "-cp", os.pathsep.join([folder, str(JOML)]), "ClientGeometryTest"], check=True)
