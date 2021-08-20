#!/usr/bin/env bash

ROOT="$(dirname "${BASH_SOURCE[0]}")"
OS="$(uname | tr '[:upper:]' '[:lower:]')"
case $OS in
  darwin*)  echo "OSX detected" && "${ROOT}"/setupOSX.sh ;;
  linux*)   echo "Linux detected" && "${ROOT}"/setupUbuntu.sh ;;
  msys*)    echo "Windows detected (unsupported)" && exit 1 ;;
  mingw*)    echo "Mingw detected" && "${ROOT}"/setupMingw.sh ;;
  *)        echo "Unknown OS: $OS" && exit 1 ;;
esac
