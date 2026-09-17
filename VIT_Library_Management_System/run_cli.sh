#!/bin/bash
# VIT Library Management System - Terminal CLI Mode for Linux / macOS / WSL
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

echo "================================================================================"
echo "   VIT Library Management System - Terminal CLI Execution"
echo "   Fully Executable via Command Line (Headless Compliant)"
echo "================================================================================"
echo ""

mkdir -p bin

javac -cp "lib/sqlite-jdbc.jar" -d bin src/com/vityarthi/library/*.java test/com/vityarthi/library/*.java
java --enable-native-access=ALL-UNNAMED -cp "bin:lib/sqlite-jdbc.jar" com.vityarthi.library.CLI
